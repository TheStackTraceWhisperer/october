package engine.ecs;

public interface IComponentManager {
  <T> void addComponent(int entityId, T component);
}
