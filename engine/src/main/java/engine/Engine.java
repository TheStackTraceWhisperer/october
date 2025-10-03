package engine;

import api.events.EngineStarted;
import api.events.EngineStopped;
import engine.services.event.EventPublisherService;
import engine.services.glfw.GlfwContextService;
import engine.services.state.ApplicationStateService;
import engine.services.time.SystemTimeService;
import engine.services.window.WindowService;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Singleton
@RequiredArgsConstructor
public final class Engine implements Runnable {

  private final EventPublisherService eventPublisherService;
  private final ApplicationLoopPolicy loopPolicy;
  private final ApplicationStateService applicationStateService;
  private final GlfwContextService glfwContextService;
  private final List<IService> services;

  @Getter
  final private WindowService windowService;
  private final SystemTimeService systemTimeService;

  public void init() {
    try {
      log.info("Initializing September Engine with DI container");

      services.sort(Comparator.comparingInt(IService::priority));

      services.forEach(service -> log.info("Service: {}", service.getClass().getSimpleName()));

      services.forEach(IService::start);

      log.info("September Engine initialized successfully");
      eventPublisherService.publish(new EngineStarted());

    } catch (Exception e) {
      shutdown();
      throw new RuntimeException("Engine initialization failed", e);
    }
  }

  private void mainLoop() {
    int frames = 0;

    while (loopPolicy.continueRunning(frames, windowService.getHandle()) && !applicationStateService.isEmpty()) {
      windowService.pollEvents();
      services.forEach(IService::update);
      //systemTimeService.update();
      float dt = systemTimeService.getDeltaTimeSeconds();
      services.forEach(service -> service.update(dt));
      //applicationStateService.update(dt);
      windowService.swapBuffers();
      frames++;
    }
  }

  public void shutdown() {
    log.info("Shutting down September Engine");

    eventPublisherService.publish(new EngineStopped());
    services.forEach(IService::stop);
    //applicationStateService.stop();
    //windowService.stop();
    //glfwContextService.stop();
  }

  @Override
  public void run() {
    try {
      init();
      mainLoop();
    } finally {
      shutdown();
    }
  }
}

