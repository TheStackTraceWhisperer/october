package engine;

import api.events.PluginStarted;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class EngineListener {
  @EventListener
  public void onPluginStarted(PluginStarted event) {
    log.info("plugin started: {}", event.plugin().getName());
  }
}
