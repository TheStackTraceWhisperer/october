package engine;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;

public class Launcher {
  public static void main(String[] args) {
    try (ApplicationContext context = Micronaut.run(args)) {
      Engine engine = context.getBean(Engine.class);
      engine.start();
    }
  }
}
