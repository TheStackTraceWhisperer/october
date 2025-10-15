package engine.ecs;

import java.util.Set;

public interface IWorld {
  int createEntity();
  void destroyEntity(int entityId);
  <T> void addComponent(int entityId, T component);
  <T> T getComponent(int entityId, Class<T> componentClass);
  void removeComponent(int entityId, Class<?> componentClass);
  boolean hasComponent(int entityId, Class<?> componentClass);
  Set<Integer> getEntitiesWith(Class<?>... componentClasses);
}
