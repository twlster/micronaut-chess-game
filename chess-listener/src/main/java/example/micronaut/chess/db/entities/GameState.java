package example.micronaut.chess.db.entities;

import example.micronaut.chess.model.dto.Player;
import example.micronaut.chess.model.dto.GameStateDTO;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Relation;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.micronaut.data.annotation.Relation.Kind.MANY_TO_ONE;

@MappedEntity("GAME_STATE")
@Getter
public class GameState {

    @Id
    @NotNull
    @NonNull
    private final UUID id;

    @Relation(MANY_TO_ONE)
    @NotNull
    @NonNull
    private final Game game;

    @DateCreated
    @Setter
    private LocalDateTime dateCreated;

    @Size(max = 1)
    @NotNull
    @NonNull
    private final Player player;

    // https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation
    @Size(max = 100)
    @NotNull
    @NonNull
    private final String fen;

    // https://en.wikipedia.org/wiki/Portable_Game_Notation
    @NotNull
    @NonNull
    private final String pgn;

    @Size(max = 10)
    @NotNull
    @NonNull
    private final String move;

    public GameState(@NonNull UUID id,
                     @NonNull Game game,
                     @NonNull Player player,
                     @NonNull String move,
                     @NonNull String fen,
                     @NonNull String pgn) {
        this.id = id;
        this.game = game;
        this.player = player;
        this.move = move;
        this.fen = fen;
        this.pgn = pgn;
    }

    @NonNull
    public GameStateDTO toDto() {
        return new GameStateDTO(id.toString(), game.getId().toString(), player, move, fen, pgn);
    }
}