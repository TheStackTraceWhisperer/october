package engine.services.world.components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllableComponentTest {

    @Test
    void constructor_shouldInitializeWithDefaultValues() {
        ControllableComponent controllable = new ControllableComponent();

        assertEquals(0, controllable.playerId);
        assertFalse(controllable.wantsToMoveUp);
        assertFalse(controllable.wantsToMoveDown);
        assertFalse(controllable.wantsToMoveLeft);
        assertFalse(controllable.wantsToMoveRight);
        assertFalse(controllable.wantsToAttack);
    }
}
