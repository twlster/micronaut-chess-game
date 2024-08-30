package example.micronaut.chess.db.entities;

import example.micronaut.chess.model.dto.Player;
import example.micronaut.chess.model.dto.GameDTO;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedEntity("GAME")
@Getter
@Setter
public class Game {

    @Id
    @NotNull
    @NonNull
    private final UUID id;

    @Size(max = 255)
    @NonNull
    private final String blackName;

    @Size(max = 255)
    @NonNull
    private final String whiteName;

    @DateCreated
    private LocalDateTime dateCreated;

    @DateUpdated
    private LocalDateTime dateUpdated;

    private boolean draw;

    @Nullable
    @Size(max = 1)
    private Player winner;

    public Game(@NonNull UUID id,
                @NonNull String blackName,
                @NonNull String whiteName) {
        this.id = id;
        this.blackName = blackName;
        this.whiteName = whiteName;
    }

    @NonNull
    public GameDTO toDto() {
        return new GameDTO(id.toString(), blackName, whiteName, draw, winner);
    }
}