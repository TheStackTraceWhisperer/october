package api.plugin;

public interface IPlugin {
  String getName();

  void start();

  void stop();
}