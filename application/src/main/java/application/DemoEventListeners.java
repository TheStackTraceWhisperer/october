package application;

import engine.events.EngineStarted;
import engine.events.EngineStopped;
import engine.events.Ping;
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
  public void onEngineStarted(EngineStarted event) {
    log.info("ENGINE STARTED");
  }

  @EventListener
  public void onEngineStopped(EngineStopped event) {
    log.info("ENGINE STOPPED");
  }
}
