package engine.services.world.components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HealthComponentTest {

    @Test
    void constructor_shouldSetMaxHealthAndCurrentHealth() {
        HealthComponent health = new HealthComponent(100);
        assertEquals(100, health.getMaxHealth());
        assertEquals(100, health.getCurrentHealth());
    }

    @Test
    void constructor_shouldThrowExceptionForNonPositiveMaxHealth() {
        assertThrows(IllegalArgumentException.class, () -> new HealthComponent(0));
        assertThrows(IllegalArgumentException.class, () -> new HealthComponent(-10));
    }

    @Test
    void takeDamage_shouldReduceCurrentHealth() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(20);
        assertEquals(80, health.getCurrentHealth());
    }

    @Test
    void takeDamage_shouldNotReduceHealthBelowZero() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(120);
        assertEquals(0, health.getCurrentHealth());
    }

    @Test
    void takeDamage_shouldNotChangeHealthForNegativeAmount() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(-20);
        assertEquals(100, health.getCurrentHealth());
    }

    @Test
    void isAlive_shouldReturnTrueWhenHealthIsPositive() {
        HealthComponent health = new HealthComponent(100);
        assertTrue(health.isAlive());
        health.takeDamage(99);
        assertTrue(health.isAlive());
    }

    @Test
    void isAlive_shouldReturnFalseWhenHealthIsZero() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(100);
        assertFalse(health.isAlive());
    }

    @Test
    void isAlive_shouldReturnFalseWhenHealthIsBelowZero() {
        HealthComponent health = new HealthComponent(100);
        health.takeDamage(120);
        assertFalse(health.isAlive());
    }
}
