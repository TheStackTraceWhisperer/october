package engine.services.rendering;

import engine.EngineTestHarness;
import engine.services.resources.AssetCacheService;
import jakarta.inject.Inject;
import org.joml.Matrix4f;
import org.junit.jupiter.api.Test;

class UIRendererServiceIT extends EngineTestHarness {

  @Inject
  private AssetCacheService assetCacheService;

  @Inject
  private UIRendererService uiRenderer;

  @Test
  void canBeginSubmitAndEndScene() {
    // Arrange
    Texture texture = assetCacheService.loadTexture("test_tex", "/textures/valid_texture.png");
    Matrix4f transform = new Matrix4f().identity();

    // Act
    uiRenderer.begin();
    uiRenderer.submit(texture, transform);
    uiRenderer.end();

    // Assert: No exceptions thrown
  }

  @Test
  void sceneWithNoSubmissionsIsHandled() {
    // Arrange
    // No submissions

    // Act
    uiRenderer.begin();
    uiRenderer.end();

    // Assert: No exceptions thrown
  }

  @Test
  void resizeIsHandledCorrectly() {
    // Arrange
    int newWidth = engine.getWindowService().getWidth() / 2;
    int newHeight = engine.getWindowService().getHeight() / 2;

    // Act
    uiRenderer.resize(newWidth, newHeight);

    // Assert: No exceptions thrown, coverage for resize method
  }

  // close() is managed by the engine lifecycle, so we don't test it directly here.
  // The @MicronautTest ensures the context is shut down, which calls the close() method
  // via the RenderingService.
}
