package engine.services.plugin;

import api.events.PluginStarted;
import api.events.PluginStopped;
import api.plugin.IPlugin;
import engine.IService;
import engine.services.event.EventPublisherService;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Singleton
@RequiredArgsConstructor
public class PluginService implements IService {
  private final List<IPlugin> plugins;
  private final EventPublisherService eventPublisherService;

  public int pluginCount() {
    return plugins.size();
  }

  public void start() {
    plugins.forEach(p -> {
      p.start();
      eventPublisherService.publish(new PluginStarted(p));
    });
  }

  public void stop() {
    plugins.forEach(p -> {
      p.stop();
      eventPublisherService.publish(new PluginStopped(p));
    });
  }

}
