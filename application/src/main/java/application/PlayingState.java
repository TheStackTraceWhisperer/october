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
  private final AudioSystem audioSystem; // Inject and register audio system
  private final TriggerSystem triggerSystem;
  private final SequenceSystem sequenceSystem;
  private final MoveToTargetSystem moveToTargetSystem;
  private final RenderSystem renderSystem;
  private final UISystem uiSystem;
  private final FadeOverlaySystem fadeOverlaySystem;

  @Override
  public void onEnter() {
    // --- Set up the game world ---
    initializeSceneAndSystems();

    // --- Configure the main game camera ---
    camera.setPosition(new Vector3f(0.0f, 0.0f, 5.0f));
    camera.resize(windowService.getWidth(), windowService.getHeight());
    windowService.setResizeListener(camera::resize);
  }

  private void initializeSceneAndSystems() {
    // Load the scene file
    sceneService.load("/scenes/playing-scene.json");

    // Register all game-related systems (ordering managed by SystemManager priority)
    worldService.addSystem(playerInputSystem);
    worldService.addSystem(movementSystem);
    worldService.addSystem(enemyAISystem);
    worldService.addSystem(collisionSystem);
    worldService.addSystem(triggerSystem);
    worldService.addSystem(sequenceSystem);
    worldService.addSystem(moveToTargetSystem);
    worldService.addSystem(audioSystem);
    worldService.addSystem(renderSystem);
    worldService.addSystem(uiSystem);
    worldService.addSystem(fadeOverlaySystem);
  }

  @Override
  public void onUpdate(float deltaTime) {
    // All logic is handled by the systems
  }

  @Override
  public void onExit() {
    worldService.clearSystems();
  }
}
