package engine.services.rendering;

import engine.EngineTestHarness;
import engine.services.resources.AssetCacheService;
import jakarta.inject.Inject;
import org.joml.Matrix4f;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SpriteRendererIT extends EngineTestHarness {

  @Inject
  private AssetCacheService assetCacheService;

  @Inject
  private Camera camera;

  private SpriteRenderer spriteRenderer;

  @BeforeEach
  void setUp() {
    spriteRenderer = new SpriteRenderer(assetCacheService);
    spriteRenderer.start();
  }

  @AfterEach
  void tearDown() {
    spriteRenderer.close();
  }

  @Test
  void canBeginSubmitAndEndScene() {
    // Arrange
    Texture texture = assetCacheService.loadTexture("test_tex", "/textures/valid_texture.png");
    Matrix4f transform = new Matrix4f().identity();

    // Act
    spriteRenderer.beginScene(camera);
    spriteRenderer.submit(texture, transform);
    spriteRenderer.endScene();

    // Assert: No exceptions thrown
  }

  @Test
  void sceneWithNoSubmissionsIsHandled() {
    // Arrange
    // No submissions

    // Act
    spriteRenderer.beginScene(camera);
    spriteRenderer.endScene();

    // Assert: No exceptions thrown
  }

  @Test
  void closeIsIdempotent() {
    // Arrange
    spriteRenderer.close();

    // Act & Assert
    spriteRenderer.close(); // should not throw
  }
}
