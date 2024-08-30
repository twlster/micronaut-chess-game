package example.micronaut.chess.api.controller;

import example.micronaut.chess.api.dto.Player;
import example.micronaut.chess.api.dto.GameDTO;
import example.micronaut.chess.api.dto.GameStateDTO;
import example.micronaut.chess.client.GameReporter;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Status;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static io.micronaut.http.HttpStatus.CREATED;
import static io.micronaut.http.HttpStatus.NO_CONTENT;
import static io.micronaut.http.MediaType.APPLICATION_FORM_URLENCODED;
import static io.micronaut.http.MediaType.TEXT_PLAIN;

@Controller("/game")
@ExecuteOn(TaskExecutors.BLOCKING)
class GameController {

    private final GameReporter gameReporter;

    GameController(GameReporter gameReporter) {
        this.gameReporter = gameReporter;
    }

    @Post(value = "/start",
            consumes = APPLICATION_FORM_URLENCODED,
            produces = TEXT_PLAIN)
    @Status(CREATED)
    Mono<String> start(String b,
                       String w) {
        GameDTO game = new GameDTO(UUID.randomUUID().toString(), b, w);
        return gameReporter.game(game.getId(), game).map(gameDTO -> game.getId());
    }

    @Post(value = "/move/{gameId}",
            consumes = APPLICATION_FORM_URLENCODED)
    @Status(CREATED)
    void move(@PathVariable String gameId,
              Player player,
              String move,
              String fen,
              String pgn) {
        GameStateDTO gameState = new GameStateDTO(UUID.randomUUID().toString(),
                gameId, player, move, fen, pgn);
        gameReporter.gameState(gameId, gameState).subscribe();
    }

    @Post("/checkmate/{gameId}/{player}")
    @Status(NO_CONTENT)
    void checkmate(@PathVariable String gameId,
                   @PathVariable Player player) {
        GameDTO game = new GameDTO(gameId, false, player);
        gameReporter.game(gameId, game).subscribe();
    }

    @Post("/draw/{gameId}")
    @Status(NO_CONTENT)
    void draw(@PathVariable String gameId) {
        GameDTO game = new GameDTO(gameId, true, null);
        gameReporter.game(gameId, game).subscribe();
    }
}