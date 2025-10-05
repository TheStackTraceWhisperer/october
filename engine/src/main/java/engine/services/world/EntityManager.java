package engine.services.world;

import api.ecs.IEntityManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class EntityManager implements IEntityManager {
  private int nextEntityId = 0;
  private final Set<Integer> activeEntities = new HashSet<>();

  public int createEntity() {
    int id = nextEntityId++;
    activeEntities.add(id);
    return id;
  }

  public void destroyEntity(int entityId) {
    activeEntities.remove(entityId);
  }

  public List<Integer> getActiveEntities() {
    return new ArrayList<>(activeEntities);
  }
}
