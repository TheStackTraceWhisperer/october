package engine.services.plugin;

import api.events.PluginStarted;
import api.events.PluginStopped;
import api.plugin.IPlugin;
import io.micronaut.context.event.ApplicationEventPublisher;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Singleton
@RequiredArgsConstructor
public class PluginService {
  final List<IPlugin> plugins;

  @SuppressWarnings("rawtypes") // The raw ApplicationEventPublisher accepts all types of events
  private final ApplicationEventPublisher eventPublisher;

  @SuppressWarnings("unchecked")
  public void start() {
    plugins.forEach(p -> {
      p.start();
      eventPublisher.publishEvent(new PluginStarted(p));
    });
  }

  @SuppressWarnings("unchecked")
  public void stop() {
    plugins.forEach(p -> {
      p.stop();
      eventPublisher.publishEvent(new PluginStopped(p));
    });
  }

}
