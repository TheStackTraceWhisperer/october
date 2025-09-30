package api.events;

import api.plugin.IPlugin;

public record PluginStarted(
  IPlugin plugin
) {
}
