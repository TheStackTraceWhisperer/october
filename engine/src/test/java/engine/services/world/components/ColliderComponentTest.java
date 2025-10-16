package engine.services.world.components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColliderComponentTest {

    // A mock implementation of the ColliderType for testing purposes
    private enum TestColliderType implements ColliderComponent.ColliderType {
        PLAYER, ENEMY
    }

    @Test
    void constructor_shouldSetAllFields() {
        ColliderComponent.ColliderType type = TestColliderType.PLAYER;
        int width = 32;
        int height = 32;
        int offsetX = 0;
        int offsetY = 0;

        ColliderComponent collider = new ColliderComponent(type, width, height, offsetX, offsetY);

        assertEquals(type, collider.getType());
        assertEquals(width, collider.getWidth());
        assertEquals(height, collider.getHeight());
        assertEquals(offsetX, collider.getOffsetX());
        assertEquals(offsetY, collider.getOffsetY());
    }
}
