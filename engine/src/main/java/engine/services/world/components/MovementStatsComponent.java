package engine.services.world.components;


import engine.services.world.IComponent;

/**
 * A component that stores movement-related stats for an entity.
 */
public record MovementStatsComponent(float speed) implements IComponent {
}
