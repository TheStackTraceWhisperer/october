package application;

import engine.services.input.InputService;
import engine.services.scene.SceneService;
import engine.services.state.ApplicationState;
import engine.services.state.ApplicationStateService;
import engine.services.world.WorldService;
import engine.services.world.ISystem;
import engine.services.world.systems.UISystem;
import io.micronaut.context.BeanProvider;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import application.ui.TimerOverlaySystem;
import application.ui.TimerOverlayProvider;

import java.util.Collection;
import java.util.List;

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
  private final BeanProvider<PlayingState> playingStateProvider;
  private final TimerOverlayProvider timerOverlayProvider;

  private TimerOverlaySystem timerOverlaySystem;
  private List<ISystem> systems;

  private float idleTimer;

  @Override
  public void onEnter() {
    resetIdleTimer();
    sceneService.load("/scenes/main_menu.json");

    // Build timer overlay via provider and capture systems list for this state
    this.timerOverlaySystem = timerOverlayProvider.mainMenu(() -> idleTimer / IDLE_TIMEOUT);
    this.systems = List.of(uiSystem, timerOverlaySystem);
  }

  @Override
  public void onResume() {
    // Reset idle tracking and reload the main menu scene (cutscene cleared entities)
    resetIdleTimer();
    sceneService.load("/scenes/main_menu.json");
  }

  @Override
  public void onSuspend() {
    // No manual system management needed; ApplicationStateService handles attach/detach.
  }

  @Override
  public void onExit() {
    // No manual system clearing; managed by ApplicationStateService
    this.timerOverlaySystem = null;
    this.systems = null;
  }

  @Override
  public Collection<ISystem> systems() {
    return systems != null ? systems : List.of();
  }

  @EventListener
  public void onStartGame(String event) {
    if ("START_NEW_GAME".equals(event)) {
      applicationStateService.changeState(playingStateProvider::get);
    }
  }

  @Override
  public void onUpdate(float deltaTime) {
    handleInput();
    
    this.idleTimer += deltaTime;
    if (this.idleTimer >= IDLE_TIMEOUT) {
      applicationStateService.pushState(introCutsceneStateProvider::get);
    }
  }

  private void resetIdleTimer() {
    this.idleTimer = 0.0f;
  }

  private void handleInput() {
    // Ignore cursor movement; only reset idle on keyboard or mouse button clicks
    if (inputService.isAnyKeyJustPressed() || inputService.isAnyMouseButtonJustPressed()) {
      resetIdleTimer();
    }
  }
}