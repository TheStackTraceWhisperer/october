package engine;

import api.events.PluginStarted;
import api.plugin.IPlugin;
import io.micronaut.context.ApplicationContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest(packages = "engine")
class EngineListenerTest {

  @Inject
  private ApplicationContext applicationContext;

  @Inject
  private EngineListener engineListener;

  @Test
  void onPluginStarted_shouldSetFlag_whenEventIsPublished() {
    // Given
    IPlugin mockPlugin = mock(IPlugin.class);
    when(mockPlugin.getName()).thenReturn("TestPlugin");

    // When
    applicationContext.publishEvent(new PluginStarted(mockPlugin));

    // Then
    assertTrue(engineListener.pluginStarted, "The pluginStarted flag should be true after the event.");
  }
}


