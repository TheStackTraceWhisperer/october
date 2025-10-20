package application;

import engine.game.GameAction;
import engine.services.input.InputService;
import engine.services.scene.SceneService;
import engine.services.state.ApplicationState;
import engine.services.state.ApplicationStateService;
import engine.services.world.WorldService;
import engine.services.world.systems.UISystem;
import io.micronaut.context.BeanProvider;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import org.joml.Vector2d;

@Prototype
@Named("initial")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class MainMenuState implements ApplicationState {

  private static final float IDLE_TIMEOUT = 30.0f;

  private final SceneService sceneService;
  private final WorldService worldService;
  private final ApplicationStateService applicationStateService;
  private final UISystem uiSystem;
  private final InputService inputService;
  private final BeanProvider<IntroCutsceneState> introCutsceneStateProvider;

  private float idleTimer;
  private final Vector2d lastCursorPos = new Vector2d(-1, -1);

  @Override
  public void onEnter() {
    resetIdleTimer();
    sceneService.load("/scenes/main_menu.json");
    worldService.addSystem(uiSystem);
  }

  @EventListener
  public void onStartGame(String event) {
    if ("START_NEW_GAME".equals(event)) {
      applicationStateService.changeState(PlayingState.class);
    }
  }

  @Override
  public void onUpdate(float deltaTime) {
    handleInput();
    
    // The UISystem is now handling updates and rendering
    
    this.idleTimer += deltaTime;
    if (this.idleTimer >= IDLE_TIMEOUT) {
      // Idle timeout reached, push the cutscene state onto the stack.
      applicationStateService.pushState(introCutsceneStateProvider.get());
      // The timer will be reset in onEnter() when this state becomes active again.
    }
  }

  @Override
  public void onExit() {
    worldService.clearSystems();
  }

  private void resetIdleTimer() {
    this.idleTimer = 0.0f;
  }

  private void handleInput() {
    // Check for mouse movement first
    Vector2d currentCursorPos = inputService.getCursorPos();
    if (lastCursorPos.x != currentCursorPos.x || lastCursorPos.y != currentCursorPos.y) {
      resetIdleTimer();
      this.lastCursorPos.set(currentCursorPos);
    }

    // Check for any key or button press
    for (GameAction action : GameAction.values()) {
      if (inputService.isActionJustPressed(action)) {
        resetIdleTimer();
        // Do not return here, allow the rest of the logic to process
      }
    }
  }
}