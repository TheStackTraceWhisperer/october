package engine;

import io.micronaut.context.ApplicationContext;
import io.micronaut.runtime.Micronaut;

public class Launcher {
    public static void run(Class<?> primarySource, String[] args) {
        try (ApplicationContext context = Micronaut.build(args).mainClass(primarySource).banner(false).start()) {
            Engine engine = context.getBean(Engine.class);
            engine.start();
        }
    }
}
