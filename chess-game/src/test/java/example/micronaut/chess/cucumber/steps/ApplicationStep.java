package example.micronaut.chess.cucumber.steps;

import io.cucumber.java.en.Given;
import io.micronaut.runtime.EmbeddedApplication;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;

public class ApplicationStep {
    @Inject
    EmbeddedApplication<?> application;

    @Given("The application is running")
    public void that_the_application_is_running() {
        Assertions.assertTrue(application.isRunning());
    }
}
