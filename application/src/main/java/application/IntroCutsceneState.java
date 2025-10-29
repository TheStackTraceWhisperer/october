package application;

import engine.services.state.ApplicationState;
import engine.services.state.ApplicationStateService;
import engine.services.input.InputService;
import engine.services.scene.SceneService;
import engine.services.world.WorldService;
import engine.services.world.ISystem;
import engine.services.world.systems.TriggerSystem;
import engine.services.world.systems.SequenceSystem;
import engine.services.world.systems.MovementSystem;
import engine.services.world.systems.AudioSystem;
import engine.services.world.systems.MoveToTargetSystem;
import engine.services.world.systems.FadeOverlaySystem;
import engine.services.world.systems.RenderSystem;
import engine.services.world.systems.UISystem;
import engine.services.world.components.ActiveSequenceComponent;
import engine.services.zone.ZoneService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import application.ui.TimerOverlaySystem;
import application.ui.TimerOverlayProvider;

import java.util.Collection;
import java.util.List;

/** Intro cutscene that ties together zone, tilemap, and sequences. */
@Singleton
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class IntroCutsceneState implements ApplicationState {

  private static final float CUTSCENE_DURATION = 10.0f;

  private final ApplicationStateService applicationStateService;
  private final InputService inputService;
  private final WorldService worldService;
  private final SceneService sceneService;
  private final ZoneService zoneService;
  private final TimerOverlayProvider timerOverlayProvider;

  private int cutsceneTimeoutEntityId = -1;

  @Override
  public void onEnter() {
    log.debug("Entering IntroCutsceneState");

    // Clear any existing entities (e.g., main menu UI) so the cutscene fully takes over
    sceneService.load("/scenes/cutscene-blank.json");

    // Load the intro cutscene zone (publishes ZoneLoadedEvent)
    zoneService.loadZone("intro_cutscene_zone");

    // Create cutscene timeout entity with sequence
    cutsceneTimeoutEntityId = worldService.createEntity();
    worldService.addComponent(cutsceneTimeoutEntityId, new ActiveSequenceComponent("cutscene_timeout"));

    // Configure progress overlay; WorldService will enable the system using classes
    TimerOverlaySystem overlay = worldService.getSystem(TimerOverlaySystem.class);
    timerOverlayProvider.configureIntroCutscene(overlay, () -> getCutsceneProgress());

    log.debug("IntroCutsceneState systems registered and zone loading initiated");
  }

  @Override
  public void onExit() {
    log.debug("Exiting IntroCutsceneState");

    // Cancel cutscene timeout entity if still active
    if (cutsceneTimeoutEntityId >= 0 && worldService.hasComponent(cutsceneTimeoutEntityId, ActiveSequenceComponent.class)) {
      worldService.destroyEntity(cutsceneTimeoutEntityId);
      cutsceneTimeoutEntityId = -1;
    }
  }

  @Override
  public Collection<Class<? extends ISystem>> systems() {
    return List.of(
      TriggerSystem.class,
      SequenceSystem.class,
      MovementSystem.class,
      MoveToTargetSystem.class,
      AudioSystem.class,
      RenderSystem.class,
      UISystem.class,
      FadeOverlaySystem.class,
      TimerOverlaySystem.class
    );
  }

  @Override
  public void onUpdate(float deltaTime) {
    handleInput();
    update(deltaTime);
  }

  public void update(float deltaTime) {
    // Check if cutscene timeout entity still exists (sequence not complete yet)
    if (cutsceneTimeoutEntityId >= 0 && !worldService.hasComponent(cutsceneTimeoutEntityId, ActiveSequenceComponent.class)) {
      // Timeout reached - sequence completed and entity was destroyed
      applicationStateService.popState();
      cutsceneTimeoutEntityId = -1; // Reset for next time
    }
  }

  public void handleInput() {
    // Skip on any keyboard key, mouse button, or gamepad button just-pressed; ignore cursor movement
    if (inputService.isAnyKeyJustPressed() || inputService.isAnyMouseButtonJustPressed() || inputService.isAnyGamepadButtonJustPressed()) {
      applicationStateService.popState();
    }
  }

  private float getCutsceneProgress() {
    if (cutsceneTimeoutEntityId < 0 || !worldService.hasComponent(cutsceneTimeoutEntityId, ActiveSequenceComponent.class)) {
      return 1.0f; // Timeout complete
    }

    ActiveSequenceComponent component = worldService.getComponent(cutsceneTimeoutEntityId, ActiveSequenceComponent.class);
    if (component == null) {
      return 1.0f;
    }

    // Calculate progress: elapsed time / total duration
    // The WAIT event sets waitTimer to the duration, then counts down
    // So elapsed = CUTSCENE_DURATION - waitTimer
    float elapsed = CUTSCENE_DURATION - component.getWaitTimer();
    return elapsed / CUTSCENE_DURATION;
  }
}
