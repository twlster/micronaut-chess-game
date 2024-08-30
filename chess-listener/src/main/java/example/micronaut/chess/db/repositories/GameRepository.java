package example.micronaut.chess.db.repositories;

import example.micronaut.chess.db.entities.Game;
import io.micronaut.data.repository.CrudRepository;

import java.util.UUID;

public interface GameRepository extends CrudRepository<Game, UUID> {
}