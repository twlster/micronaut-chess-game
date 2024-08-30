package example.micronaut.chess.client;

import example.micronaut.chess.ContainersConfiguration;
import example.micronaut.chess.api.dto.GameDTO;
import example.micronaut.chess.api.dto.GameStateDTO;
import example.micronaut.chess.api.dto.Player;
import example.micronaut.chess.common.ChessRestClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@MicronautTest
@TestInstance(PER_CLASS)
class GameReporterTest extends ContainersConfiguration implements ChessRestClient {

    @Inject
    ChessListener chessListener;

    @Inject
    @Client("/")
    HttpClient client;

    @AfterEach
    void cleanup() {
        receivedGames.clear();
        receivedMoves.clear();
    }

    @Override
    public HttpClient getClient() {
        return this.client;
    }

    @Test
    void testGameEndingInCheckmate() {

        String blackName = "b_name";
        String whiteName = "w_name";

        // start game

        Optional<String> result = startGame(blackName, whiteName);
        String gameId = result.orElseThrow(() -> new RuntimeException("Expected GameDTO id"));

        await().atMost(5, SECONDS).until(() -> !receivedGames.isEmpty());

        assertEquals(1, receivedGames.size());
        assertEquals(0, receivedMoves.size());

        GameDTO game = receivedGames.iterator().next();

        assertEquals(gameId, game.getId());
        assertEquals(blackName, game.getBlackName());
        assertEquals(whiteName, game.getWhiteName());
        assertFalse(game.isDraw());
        assertNull(game.getWinner());

        // make moves
        receivedGames.clear();

        makeMove(gameId, Player.WHITE, "f3", "rnbqkbnr/pppppppp/8/8/8/5P2/PPPPP1PP/RNBQKBNR b KQkq - 0 1", "1. f3");
        makeMove(gameId, Player.BLACK, "e6", "rnbqkbnr/pppp1ppp/4p3/8/8/5P2/PPPPP1PP/RNBQKBNR w KQkq - 0 2", "1. f3 e6");
        makeMove(gameId, Player.WHITE, "g4", "rnbqkbnr/pppp1ppp/4p3/8/6P1/5P2/PPPPP2P/RNBQKBNR b KQkq g3 0 2", "1. f3 e6 2. g4");
        makeMove(gameId, Player.BLACK, "Qh4#", "rnb1kbnr/pppp1ppp/4p3/8/6Pq/5P2/PPPPP2P/RNBQKBNR w KQkq - 1 3", "1. f3 e6 2. g4 Qh4#");

        await().atMost(5, SECONDS).until(() -> receivedMoves.size() > 3);

        assertEquals(0, receivedGames.size());
        assertEquals(4, receivedMoves.size());

        List<GameStateDTO> moves = new ArrayList<>(receivedMoves);

        assertEquals(Player.WHITE, moves.get(0).getPlayer());
        assertEquals("f3", moves.get(0).getMove());

        assertEquals(Player.BLACK, moves.get(1).getPlayer());
        assertEquals("e6", moves.get(1).getMove());

        assertEquals(Player.WHITE, moves.get(2).getPlayer());
        assertEquals("g4", moves.get(2).getMove());

        assertEquals(Player.BLACK, moves.get(3).getPlayer());
        assertEquals("Qh4#", moves.get(3).getMove());

        // end game

        receivedMoves.clear();

        endGame(gameId, Player.BLACK);

        await().atMost(5, SECONDS).until(() -> !receivedGames.isEmpty());

        assertEquals(1, receivedGames.size());
        assertEquals(0, receivedMoves.size());

        game = receivedGames.iterator().next();

        assertEquals(gameId, game.getId());
        assertNull(game.getBlackName());
        assertNull(game.getWhiteName());
        assertFalse(game.isDraw());
        assertEquals(Player.BLACK, game.getWinner());
    }

    @Test
    void testGameEndingInDraw() {

        String blackName = "b_name";
        String whiteName = "w_name";

        // start game

        Optional<String> result = startGame(blackName, whiteName);

        String gameId = result.orElseThrow(() -> new RuntimeException("Expected GameDTO id"));

        await().atMost(5, SECONDS).until(() -> !receivedGames.isEmpty());

        assertEquals(1, receivedGames.size());
        assertEquals(0, receivedMoves.size());

        GameDTO game = receivedGames.iterator().next();

        assertEquals(gameId, game.getId());
        assertEquals(blackName, game.getBlackName());
        assertEquals(whiteName, game.getWhiteName());
        assertFalse(game.isDraw());
        assertNull(game.getWinner());

        // make moves
        receivedGames.clear();

        makeMove(gameId, Player.WHITE, "f3", "rnbqkbnr/pppppppp/8/8/8/5P2/PPPPP1PP/RNBQKBNR b KQkq - 0 1", "1. f3");
        makeMove(gameId, Player.BLACK, "e6", "rnbqkbnr/pppp1ppp/4p3/8/8/5P2/PPPPP1PP/RNBQKBNR w KQkq - 0 2", "1. f3 e6");

        await().atMost(5, SECONDS).until(() -> receivedMoves.size() > 1);

        assertEquals(0, receivedGames.size());
        assertEquals(2, receivedMoves.size());

        // end game

        receivedMoves.clear();

        endGame(gameId, null);

        await().atMost(5, SECONDS).until(() -> !receivedGames.isEmpty());

        assertEquals(1, receivedGames.size());
        assertEquals(0, receivedMoves.size());

        game = receivedGames.iterator().next();

        assertEquals(gameId, game.getId());
        assertNull(game.getBlackName());
        assertNull(game.getWhiteName());
        assertTrue(game.isDraw());
        assertNull(game.getWinner());
    }
}