package application;

import engine.game.GameAction;
import engine.services.state.ApplicationState;
import engine.services.state.ApplicationStateService;
import engine.services.input.InputService;
import engine.services.world.WorldService;
import engine.services.world.systems.TriggerSystem;
import engine.services.world.systems.SequenceSystem;
import engine.services.world.systems.MovementSystem;
import engine.services.zone.ZoneService;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

/**
 * The IntroCutsceneState demonstrates the integration of the Zone, Tilemap, and Sequence components.
 * 
 * According to the specification, the ApplicationState is responsible for:
 * - Enabling necessary services (ZoneService, RenderService, AudioService)
 * - Registering systems (TriggerSystem, SequenceSystem, MovementSystem)
 * - Loading the initial zone which kicks off the trigger and sequence processing
 */
@Singleton
@Slf4j
public class IntroCutsceneState implements ApplicationState {

  private static final float CUTSCENE_DURATION = 10.0f;

  private final ApplicationStateService applicationStateService;
  private final InputService inputService;
  private final WorldService worldService;
  private final ZoneService zoneService;
  private final TriggerSystem triggerSystem;
  private final SequenceSystem sequenceSystem;
  private final MovementSystem movementSystem;
  
  private float cutsceneTimer;

  public IntroCutsceneState(
      ApplicationStateService applicationStateService, 
      InputService inputService,
      WorldService worldService,
      ZoneService zoneService,
      TriggerSystem triggerSystem,
      SequenceSystem sequenceSystem,
      MovementSystem movementSystem) {
    this.applicationStateService = applicationStateService;
    this.inputService = inputService;
    this.worldService = worldService;
    this.zoneService = zoneService;
    this.triggerSystem = triggerSystem;
    this.sequenceSystem = sequenceSystem;
    this.movementSystem = movementSystem;
  }

  @Override
  public void onEnter() {
    log.debug("Entering IntroCutsceneState");
    this.cutsceneTimer = 0.0f;
    
    // Register the necessary systems for cutscene processing
    // The TriggerSystem will listen for ZoneLoadedEvent and process triggers
    // The SequenceSystem will interpret ActiveSequence components and execute GameEvents
    worldService.addSystem(triggerSystem);
    worldService.addSystem(sequenceSystem);
    worldService.addSystem(movementSystem);
    
    // Load the intro cutscene zone
    // This will emit a ZoneLoadedEvent which the TriggerSystem will process
    // Note: The actual zone loading functionality needs to be implemented in ZoneService
    // zoneService.loadZone("intro_cutscene_zone");
    
    log.debug("IntroCutsceneState systems registered and zone loading initiated");
  }

  @Override
  public void onExit() {
    log.debug("Exiting IntroCutsceneState");
    
    // Clean up systems when exiting the state
    worldService.clearSystems();
  }

  @Override
  public void onUpdate(float deltaTime) {
    handleInput();
    update(deltaTime);
    render();
  }

  public void update(float deltaTime) {
    this.cutsceneTimer += deltaTime;
    if (this.cutsceneTimer >= CUTSCENE_DURATION) {
      applicationStateService.popState();
    }
  }

  public void handleInput() {
    // Implement skip functionality - any input will skip the cutscene
    for (GameAction action : GameAction.values()) {
      if (inputService.isActionJustPressed(action)) {
        applicationStateService.popState();
        return;
      }
    }
  }

  public void render() {
    // Clear screen to black to indicate state change
    glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT);
  }
}
