package api.ecs;

public interface ISystem {
  int priority();
  void update(IWorld world);
  void update(IWorld world, float dt);
  // TODO: Lifecycle
}
