package api.ecs;

public interface IComponentManager {
  <T> void addComponent(int entityId, T component);
  <T> T getComponent(int entityId, Class<T> componentClass);
  void removeComponent(int entityId, Class<?> componentClass);
  boolean hasComponent(int entityId, Class<?> componentClass);
  void entityDestroyed(int entityId);
}
