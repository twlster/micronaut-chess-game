package example.micronaut.chess.client;

import example.micronaut.chess.api.dto.GameDTO;
import example.micronaut.chess.api.dto.GameStateDTO;
import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.core.annotation.NonNull;
import reactor.core.publisher.Mono;

@KafkaClient
public interface GameReporter {

    @Topic("chessGame")
    @NonNull
    Mono<GameDTO> game(@NonNull @KafkaKey String gameId,
                       @NonNull GameDTO game);

    @Topic("chessGameState")
    @NonNull
    Mono<GameStateDTO> gameState(@NonNull @KafkaKey String gameId,
                                 @NonNull GameStateDTO gameState);
}