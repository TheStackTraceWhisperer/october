package engine;

import api.events.EngineStarted;
import api.events.EngineStopped;
import engine.services.event.EventPublisherService;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(packages = "engine")
class EngineTest extends EngineTestHarness {

    @Inject
    private EventPublisherService eventPublisherService;

    @Inject
    private TestListener listener;

    @Test
    void testEngineStartAndStopEvents() {
        // Given
        assertFalse(listener.hasStarted, "Engine should not have started yet.");
        assertFalse(listener.hasStopped, "Engine should not have stopped yet.");

        // When
        engine.init();

        // Then
        assertTrue(listener.hasStarted, "Engine should have started.");
        assertFalse(listener.hasStopped, "Engine should not have stopped yet.");

        // When
        engine.shutdown();

        // Then
        assertTrue(listener.hasStopped, "Engine should have stopped.");
    }

    @Singleton
    static class TestListener implements ApplicationEventListener<Object> {

        boolean hasStarted = false;
        boolean hasStopped = false;

        @Override
        public void onApplicationEvent(Object event) {
            if (event instanceof EngineStarted) {
                hasStarted = true;
            }
            if (event instanceof EngineStopped) {
                hasStopped = true;
            }
        }

        @Override
        public boolean supports(Object event) {
            return event instanceof EngineStarted || event instanceof EngineStopped;
        }
    }
}
