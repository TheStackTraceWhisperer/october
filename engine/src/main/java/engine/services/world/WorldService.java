package engine.services.world;

import engine.ecs.ISystem;
import engine.IService;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Set;

@Singleton
public class WorldService implements IService {
  private final World world = new World();
  private final SystemManager systemManager = new SystemManager(world);

  @Override
  public void update() {
    systemManager.update();
  }

  @Override
  public void update(float dt) {
    systemManager.update(dt);
  }

  public int createEntity() {
    return world.createEntity();
  }

  public void destroyEntity(int entityId) {
    world.destroyEntity(entityId);
  }

  public <T> void addComponent(int entityId, T component) {
    world.addComponent(entityId, component);
  }

  public <T> T getComponent(int entityId, Class<T> componentClass) {
    return world.getComponent(entityId, componentClass);
  }

  public void removeComponent(int entityId, Class<?> componentClass) {
    world.removeComponent(entityId, componentClass);
  }

  public boolean hasComponent(int entityId, Class<?> componentClass) {
    return world.hasComponent(entityId, componentClass);
  }

  public void addSystem(ISystem system) {
    systemManager.addSystem(system);
  }

  public void removeSystem(ISystem system) {
    systemManager.removeSystem(system);
  }

  public Set<Integer> getEntitiesWith(Class<?>... componentClasses) {
    return world.getEntitiesWith(componentClasses);
  }

  public void clearSystems() {
    systemManager.clear();
  }
}

