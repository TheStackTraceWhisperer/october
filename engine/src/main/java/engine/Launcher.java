package engine;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;

public class Launcher {
  public static void run(Class<?> primarySource, String[] args) {
    try (
      ApplicationContext context = Micronaut
        .build(args)
        .mainClass(primarySource)
        .packages("engine")
        .banner(false)
        .start()
    ) {
      Engine engine = context.getBean(Engine.class);
      engine.run();
    }
  }
}
