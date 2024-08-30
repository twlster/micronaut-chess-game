package example.micronaut.chess.cucumber.steps;

import example.micronaut.chess.api.dto.GameDTO;
import example.micronaut.chess.api.dto.GameStateDTO;
import example.micronaut.chess.ContainersConfiguration;
import jakarta.inject.Singleton;

import java.util.Collection;

@Singleton
public class StepContext {

    //Start of Dirty Code
    private final Collection<GameDTO> receivedGames = ContainersConfiguration.receivedGames;
    private final Collection<GameStateDTO> receivedMoves = ContainersConfiguration.receivedMoves;
    //End of Dirty Code
    private GameDTO game;

    public Collection<GameDTO> getReceivedGames() {
        return receivedGames;
    }

    public Collection<GameStateDTO> getReceivedMoves() {
        return receivedMoves;
    }

    public GameDTO getGame() {
        return game;
    }

    public void setGame(GameDTO game) {
        this.game = game;
    }
}
