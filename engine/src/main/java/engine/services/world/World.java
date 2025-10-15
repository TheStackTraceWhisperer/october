package engine.services.world;

import engine.ecs.IWorld;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class World implements IWorld {
  EntityManager entityManager = new EntityManager();
  ComponentManager componentManager = new ComponentManager();

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
  public Set<Integer> getEntitiesWith(Class<?>... componentClasses) {
    // Start with a list of all active entities.
    Set<Integer> activeEntities = entityManager.getActiveEntities();

    if (componentClasses == null || componentClasses.length == 0) {
      return activeEntities;
    }

    // Filter the list down to only those that have all the required components.
    return activeEntities.stream()
      .filter(entityId -> {
        for (Class<?> componentClass : componentClasses) {
          if (!componentManager.hasComponent(entityId, componentClass)) {
            return false; // This entity doesn't have one of the required components.
          }
        }
        return true; // This entity has all required components.
      })
      .collect(Collectors.toSet());
  }
}
