package example.micronaut.chess.listener;

import example.micronaut.chess.model.dto.GameDTO;
import example.micronaut.chess.model.dto.GameStateDTO;
import example.micronaut.chess.service.GameService;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import lombok.AllArgsConstructor;

import static io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST;

@KafkaListener(offsetReset = EARLIEST)
@AllArgsConstructor
class ChessListener {

    private final GameService gameService;

    @Topic("chessGame")
    void onGame(GameDTO gameDTO) {
        if (gameDTO.isDraw()) {
            gameService.draw(gameDTO);
        }
        else if (gameDTO.getWinner() != null) {
            gameService.checkmate(gameDTO);
        }
        else {
            gameService.newGame(gameDTO);
        }
    }

    @Topic("chessGameState")
    void onGameState(GameStateDTO gameState) {
        gameService.newGameState(gameState);
    }
}