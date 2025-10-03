package engine;

import engine.services.state.GameState;
import io.micronaut.context.ApplicationContext;

import java.util.function.Function;

public interface IApplication {
  void start();
  void run();
  void stop();
}
