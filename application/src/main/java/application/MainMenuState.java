package application;

import engine.services.input.InputService;
import engine.services.scene.SceneService;
import engine.services.state.ApplicationState;
import engine.services.state.ApplicationStateService;
import engine.services.world.WorldService;
import engine.services.world.ISystem;
import engine.services.world.systems.UISystem;
import engine.services.world.systems.SequenceSystem;
import engine.services.world.components.ActiveSequenceComponent;
import engine.services.zone.ZoneService;
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
  private final ZoneService zoneService;

  private int idleTimeoutEntityId = -1;

  @Override
  public void onEnter() {
    sceneService.load("/scenes/main_menu.json");
    zoneService.loadZone("main_menu_zone");

    // Create idle timeout entity with sequence
    resetIdleTimer();

    // Configure overlay system instance now that WorldService has enabled it
    TimerOverlaySystem overlay = worldService.getSystem(TimerOverlaySystem.class);
    timerOverlayProvider.configureMainMenu(overlay, () -> getIdleProgress());
  }

  @Override
  public void onResume() {
    // Reset idle tracking and reload the main menu scene (cutscene cleared entities)
    sceneService.load("/scenes/main_menu.json");
    zoneService.loadZone("main_menu_zone");

    resetIdleTimer();

    // Reconfigure overlay in case it was recreated
    TimerOverlaySystem overlay = worldService.getSystem(TimerOverlaySystem.class);
    timerOverlayProvider.configureMainMenu(overlay, () -> getIdleProgress());
  }

  @Override
  public void onSuspend() {
    // No manual system management needed; ApplicationStateService handles enable/disable.
  }

  @Override
  public void onExit() {
    // Cancel idle timeout entity
    if (idleTimeoutEntityId >= 0 && worldService.hasComponent(idleTimeoutEntityId, ActiveSequenceComponent.class)) {
      worldService.destroyEntity(idleTimeoutEntityId);
      idleTimeoutEntityId = -1;
    }
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

    // Check if idle timeout entity still exists (sequence not complete yet)
    if (idleTimeoutEntityId >= 0 && !worldService.hasComponent(idleTimeoutEntityId, ActiveSequenceComponent.class)) {
      // Timeout reached - sequence completed and entity was destroyed
      applicationStateService.pushState(introCutsceneStateProvider::get);
      idleTimeoutEntityId = -1; // Reset for next time
    }
  }

  private void resetIdleTimer() {
    // Destroy existing timeout entity if any
    if (idleTimeoutEntityId >= 0 && worldService.hasComponent(idleTimeoutEntityId, ActiveSequenceComponent.class)) {
      worldService.destroyEntity(idleTimeoutEntityId);
    }

    // Create new idle timeout entity with sequence
    idleTimeoutEntityId = worldService.createEntity();
    worldService.addComponent(idleTimeoutEntityId, new ActiveSequenceComponent("idle_timeout"));
  }

  private float getIdleProgress() {
    if (idleTimeoutEntityId < 0 || !worldService.hasComponent(idleTimeoutEntityId, ActiveSequenceComponent.class)) {
      return 1.0f; // Timeout complete
    }

    ActiveSequenceComponent component = worldService.getComponent(idleTimeoutEntityId, ActiveSequenceComponent.class);
    if (component == null) {
      return 1.0f;
    }

    // Calculate progress: elapsed time / total timeout
    // The WAIT event sets waitTimer to the duration, then counts down
    // So elapsed = IDLE_TIMEOUT - waitTimer
    float elapsed = IDLE_TIMEOUT - component.getWaitTimer();
    return elapsed / IDLE_TIMEOUT;
  }

  private void handleInput() {
    // Ignore cursor movement; only reset idle on keyboard or mouse button clicks
    if (inputService.isAnyKeyJustPressed() || inputService.isAnyMouseButtonJustPressed()) {
      resetIdleTimer();
    }
  }
}
