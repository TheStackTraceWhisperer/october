package engine;


public interface IService {
  // Called on start
  default void start() {};
  // Called on stop
  default void stop() {};
  // Called for all services BEFORE update(float dt)
  default void update() {};
  // Called for all services AFTER update();
  default void update(float dt) { };

  // Determines the priority (order) of invocation (priority = weight = heavier = later)
  default int priority() { return 0; };
}
