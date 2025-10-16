package engine.services.world.components;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransformComponentTest {

    private TransformComponent transform;

    @BeforeEach
    void setUp() {
        transform = new TransformComponent();
    }

    @Test
    void constructor_shouldInitializeWithDefaultValues() {
        assertEquals(new Vector3f(0, 0, 0), transform.position);
        assertEquals(new Vector3f(0, 0, 0), transform.previousPosition);
        assertEquals(new Quaternionf().identity(), transform.rotation);
        assertEquals(new Vector3f(1, 1, 1), transform.scale);
    }

    @Test
    void updatePreviousPosition_shouldSetPreviousPositionToCurrentPosition() {
        transform.position.set(1, 2, 3);
        transform.updatePreviousPosition();
        assertEquals(new Vector3f(1, 2, 3), transform.previousPosition);
    }

    @Test
    void revertPosition_shouldSetPositionToPreviousPosition() {
        transform.previousPosition.set(4, 5, 6);
        transform.position.set(1, 2, 3); // Change current position
        transform.revertPosition();
        assertEquals(new Vector3f(4, 5, 6), transform.position);
    }

    @Test
    void getTransformMatrix_shouldReturnCorrectMatrix() {
        transform.position.set(10, 0, 0);
        transform.rotation.fromAxisAngleDeg(0, 1, 0, 90);
        transform.scale.set(2, 2, 2);

        Matrix4f expectedMatrix = new Matrix4f()
                .translate(10, 0, 0)
                .rotate((float) Math.toRadians(90), 0, 1, 0)
                .scale(2, 2, 2);

        assertTrue(expectedMatrix.equals(transform.getTransformMatrix(), 0.001f));
    }
}
