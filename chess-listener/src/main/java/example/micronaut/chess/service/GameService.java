package example.micronaut.chess.service;

import example.micronaut.chess.model.dto.GameDTO;
import example.micronaut.chess.model.dto.GameStateDTO;
import example.micronaut.chess.db.entities.Game;
import example.micronaut.chess.db.entities.GameState;
import example.micronaut.chess.db.repositories.GameRepository;
import example.micronaut.chess.db.repositories.GameStateRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.micronaut.core.annotation.NonNull;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Singleton
@Transactional
@Slf4j
@AllArgsConstructor
public class GameService {

    private final GameRepository gameRepository;
    private final GameStateRepository gameStateRepository;

    public Game newGame(GameDTO gameDTO) {
        log.debug("New game {}, black: {}, white: {}",
                gameDTO.getId(), gameDTO.getBlackName(), gameDTO.getWhiteName());
        Game game = new Game(UUID.fromString(gameDTO.getId()),
                gameDTO.getBlackName(), gameDTO.getWhiteName());
        if(game.getDateCreated() == null ){
            game.setDateCreated(LocalDateTime.now());
        }
        return gameRepository.save(game);
    }

    public void newGameState(GameStateDTO gameStateDTO) {
        Game game = findGame(gameStateDTO.getGameId());
        GameState gameState = new GameState(
                UUID.fromString(gameStateDTO.getId()), game,
                gameStateDTO.getPlayer(), gameStateDTO.getMove(),
                gameStateDTO.getFen(), gameStateDTO.getPgn());
        if(gameState.getDateCreated() == null ){
            gameState.setDateCreated(LocalDateTime.now());
        }
        gameStateRepository.save(gameState);
    }

    public void checkmate(GameDTO gameDTO) {
        log.debug("Game {} ended with winner: {}",
                gameDTO.getId(), gameDTO.getWinner());
        Game game = findGame(gameDTO.getId());
        game.setWinner(gameDTO.getWinner());
        gameRepository.update(game);
    }

    public void draw(GameDTO gameDTO) {
        log.debug("Game {} ended in a draw", gameDTO.getId());
        Game game = findGame(gameDTO.getId());
        game.setDraw(true);
        gameRepository.update(game);
    }

    @NonNull
    private Game findGame(String gameId) {
        return gameRepository.findById(UUID.fromString(gameId)).orElseThrow(() ->
                new IllegalArgumentException("Game with id '" + gameId + "' not found"));
    }
}