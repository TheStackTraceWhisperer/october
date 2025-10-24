package engine.services.state;

/**
 * Represents a distinct state of the application, such as a main menu, playing, or paused.
 * Each state is responsible for its own logic and rendering.
 */
public interface ApplicationState {
  /**
   * Called once when the state becomes the active state.
   * Use this to set up systems, load scene data, and initialize the state.
   *
   */
  void onEnter();

  /**
   * Called once per frame while this is the active state.
   *
   * @param deltaTime The time elapsed since the last frame.
   */
  void onUpdate(float deltaTime);

  /**
   * Called when the state is about to be exited.
   * Use this for cleanup before transitioning to a new state.
   */
  void onExit();

  /**
   * Called when the state is uncovered and becomes active again after another
   * state above it has been popped. Default no-op.
   */
  default void onResume() { }

  /**
   * Called when the state is being covered by another pushed state. Default no-op.
   */
  default void onSuspend() { }
}
