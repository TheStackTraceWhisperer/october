package application;

import engine.ecs.IComponent;
import engine.services.rendering.Camera;
import engine.services.rendering.RenderingService;
import engine.services.resources.AssetCacheService;
import engine.services.scene.SceneService;
import engine.services.state.ApplicationState;
import engine.services.time.SystemTimeService;
import engine.services.window.WindowService;
import engine.services.world.WorldService;
import engine.services.world.components.*;
import engine.services.world.systems.MovementSystem;
import engine.services.world.systems.RenderSystem;
import io.micronaut.context.annotation.Prototype;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

@Prototype
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class PlayingState implements ApplicationState {

  private final SceneService sceneService;
  private final WorldService worldService;
  private final AssetCacheService assetCacheService;
  private final RenderingService renderingService;
  private final Camera camera;
  private final WindowService windowService;
  private final InputMappingService inputMappingService;
  private final SystemTimeService timeService;

  @Override
  public void onEnter() {
    // --- Set up the game world ---
    initializeSceneAndSystems();

    // --- Configure the main game camera ---
    camera.setPosition(new Vector3f(0.0f, 0.0f, 5.0f));
    // Set the projection based on the current window size
    camera.resize(windowService.getWidth(), windowService.getHeight());
    // Ensure the camera updates if the window is resized
    windowService.setResizeListener(camera::resize);
  }

  private void initializeSceneAndSystems() {
    // Define all components the scene loader might encounter
    Map<String, Class<? extends IComponent>> registry = new HashMap<>();
    registry.put("TransformComponent", TransformComponent.class);
    registry.put("SpriteComponent", SpriteComponent.class);
    registry.put("ControllableComponent", ControllableComponent.class);
    registry.put("MovementStatsComponent", MovementStatsComponent.class);
    registry.put("ColliderComponent", ColliderComponent.class);
    registry.put("PlayerComponent", PlayerComponent.class);
    registry.put("EnemyComponent", EnemyComponent.class);
    registry.put("HealthComponent", HealthComponent.class);

    // Load the scene file
    sceneService.initialize(registry);
    sceneService.load("/scenes/playing-scene.json");

    // Register all game-related systems
    worldService.addSystem(new PlayerInputSystem(inputMappingService));
    worldService.addSystem(new MovementSystem());
    worldService.addSystem(new EnemyAISystem(timeService));
    worldService.addSystem(new CollisionSystem());
    worldService.addSystem(new RenderSystem(renderingService, assetCacheService, camera));
  }

  @Override
  public void onUpdate(float deltaTime) {
    // All logic is handled by the systems, so this can be empty for now
  }

  @Override
  public void onExit() {
    // Clear out all the game systems to prepare for the next state
    worldService.clearSystems();
  }
}