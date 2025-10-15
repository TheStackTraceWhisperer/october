package engine.ecs;

public interface ISystem {
  default void update(IWorld world) { }
  default void update(IWorld world, float deltaTime) { }
  default int priority() {
    return 0;
  }
}
