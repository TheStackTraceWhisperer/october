package application;

import engine.services.input.InputService;
import engine.services.scene.SceneService;
import engine.services.state.ApplicationState;
import engine.services.state.ApplicationStateService;
import engine.services.world.WorldService;
import engine.services.world.ISystem;
import engine.services.world.systems.UISystem;
import engine.services.world.systems.SequenceSystem;
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
  private final InputService inputService;
  private final BeanProvider<IntroCutsceneState> introCutsceneStateProvider;
  private final BeanProvider<PlayingState> playingStateProvider;
  private final TimerOverlayProvider timerOverlayProvider;

  @Override
  public void onEnter() {
    sceneService.load("/scenes/main_menu.json");

    // Configure idle timeout tracking in SequenceSystem
    SequenceSystem sequenceSystem = worldService.getSystem(SequenceSystem.class);
    sequenceSystem.configureIdleTimeout(IDLE_TIMEOUT);

    // Configure overlay system instance now that WorldService has enabled it
    TimerOverlaySystem overlay = worldService.getSystem(TimerOverlaySystem.class);
    timerOverlayProvider.configureMainMenu(overlay, () -> sequenceSystem.getIdleTimer() / IDLE_TIMEOUT);
  }

  @Override
  public void onResume() {
    // Reset idle tracking and reload the main menu scene (cutscene cleared entities)
    sceneService.load("/scenes/main_menu.json");

    // Reset idle timeout in SequenceSystem
    SequenceSystem sequenceSystem = worldService.getSystem(SequenceSystem.class);
    sequenceSystem.resetIdleTimer();

    // Reconfigure overlay in case it was recreated
    TimerOverlaySystem overlay = worldService.getSystem(TimerOverlaySystem.class);
    timerOverlayProvider.configureMainMenu(overlay, () -> sequenceSystem.getIdleTimer() / IDLE_TIMEOUT);
  }

  @Override
  public void onSuspend() {
    // No manual system management needed; ApplicationStateService handles enable/disable.
  }

  @Override
  public void onExit() {
    // Disable idle timeout when exiting
    SequenceSystem sequenceSystem = worldService.getSystem(SequenceSystem.class);
    sequenceSystem.disableIdleTimeout();
  }

  @Override
  public Collection<Class<? extends ISystem>> systems() {
    return List.of(UISystem.class, TimerOverlaySystem.class, SequenceSystem.class);
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

    // Check if idle timeout has been reached
    SequenceSystem sequenceSystem = worldService.getSystem(SequenceSystem.class);
    if (sequenceSystem.hasIdleTimedOut()) {
      applicationStateService.pushState(introCutsceneStateProvider::get);
    }
  }

  private void handleInput() {
    // Ignore cursor movement; only reset idle on keyboard or mouse button clicks
    if (inputService.isAnyKeyJustPressed() || inputService.isAnyMouseButtonJustPressed()) {
      SequenceSystem sequenceSystem = worldService.getSystem(SequenceSystem.class);
      sequenceSystem.resetIdleTimer();
    }
  }
}
