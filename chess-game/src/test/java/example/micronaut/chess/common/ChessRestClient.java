package example.micronaut.chess.common;

import example.micronaut.chess.api.dto.Player;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.uri.UriBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static io.micronaut.http.MediaType.APPLICATION_FORM_URLENCODED_TYPE;

public interface ChessRestClient {

    HttpClient getClient();

    default Optional<String> startGame(String blackName, String whiteName) {
        Map<String, String> body = new HashMap<>();
        body.put(Player.BLACK.toString(), blackName);
        body.put(Player.WHITE.toString(), whiteName);

        HttpRequest<?> request = HttpRequest.POST("/game/start", body)
                .contentType(APPLICATION_FORM_URLENCODED_TYPE);
        return getClient().toBlocking().retrieve(request,
                Argument.of(Optional.class, String.class));
    }

    default void makeMove(String gameId, Player player, String move,
                          String fen, String pgn) {
        Map<String, String> body = new HashMap<>();
        body.put("player", player.toString());
        body.put("move", move);
        body.put("fen", fen);
        body.put("pgn", pgn);

        HttpRequest<?> request = HttpRequest.POST("/game/move/" + gameId, body)
                .contentType(APPLICATION_FORM_URLENCODED_TYPE);
        getClient().toBlocking().exchange(request);
    }

    default void endGame(String gameId, Player winner) {
        UriBuilder uriBuilder = UriBuilder.of("/game").path(winner == null ? "draw" : "checkmate").path(gameId);
        if (winner != null) {
            uriBuilder = uriBuilder.path(winner.toString());
        }
        URI uri = uriBuilder.build();
        HttpRequest<?> request = HttpRequest.POST(uri, null);
        getClient().toBlocking().exchange(request);
    }
}
