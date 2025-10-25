package application;

import engine.services.rendering.CameraService;
import engine.services.scene.SceneService;
import engine.services.state.ApplicationState;
import engine.services.window.WindowService;
import engine.services.world.WorldService;
import engine.services.world.ISystem;
import engine.services.world.systems.*;
import io.micronaut.context.annotation.Prototype;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;
import org.joml.Vector3f;

import java.util.Collection;
import java.util.List;

@Prototype
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class PlayingState implements ApplicationState {

  private final SceneService sceneService;
  private final WorldService worldService;
  private final CameraService cameraService;
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

  private List<ISystem> systems;

  @Override
  public void onEnter() {
    // --- Set up the game world ---
    initializeSceneAndSystems();

    // --- Configure the main game camera ---
    cameraService.setPosition(new Vector3f(0.0f, 0.0f, 5.0f));
    cameraService.resize(windowService.getWidth(), windowService.getHeight());
    windowService.setResizeListener(cameraService::resize);
  }

  private void initializeSceneAndSystems() {
    // Load the scene file
    sceneService.load("/scenes/playing-scene.json");

    // Register all game-related systems for this state via systems() contract
    this.systems = List.of(
      playerInputSystem,
      movementSystem,
      enemyAISystem,
      collisionSystem,
      triggerSystem,
      sequenceSystem,
      moveToTargetSystem,
      audioSystem,
      renderSystem,
      uiSystem,
      fadeOverlaySystem
    );
  }

  @Override
  public void onUpdate(float deltaTime) {
    // All logic is handled by the systems
  }

  @Override
  public void onExit() {
    // No manual clearing; ApplicationStateService will detach systems
    this.systems = null;
  }

  @Override
  public Collection<ISystem> systems() {
    return systems != null ? systems : List.of();
  }
}
