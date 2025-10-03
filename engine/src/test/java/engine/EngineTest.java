package engine;

import api.events.Ping;
import engine.services.plugin.PluginService;
import io.micronaut.context.event.ApplicationEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class EngineTest {

    @Mock
    private IApplication application;

    @Mock
    private PluginService pluginService;

    @Mock
    private ApplicationEventPublisher<Ping> publisher;

    @InjectMocks
    private Engine engine;

    @BeforeEach
    void setUp() {
        // This initializes the mocks before each test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testStart() {
        // This is a pure unit test. It calls the start() method and uses Mockito
        // to verify that the engine correctly calls the start() methods of its collaborators.
        engine.start();

        verify(pluginService).start();
        verify(application).start();
        verify(application).run();
        verify(application).stop();
    }
}
