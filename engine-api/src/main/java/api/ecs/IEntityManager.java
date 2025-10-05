package api.ecs;

import java.util.List;

public interface IEntityManager {
  int createEntity();
  void destroyEntity(int entityId);
  List<Integer> getActiveEntities();
}
