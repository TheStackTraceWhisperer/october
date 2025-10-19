package application;

import engine.game.GameAction;
import engine.services.state.ApplicationState;
import engine.services.state.ApplicationStateService;
import engine.services.input.InputService;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

@Singleton
@Slf4j
public class IntroCutsceneState implements ApplicationState {

  private static final float CUTSCENE_DURATION = 10.0f;

  private final ApplicationStateService applicationStateService;
  private final InputService inputService;
  private float cutsceneTimer;

  public IntroCutsceneState(ApplicationStateService applicationStateService, InputService inputService) {
    this.applicationStateService = applicationStateService;
    this.inputService = inputService;
  }

  @Override
  public void onEnter() {
    log.debug("Entering IntroCutsceneState");
    this.cutsceneTimer = 0.0f;
  }

  @Override
  public void onExit() {
    log.debug("Exiting IntroCutsceneState");
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
