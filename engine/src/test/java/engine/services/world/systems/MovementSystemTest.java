package engine.services.world.systems;

import engine.services.world.World;
import engine.services.world.components.ControllableComponent;
import engine.services.world.components.MovementStatsComponent;
import engine.services.world.components.TransformComponent;
import org.joml.Vector3f;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovementSystemTest {

    @Mock
    private World world;

    private MovementSystem movementSystem;

    @BeforeEach
    void setUp() {
        movementSystem = new MovementSystem();
    }

    @Test
    void testMovementSystem_updatesPositionCorrectly() {
        // Given: An entity with all required movement components
        int entityId = 1;
        ControllableComponent control = new ControllableComponent();
        TransformComponent transform = new TransformComponent();
        MovementStatsComponent stats = new MovementStatsComponent(100.0f); // speed = 100

        // And: The entity wants to move up and right
        control.wantsToMoveUp = true;
        control.wantsToMoveRight = true;

        // And: The mock world is configured to return this entity
        when(world.getEntitiesWith(ControllableComponent.class, TransformComponent.class, MovementStatsComponent.class))
                .thenReturn(Set.of(entityId));
        when(world.getComponent(entityId, ControllableComponent.class)).thenReturn(control);
        when(world.getComponent(entityId, TransformComponent.class)).thenReturn(transform);
        when(world.getComponent(entityId, MovementStatsComponent.class)).thenReturn(stats);

        // When: The movement system updates for 0.1 seconds
        float deltaTime = 0.1f;
        movementSystem.update(world, deltaTime);

        // Then: The position should be updated correctly for diagonal movement
        // Expected movement = normalize(1, 1, 0) * speed * deltaTime
        // = (0.707, 0.707, 0) * 100 * 0.1 = (7.07, 7.07, 0)
        float expected = (float) (Math.sqrt(0.5) * 100.0f * deltaTime);
        assertEquals(expected, transform.position.x, 0.001f, "X position should be updated correctly for diagonal movement.");
        assertEquals(expected, transform.position.y, 0.001f, "Y position should be updated correctly for diagonal movement.");
        assertEquals(0, transform.position.z, "Z position should not change.");
    }

    @Test
    void testMovementSystem_noMovementWhenNotControlled() {
        // Given: An entity that does not want to move
        int entityId = 1;
        ControllableComponent control = new ControllableComponent(); // All flags false
        TransformComponent transform = new TransformComponent();
        MovementStatsComponent stats = new MovementStatsComponent(100.0f);

        when(world.getEntitiesWith(ControllableComponent.class, TransformComponent.class, MovementStatsComponent.class))
                .thenReturn(Set.of(entityId));
        when(world.getComponent(entityId, ControllableComponent.class)).thenReturn(control);
        when(world.getComponent(entityId, TransformComponent.class)).thenReturn(transform);
        when(world.getComponent(entityId, MovementStatsComponent.class)).thenReturn(stats);

        // When: The movement system updates
        movementSystem.update(world, 0.1f);

        // Then: The position should remain at the origin
        assertEquals(new Vector3f(0, 0, 0), transform.position, "Position should not change when there is no input.");
    }
}
