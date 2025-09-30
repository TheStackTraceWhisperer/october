package engine;

import api.events.Ping;
import io.micronaut.context.event.ApplicationEventPublisher;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class Engine {
  private final IApplication application;
  private final PluginManager pluginManager;
  private final ApplicationEventPublisher<Ping> publisher;

  public void start() {
    log.info("Starting engine...");
    pluginManager.start();
    log.info("Engine started!");

    publisher.publishEvent(new Ping("ping 1"));

    application.start();
    publisher.publishEvent(new Ping("ping 2"));

    application.run();
    publisher.publishEvent(new Ping("ping 3"));

    application.stop();
    publisher.publishEvent(new Ping("ping 4"));

    log.info("Stopping engine...");
    pluginManager.stop();
    log.info("Engine stopped!");
  }

}
