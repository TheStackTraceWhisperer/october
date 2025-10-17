package engine;

import engine.services.world.IComponent;
import engine.services.world.components.*;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Factory
public class EngineConfiguration {

    @Singleton
    @Named("componentRegistry")
    public Map<String, Class<? extends IComponent>> componentRegistry() {
        // A map of component simple names to their class types
        return Stream.of(
                AudioSourceComponent.class,
                ColliderComponent.class,
                ControllableComponent.class,
                EnemyComponent.class,
                HealthComponent.class,
                MeshComponent.class,
                MovementStatsComponent.class,
                MusicComponent.class,
                SoundEffectComponent.class,
                SpriteComponent.class,
                TransformComponent.class,
                UIButtonComponent.class,
                UIImageComponent.class,
                UITransformComponent.class
        ).collect(Collectors.toMap(Class::getSimpleName, Function.identity()));
    }
}
