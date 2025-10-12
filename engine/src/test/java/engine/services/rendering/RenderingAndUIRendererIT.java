package engine.services.rendering;

import engine.EngineTestHarness;
import engine.services.resources.AssetCacheService;
import jakarta.inject.Inject;
import org.joml.Matrix4f;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RenderingAndUIRendererIT extends EngineTestHarness {

  @Inject RenderingService renderer;
  @Inject UIRendererService uiRenderer;
  @Inject Camera camera;
  @Inject AssetCacheService cache;

  @Test
  void canBeginSubmitAndEndScene_withInstancedRendering() {
    // Arrange: load a texture and use default quad mesh created by cache
    cache.loadTexture("sprite", "/textures/valid_texture.png");
    Mesh quad = cache.resolveMeshHandle("quad");
    Texture tex = cache.resolveTextureHandle("sprite");

    // Act
    renderer.beginScene(camera);
    renderer.submit(quad, tex, new Matrix4f().identity());
    renderer.endScene();

    // Assert: no exceptions and resources are present
    assertThat(quad.getVaoId()).isPositive();
  }

  @Test
  void uiRendererCanRenderASimpleElement() {
    // Arrange
    cache.loadTexture("uiTex", "/textures/valid_texture.png");
    var ui = new engine.services.world.components.UITransformComponent();
    ui.relativeSize = false;
    ui.size.set(32, 32);
    ui.anchor.set(0, 0);
    ui.pivot.set(0, 0);
    ui.offset.set(10, 10, 0);
    // pretend layout already ran
    ui.screenBounds[0] = 10; ui.screenBounds[1] = 10; ui.screenBounds[2] = 42; ui.screenBounds[3] = 42;

    // Act: begin->submit->end should render without throwing
    uiRenderer.begin();
    uiRenderer.submit(ui, "uiTex");
    uiRenderer.end();
  }
}
