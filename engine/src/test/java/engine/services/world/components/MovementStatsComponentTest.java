package engine.services.world.components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovementStatsComponentTest {

    @Test
    void constructor_shouldSetSpeed() {
        MovementStatsComponent stats = new MovementStatsComponent(5.0f);
        assertEquals(5.0f, stats.speed());
    }
}
