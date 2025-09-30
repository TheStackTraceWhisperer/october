package engine;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class EngineIT {

    @Inject
    private Engine engine;

    @Inject
    private PluginManager pluginManager;

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
        assertThat(pluginManager).isNotNull();
        assertThat(pluginManager.plugins).isEmpty();
    }
}
