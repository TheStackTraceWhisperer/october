package application;

import engine.IApplication;
import engine.Launcher;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class Main implements IApplication {

    public static void main(String[] args) {
      Launcher.run(Main.class, args);
    }

    @Override
    public void start() {
        log.info("Starting application...");
    }

    @Override
    public void run() {
        log.info("Application started...");
    }

    @Override
    public void stop() {
        log.info("Application stopped...");
    }
}
