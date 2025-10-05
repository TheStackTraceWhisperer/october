package engine.services.world;

import api.ecs.IWorld;

import java.util.List;

public class World implements IWorld {
  EntityManager entityManager;
  ComponentManager componentManager;

  @Override
  public int createEntity() {
    return entityManager.createEntity();
  }

  @Override
  public void destroyEntity(int entityId) {
    componentManager.entityDestroyed(entityId);
    entityManager.destroyEntity(entityId);
  }

  @Override
  public <T> void addComponent(int entityId, T component) {
    componentManager.addComponent(entityId, component);
  }

  @Override
  public <T> T getComponent(int entityId, Class<T> componentClass) {
    return componentManager.getComponent(entityId, componentClass);
  }

  @Override
  public void removeComponent(int entityId, Class<?> componentClass) {
    componentManager.removeComponent(entityId, componentClass);
  }

  @Override
  public boolean hasComponent(int entityId, Class<?> componentClass) {
    return componentManager.hasComponent(entityId, componentClass);
  }

  @Override
  public List<Integer> getEntitiesWith(Class<?>... componentClasses) {
    return List.of();
  }
}
