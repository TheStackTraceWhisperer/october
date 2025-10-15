package engine.ecs;

import java.util.Set;

public interface IEntityManager {
  int createEntity();
  void destroyEntity(int entityId);
  Set<Integer> getEntities();
}
