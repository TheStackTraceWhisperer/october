package engine;

import api.events.PluginStarted;
import api.events.PluginStopped;
import api.plugin.IPlugin;
import io.micronaut.context.event.ApplicationEventPublisher;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Singleton
@RequiredArgsConstructor
public class PluginManager {
  private final List<IPlugin> plugins;
  private final ApplicationEventPublisher<PluginStarted> pluginStartedApplicationEventPublisher;
  private final ApplicationEventPublisher<PluginStopped> pluginStoppedApplicationEventPublisher;

  public void start() {
    plugins.forEach(p -> {
      p.start();
      pluginStartedApplicationEventPublisher.publishEvent(new PluginStarted(p));
    });
  }

  public void stop() {
    plugins.forEach(p -> {
      p.stop();
      pluginStoppedApplicationEventPublisher.publishEvent(new PluginStopped(p));
    });
  }

}
