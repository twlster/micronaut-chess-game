package example.micronaut.chess;

import example.micronaut.chess.api.dto.GameDTO;
import example.micronaut.chess.api.dto.GameStateDTO;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.test.support.TestPropertyProvider;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.checkerframework.checker.nullness.qual.NonNull;
import org.testcontainers.utility.DockerImageName;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

import static io.micronaut.configuration.kafka.annotation.OffsetReset.EARLIEST;

//@Testcontainers
public class ContainersConfiguration {//implements TestPropertyProvider {

    public static final Collection<GameDTO> receivedGames = new ConcurrentLinkedDeque<>();
    public static final Collection<GameStateDTO> receivedMoves = new ConcurrentLinkedDeque<>();

//    @Container
//    public static KafkaContainer kafka = new KafkaContainer(
//            DockerImageName.parse("confluentinc/cp-kafka:latest"));
//
//    @NonNull
//    @Override
//    public Map<String, String> getProperties() {
//        return Collections.singletonMap(
//                "kafka.bootstrap.servers", kafka.getBootstrapServers()
//        );
//    }

    @KafkaListener(offsetReset = EARLIEST)
    protected static class ChessListener {

        @Topic("chessGame")
        void onGame(GameDTO game) {
            receivedGames.add(game);
        }

        @Topic("chessGameState")
        void onGameState(GameStateDTO gameState) {
            receivedMoves.add(gameState);
        }
    }

}
