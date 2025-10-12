package engine.services.scene;

import api.ecs.IComponent;
import engine.EngineTestHarness;
import engine.services.resources.AssetCacheService;
import engine.services.world.WorldService;
import engine.services.world.components.SpriteComponent;
import engine.services.world.components.TransformComponent;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SceneServiceIT extends EngineTestHarness {

  @Inject SceneService scenes;
  @Inject WorldService world;
  @Inject AssetCacheService cache;

  @Test
  void loadScene_createsEntities_andLoadsAssets() {
    // Arrange: initialize component registry
    Map<String, Class<? extends IComponent>> registry = new HashMap<>();
    registry.put("TransformComponent", TransformComponent.class);
    registry.put("SpriteComponent", SpriteComponent.class);
    scenes.initialize(registry);

    // Act
    scenes.load("/scenes/simple.json");

    // Assert: exactly one entity with Transform+Sprite
    List<Integer> entities = world.getEntitiesWith(TransformComponent.class, SpriteComponent.class);
    assertThat(entities).hasSize(1);

    int e = entities.get(0);
    assertThat(world.getComponent(e, TransformComponent.class)).isNotNull();
    SpriteComponent sprite = world.getComponent(e, SpriteComponent.class);
    assertThat(sprite).isNotNull();
    assertThat(cache.resolveTextureHandle(sprite.textureHandle())).isNotNull();
  }
}
