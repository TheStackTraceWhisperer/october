package engine.services.world;

import api.ecs.ISystem;

import api.ecs.IWorld;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
class SystemManager {
  private final IWorld world;

  private final List<ISystem> systems = new ArrayList<>();
  private boolean sorted = true;

  public void addSystem(ISystem system) {
    systems.add(system);
    sorted = false;
  }

  public void removeSystem(ISystem system) {
    systems.remove(system);
  }

  public void update() {
    if(!sorted) {
      systems.sort(Comparator.comparingInt(ISystem::priority));
      sorted = true;
    }

    for (ISystem system : systems) {
      system.update(world);
    }
  }

  public void update(float dt) {
    if(!sorted) {
      systems.sort(Comparator.comparingInt(ISystem::priority));
      sorted = true;
    }

    for (ISystem system : systems) {
      system.update(world, dt);
    }
  }
}
