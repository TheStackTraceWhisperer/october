package engine;

import api.events.PluginStarted;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class EngineListener {
  // TODO: This is just a placeholder to show engine @Singletons are wired up and listen
  @EventListener
  public void onPluginStarted(PluginStarted event) {
    log.info("plugin started: {}", event.plugin().getName());
  }
}
