package engine.services.state;

import engine.IApplication;
import io.micronaut.context.ApplicationContext;
import jakarta.inject.Named;
import jakarta.inject.Qualifier;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.Stack;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A stack-based Finite State Machine (FSM) for managing the game's high-level states.
 */
@Slf4j
@Singleton
@RequiredArgsConstructor
public class ApplicationStateService {
  @Named("initial")
  private final Supplier<GameState> initialSupplier;

  private final Stack<GameState> stateStack = new Stack<>();

  public void start(ApplicationContext context) {
    pushState(initialSupplier.get());
  }

  public void stop() {

  }

  /**
   * Updates the current active state.
   *
   * @param deltaTime The time elapsed since the last frame.
   */
  public void update(float deltaTime) {
    if (!stateStack.isEmpty()) {
      stateStack.peek().onUpdate(deltaTime);
    }
  }

  /**
   * Pushes a new state onto the stack, making it the active state.
   *
   * @param state    The new state to activate.
   */
  public void pushState(GameState state) {
    stateStack.push(state);
    state.onEnter();
  }

  /**
   * Removes the current state from the stack, returning to the previous state.
   */
  public void popState() {
    if (!stateStack.isEmpty()) {
      stateStack.pop().onExit();
    }
  }

  /**
   * Pops the current state and pushes a new one in a single, atomic operation.
   *
   * @param state    The new state to activate.
   */
  public void changeState(GameState state) {
    if (!stateStack.isEmpty()) {
      stateStack.pop().onExit();
    }
    stateStack.push(state);
    state.onEnter();
  }

  /**
   * Checks if the state manager is empty.
   *
   * @return true if no states are active.
   */
  public boolean isEmpty() {
    return stateStack.isEmpty();
  }
}
