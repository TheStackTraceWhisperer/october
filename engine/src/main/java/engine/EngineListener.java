package engine;

import api.events.PluginStarted;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class EngineListener {

  public boolean pluginStarted = false; // Add this field for testing

  @EventListener
  public void onPluginStarted(PluginStarted event) {
    log.info("plugin started: {}", event.plugin().getName());
    this.pluginStarted = true; // Set the flag when the event is received
  }
}