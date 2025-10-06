package api.ecs;

public interface ISystem {
  default int priority() { return 0; };
  default void update(IWorld world) {};
  default void update(IWorld world, float dt) {};
  // TODO: Lifecycle
}
