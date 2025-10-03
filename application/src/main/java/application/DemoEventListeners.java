package application;

import api.events.EngineStarted;
import api.events.EngineStopped;
import api.events.Ping;
import api.events.PluginStarted;
import api.events.PluginStopped;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class DemoEventListeners {

  @EventListener
  public void onPing(Ping event) {
    log.info("PING: {}", event.message());
  }

  @EventListener
  public void onPluginStarted(PluginStarted event) {
    log.info("PLUGIN STARTED: {}", event);
  }

  @EventListener
  public void onPluginStopped(PluginStopped event) {
    log.info("PLUGIN STOPPED: {}", event);
  }

  @EventListener
  public void onEngineStarted(EngineStarted event) {
    log.info("ENGINE STARTED");
  }

  @EventListener
  public void onEngineStopped(EngineStopped event) {
    log.info("ENGINE STOPPED");
  }
}
