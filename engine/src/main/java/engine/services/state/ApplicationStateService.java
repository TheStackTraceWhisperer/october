package engine.services.state;

import engine.IService;
import io.micronaut.context.ApplicationContext;
import jakarta.inject.Named;
import jakarta.inject.Provider;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Stack;

/**
 * A stack-based Finite State Machine (FSM) for managing the game's high-level states.
 */
@Slf4j
@Singleton
@RequiredArgsConstructor
public class ApplicationStateService implements IService {
  private final ApplicationContext applicationContext;

  @Named("initial")
  private final Provider<ApplicationState> initialStateProvider;

  private final Stack<ApplicationState> stateStack = new Stack<>();

  public void start() {
    pushState(initialStateProvider.get());
  }

  public void stop() {
    log.info("Stopping ApplicationStateService. Popping all states.");
    while (!stateStack.isEmpty()) {
      popState();
    }
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

  public ApplicationState peek() {
    if (!stateStack.isEmpty()) {
      return stateStack.peek();
    } else {
      return null;
    }
  }

  /**
   * Pushes a new state onto the stack, making it the active state.
   *
   * @param state The new state to activate.
   */
  public void pushState(ApplicationState state) {
    log.debug("Pushing state: {}", state.getClass().getSimpleName());
    stateStack.push(state);
    state.onEnter();
  }

  /**
   * Pushes a new state onto the stack, making it the active state.
   * A new instance of the state class will be created by the application context.
   *
   * @param stateClass The class of the new state to activate.
   */
  public void pushState(Class<? extends ApplicationState> stateClass) {
    log.debug("Creating and pushing state from class: {}", stateClass.getSimpleName());
    ApplicationState newState = applicationContext.createBean(stateClass);
    pushState(newState);
  }

  /**
   * Removes the current state from the stack, returning to the previous state.
   */
  public void popState() {
    if (!stateStack.isEmpty()) {
      ApplicationState poppedState = stateStack.pop();
      log.debug("Popping state: {}", poppedState.getClass().getSimpleName());
      poppedState.onExit();
    } else {
      log.warn("Attempted to pop state from an empty stack.");
    }
  }

  /**
   * Pops the current state and pushes a new one in a single, atomic operation.
   *
   * @param state The new state to activate.
   */
  public void changeState(ApplicationState state) {
    log.debug("Changing state to: {}", state.getClass().getSimpleName());
    if (!stateStack.isEmpty()) {
      stateStack.pop().onExit();
    }
    stateStack.push(state);
    state.onEnter();
  }

  /**
   * Pops the current state and pushes a new one in a single, atomic operation.
   * A new instance of the state class will be created by the application context.
   *
   * @param stateClass The class of the new state to activate.
   */
  public void changeState(Class<? extends ApplicationState> stateClass) {
    log.debug("Creating and changing state to class: {}", stateClass.getSimpleName());
    ApplicationState newState = applicationContext.createBean(stateClass);
    changeState(newState);
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
