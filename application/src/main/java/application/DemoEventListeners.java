package application;

import api.events.Ping;
import api.events.PluginStarted;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class DemoEventListeners {

  @EventListener
  public void onPing(Ping event) {
    log.info("Received ping event: {}", event.message());
  }

  @EventListener
  public void onPluginStarted(PluginStarted event) {
    log.info("PLUGIN STARTED: {}", event);
  }
}
