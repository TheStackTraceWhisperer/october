package engine;

import engine.services.plugin.PluginService;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class EngineIT {

    @Inject
    private Engine engine;

    @Inject
    private PluginService pluginService;

    @Test
    void testEngineStarts() {
        // This is an integration test that starts a Micronaut context for the engine module.
        // It verifies that the Engine bean is created and can be injected.
        assertThat(engine).isNotNull();
    }

    @Test
    void testPluginManagerIsInjectedWithNoPlugins() {
        // In this test, the 'plugin' module is not on the classpath, so we expect
        // the PluginManager to be injected with an empty list of plugins.
        assertThat(pluginService).isNotNull();
        assertThat(pluginService.plugins).isEmpty();
    }
}
