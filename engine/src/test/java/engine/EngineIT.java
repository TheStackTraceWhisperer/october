package engine;

import engine.services.plugin.PluginService;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EngineIT extends EngineTestHarness {

  @Inject
  private PluginService pluginService;

  @Test
  void engineBeanIsCreatedSuccessfully() {
    // Verifies that the Engine bean and its dependencies can be created.
    // This test now implicitly uses the setup from EngineTestHarness.
    assertThat(engine).isNotNull();
  }

  @Test
  void pluginServiceIsCreatedWithNoPlugins() {
    // In this test, the 'plugin' module is not on the classpath, so we expect
    // the PluginService to be injected with an empty list of plugins.
    assertThat(pluginService).isNotNull();
    assertThat(pluginService.pluginCount()).isZero();
  }
}
