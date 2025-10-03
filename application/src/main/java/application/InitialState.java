package application;

import engine.services.state.GameState;
import io.micronaut.context.annotation.Prototype;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Prototype
@Named("initial")
public class InitialState implements GameState {

  @Override
  public void onEnter() {
    log.info("Entering initial state...");
  }

  @Override
  public void onUpdate(float deltaTime) {

  }

  @Override
  public void onExit() {
    log.info("Exiting initial state...");
  }
}
