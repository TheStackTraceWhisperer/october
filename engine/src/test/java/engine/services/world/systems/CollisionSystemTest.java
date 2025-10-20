package engine.services.world.systems;

import engine.game.GameColliderType;
import engine.services.world.World;
import engine.services.world.components.ColliderComponent;
import engine.services.world.components.TransformComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollisionSystemTest {

    @Mock
    private World world;

    @InjectMocks
    private CollisionSystem collisionSystem;

    private TransformComponent playerTransform;
    private TransformComponent wallTransform;

    private final int playerEntity = 1;
    private final int wallEntity = 2;

    @BeforeEach
    void setUp() {
        // Use spy for transforms to track method calls on real objects
        playerTransform = spy(new TransformComponent());
        ColliderComponent playerCollider = new ColliderComponent(GameColliderType.PLAYER, 1, 1, 0, 0);

        wallTransform = new TransformComponent();
        ColliderComponent wallCollider = new ColliderComponent(GameColliderType.WALL, 1, 1, 0, 0);

        // Configure mock world to return our two entities
        when(world.getEntitiesWith(TransformComponent.class, ColliderComponent.class))
                .thenReturn(Set.of(playerEntity, wallEntity));

        when(world.getComponent(playerEntity, TransformComponent.class)).thenReturn(playerTransform);
        when(world.getComponent(playerEntity, ColliderComponent.class)).thenReturn(playerCollider);

        when(world.getComponent(wallEntity, TransformComponent.class)).thenReturn(wallTransform);
        when(world.getComponent(wallEntity, ColliderComponent.class)).thenReturn(wallCollider);
    }

    @Test
    void testCollision_revertsPositionWhenOverlapping() {
        // Given: The player and wall are positioned to overlap
        playerTransform.position.set(0.5f, 0, 0);
        wallTransform.position.set(1.0f, 0, 0);

        // When: The collision system updates
        collisionSystem.update(world, 0.1f);

        // Then: The player's revertPosition method should have been called
        verify(playerTransform, times(1)).revertPosition();
    }

    @Test
    void testCollision_doesNothingWhenNotOverlapping() {
        // Given: The player and wall are positioned far apart
        playerTransform.position.set(-5.0f, 0, 0);
        wallTransform.position.set(5.0f, 0, 0);

        // When: The collision system updates
        collisionSystem.update(world, 0.1f);

        // Then: The player's revertPosition method should NOT have been called
        verify(playerTransform, never()).revertPosition();
    }
}
