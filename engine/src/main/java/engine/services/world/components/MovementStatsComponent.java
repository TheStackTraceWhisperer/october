package engine.services.world.components;


import api.ecs.IComponent;

/**
 * A component that stores movement-related stats for an entity.
 */
public record MovementStatsComponent(float speed) implements IComponent {
}
