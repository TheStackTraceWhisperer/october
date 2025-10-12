package engine.services.world;

import engine.EngineTestHarness;
import engine.services.world.components.ControllableComponent;
import engine.services.world.components.MovementStatsComponent;
import engine.services.world.components.TransformComponent;
import engine.services.world.systems.MovementSystem;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

class WorldAndMovementSystemIT extends EngineTestHarness {

  @Inject
  WorldService world;

  @Test
  void movementSystemMovesEntityBasedOnControlAndSpeed() {
    // Arrange
    int entity = world.createEntity();
    TransformComponent transform = new TransformComponent();
    ControllableComponent control = new ControllableComponent();
    MovementStatsComponent stats = new MovementStatsComponent(5.0f);

    world.addComponent(entity, transform);
    world.addComponent(entity, control);
    world.addComponent(entity, stats);

    world.clearSystems();
    world.addSystem(new MovementSystem());

    // Act: move right for 0.5 seconds
    control.wantsToMoveRight = true;
    world.update(0.5f);

    // Assert: x should have increased by speed * dt = 2.5
    assertThat(transform.position.x).isCloseTo(2.5f, offset(0.0001f));
  }
}
