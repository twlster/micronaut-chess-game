package example.micronaut.chess.model.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

/**
 * DTO for <code>GameState</code> entity.
 */
@Introspected
@JsonTypeInfo(use = NAME, property = "_className")
@Getter
public class GameStateDTO {

    @Size(max = 36)
    @NotNull
    private final String id;

    @Size(max = 36)
    @NotNull
    private final String gameId;

    @Size(max = 1)
    @NotNull
    private final Player player;

    @Size(max = 100)
    @NotNull
    private final String fen;

    @NotNull
    private final String pgn;

    @Size(max = 10)
    @NotNull
    private final String move;

    public GameStateDTO(@NonNull String id,
                        @NonNull String gameId,
                        @NonNull Player player,
                        @NonNull String move,
                        @NonNull String fen,
                        @NonNull String pgn) {
        this.id = id;
        this.gameId = gameId;
        this.player = player;
        this.move = move;
        this.fen = fen;
        this.pgn = pgn;
    }
}