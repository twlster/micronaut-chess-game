package example.micronaut.chess.cucumber.steps;

import example.micronaut.chess.api.dto.GameDTO;
import example.micronaut.chess.api.dto.GameStateDTO;
import example.micronaut.chess.api.dto.Player;
import example.micronaut.chess.common.ChessRestClient;
import io.cucumber.java.en.Given;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import jakarta.inject.Inject;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.micronaut.http.MediaType.APPLICATION_FORM_URLENCODED_TYPE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameSteps implements ChessRestClient {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    private StepContext context;

    @Override
    public HttpClient getClient() {
        return this.client;
    }

    @Given("a game is created")
    public void givenGameWithNameAndPlayer() {
        String blackName = "b_name";
        String whiteName = "w_name";

        // start game
        Optional<String> result = startGame(blackName, whiteName);

        String gameId = result.orElseThrow(() -> new RuntimeException("Expected GameDTO id"));

        await().atMost(5, SECONDS).until(() -> !context.getReceivedGames().isEmpty());

        context.setGame(new GameDTO(gameId, blackName, whiteName));
    }

    @Given("^the \"([^\"]*)\" makes a move$")
    public void thenTheWMakesAMove(String player) {
        context.getReceivedGames().clear();

        makeMove(context.getGame().getId(), Player.getValue(player), "f3", "rnbqkbnr/pppppppp/8/8/8/5P2/PPPPP1PP/RNBQKBNR b KQkq - 0 1", "1. f3");
        await().atMost(5, SECONDS).until(() -> !context.getReceivedMoves().isEmpty());

        //assertEquals(0, context.getReceivedGames().size());
        assertEquals(1, context.getReceivedMoves().size());
        List<GameStateDTO> moves = new ArrayList<>(context.getReceivedMoves());

        assertEquals(Player.getValue(player), moves.get(0).getPlayer());
        assertEquals("f3", moves.get(0).getMove());
    }

    @Given("^the \"([^\"]*)\" wins the game$")
    public void thenTheWinnerHasToBe(String winner) {

        context.getReceivedMoves().clear();

        endGame(context.getGame().getId(), Player.getValue(winner));

        await().atMost(5, SECONDS).until(() -> !context.getReceivedGames().isEmpty());

        assertEquals(1, context.getReceivedGames().size());
        //assertEquals(0, context.getReceivedMoves().size());

        GameDTO game = context.getReceivedGames().iterator().next();

        assertEquals(context.getGame().getId(), game.getId());
        assertNull(game.getBlackName());
        assertNull(game.getWhiteName());
        assertFalse(game.isDraw());
        assertEquals(Player.getValue(winner), game.getWinner());
    }

    @Given("the game is a draw")
    public void thenGameIsADraw() {
        context.getReceivedMoves().clear();

        endGame(context.getGame().getId(), null);

        await().atMost(5, SECONDS).until(() -> !context.getReceivedGames().isEmpty());

        assertEquals(1, context.getReceivedGames().size());
        assertEquals(0, context.getReceivedMoves().size());

        GameDTO game = context.getReceivedGames().iterator().next();

        assertEquals(context.getGame().getId(), game.getId());
        assertNull(game.getBlackName());
        assertNull(game.getWhiteName());
        assertTrue(game.isDraw());
        assertNull(game.getWinner());
    }

}
