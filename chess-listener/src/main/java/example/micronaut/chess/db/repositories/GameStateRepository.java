package example.micronaut.chess.db.repositories;

import example.micronaut.chess.db.entities.GameState;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.annotation.Join;
import io.micronaut.data.repository.CrudRepository;

import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

import static io.micronaut.data.annotation.Join.Type.FETCH;

public interface GameStateRepository extends CrudRepository<GameState, UUID> {

    @Override
    @NonNull
    @Join(value = "game", type = FETCH)
    Optional<GameState> findById(@NotNull @NonNull UUID id);
}
