package engine.services.rendering;

import engine.EngineTestHarness;
import engine.services.resources.AssetCacheService;
import jakarta.inject.Inject;
import org.joml.Matrix4f;
import org.junit.jupiter.api.Test;

class RenderingAndUIRendererIT extends EngineTestHarness {

  @Inject RenderingService renderingService;
  @Inject UIRendererService uiRenderer;
  @Inject Camera camera;
  @Inject AssetCacheService cache;

  @Test
  void canBeginSubmitAndEndScene_withInstancedRendering() {
    // Arrange: load a texture and get the default quad mesh
    Texture tex = cache.loadTexture("sprite", "/textures/valid_texture.png");
    Mesh quad = cache.resolveMeshHandle("quad");

    // Act
    renderingService.beginScene(camera);
    renderingService.submit(quad, tex, new Matrix4f().identity());
    renderingService.endScene();

    // Assert: no exceptions thrown
  }

  @Test
  void uiRendererCanRenderASimpleElement() {
    // Arrange
    Texture uiTexture = cache.loadTexture("uiTex", "/textures/valid_texture.png");
    Matrix4f transform = new Matrix4f().identity().translate(10, 10, 0).scale(32, 32, 1);

    // Act: begin->submit->end should render without throwing
    uiRenderer.begin();
    uiRenderer.submit(uiTexture, transform);
    uiRenderer.end();

    // Assert: no exceptions thrown
  }
}
