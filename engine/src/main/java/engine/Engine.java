package engine;

import engine.services.glfw.GlfwContextService;
import engine.services.state.ApplicationStateService;
import engine.services.time.SystemTimeService;
import engine.services.window.WindowService;
import io.micronaut.context.ApplicationContext;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import september.engine.assets.ResourceManager;
//import september.engine.audio.AudioManager;
//import september.engine.ecs.IWorld;
//import september.engine.ecs.SystemManager;
//import september.engine.rendering.Camera;
//import september.engine.rendering.Renderer;
//import september.engine.state.GameState;

@Slf4j
@Singleton
@RequiredArgsConstructor
public final class Engine implements Runnable {

  private final ApplicationContext ctx;

  private final IApplication application;
  private final ApplicationLoopPolicy loopPolicy;
  private final ApplicationStateService applicationStateService;
  private final GlfwContextService glfwContextService;
  @Getter final private WindowService windowService;
  private final SystemTimeService systemTimeService;

  //private ApplicationContext applicationContext;
  //private EngineServices services;

  // Getters for tests - delegate to the injected services
//  @Getter private IWorld world;
//  @Getter private ResourceManager resourceManager;
//  @Getter private Camera camera;
//  @Getter private AudioManager audioManager;

//  @Getter private Renderer renderer;
//  @Getter private SystemManager systemManager;

  public void init() {
    try {
      log.info("Initializing September Engine with DI container");

      // Create the DI container with eager singleton initialization disabled

      // Get the main services aggregator
      //services = applicationContext.getBean(EngineServices.class);

      // Initialize the scene manager with the game's component registry
      //services.sceneManager().initialize(game.getComponentRegistry());

      // Set up test getters by delegating to the services
//      world = services.world();
//      resourceManager = services.resourceManager();
//      camera = services.camera();
//      audioManager = services.audioManager();
//      window = services.window();
//      renderer = services.renderer();
//      systemManager = services.systemManager();

      // --- SET UP CALLBACKS ---
//      window.setResizeListener((width, height) -> {
//        camera.resize(width, height);
//        camera.setPerspective(45.0f, (float) width / height, 0.1f, 100.0f);
//      });
//
//      services.inputService().installCallbacks(window);
//
//      audioManager.initialize();

      // --- INITIALIZE THE GAME AND SET THE INITIAL STATE ---
//      application.init(services);
//      // Get the initial state from the game, but allow DI injection if needed
//      GameState initialState = game.getInitialState(services, applicationContext);
//      services.gameStateManager().pushState(initialState, services);

      applicationStateService.start(ctx);
      glfwContextService.start();
      windowService.start();

      log.info("September Engine initialized successfully");

    } catch (Exception e) {
      shutdown();
      throw new RuntimeException("Engine initialization failed", e);
    }
  }

  private void mainLoop() {
    int frames = 0;

    while (loopPolicy.continueRunning(frames, windowService.getHandle()) && !applicationStateService.isEmpty()) {
      windowService.pollEvents();
      systemTimeService.update();
      float dt = systemTimeService.getDeltaTimeSeconds();
      //services.gameStateManager().update(services, dt);
      //services.systemManager().updateAll(dt);
      applicationStateService.update(dt);
      windowService.swapBuffers();
      frames++;
    }
  }

  public void shutdown() {
    log.info("Shutting down September Engine");

    windowService.stop();
    glfwContextService.stop();
    // Close the DI container, which will trigger @PreDestroy methods
//    if (applicationContext != null) {
//      try {
//        applicationContext.close();
//      } catch (Exception e) {
//        log.error("Error closing DI container", e);
//      }
//    }
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

