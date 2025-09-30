package api.events;

import api.plugin.IPlugin;

public record PluginStopped(
  IPlugin plugin
) {
}
