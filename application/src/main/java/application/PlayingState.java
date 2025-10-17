package application;

import engine.services.rendering.Camera;
import engine.services.scene.SceneService;
import engine.services.state.ApplicationState;
import engine.services.window.WindowService;
import engine.services.world.WorldService;
import engine.services.world.systems.*;
import io.micronaut.context.annotation.Prototype;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.joml.Vector3f;

@Prototype
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class PlayingState implements ApplicationState {

  private final SceneService sceneService;
  private final WorldService worldService;
  private final Camera camera;
  private final WindowService windowService;

  // Systems
  private final PlayerInputSystem playerInputSystem;
  private final MovementSystem movementSystem;
  private final EnemyAISystem enemyAISystem;
  private final CollisionSystem collisionSystem;
  private final RenderSystem renderSystem;
  private final UISystem uiSystem;

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
    // Load the scene file
    sceneService.load("/scenes/playing-scene.json");

    // Register all game-related systems
    worldService.addSystem(playerInputSystem);
    worldService.addSystem(movementSystem);
    worldService.addSystem(enemyAISystem);
    worldService.addSystem(collisionSystem);
    worldService.addSystem(renderSystem);
    worldService.addSystem(uiSystem);
  }

  @Override
  public void onUpdate(float deltaTime) {
    // All logic is handled by the systems, so this can be empty for now
  }

  @Override
  public void onExit() {
    worldService.clearSystems();
  }
}
