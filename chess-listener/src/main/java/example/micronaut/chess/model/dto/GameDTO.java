package example.micronaut.chess.model.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@Introspected
@JsonTypeInfo(use = NAME, property = "_className")
@Getter
public class GameDTO {

    @Size(max = 36)
    @NotNull
    private final String id;

    @Size(max = 255)
    @Nullable
    private final String blackName;

    @Size(max = 255)
    @Nullable
    private final String whiteName;

    private final boolean draw;

    @Size(max = 1)
    private final Player winner;

    @Creator
    public GameDTO(@NonNull String id,
                   @Nullable String blackName,
                   @Nullable String whiteName,
                   boolean draw,
                   @Nullable Player winner) {
        this.id = id;
        this.blackName = blackName;
        this.whiteName = whiteName;
        this.draw = draw;
        this.winner = winner;
    }

    public GameDTO(@NonNull String id,
                   @NonNull String blackName,
                   @NonNull String whiteName) {
        this(id, blackName, whiteName, false, null);
    }

    public GameDTO(@NonNull String id,
                   boolean draw,
                   @Nullable Player winner) {
        this(id, null, null, draw, winner);
    }
}