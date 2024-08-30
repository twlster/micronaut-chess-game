package example.micronaut.chess.cucumber.micronaut;

import example.micronaut.chess.ContainersConfiguration;
import io.cucumber.core.backend.ObjectFactory;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;
import io.micronaut.runtime.server.EmbeddedServer;

import java.util.Collections;
import java.util.Map;

public final class MicronautObjectFactory extends ContainersConfiguration implements ObjectFactory {

    private EmbeddedServer embeddedServer;

    @Override
    public void start() {
        kafka.start();
        Map<String, Object> properties = Collections.singletonMap(
                "kafka.bootstrap.servers", kafka.getBootstrapServers());
        embeddedServer = ApplicationContext.run(EmbeddedServer.class, properties, Environment.TEST);
    }

    @Override
    public void stop() {
        if (embeddedServer != null) {
            embeddedServer.stop();
        }
        embeddedServer = null;

        if(kafka.isRunning()){
            kafka.stop();
        }

    }

    @Override
    public <T> T getInstance(final Class<T> beanType) {
        return embeddedServer.getApplicationContext().getBean(beanType);
    }

    @Override
    public boolean addClass(final Class<?> aClass) {
        return true;
    }

}
