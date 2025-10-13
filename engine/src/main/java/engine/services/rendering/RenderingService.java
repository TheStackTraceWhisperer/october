package engine.services.rendering;

import engine.IService;
import engine.services.resources.AssetCacheService;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

@Singleton
@RequiredArgsConstructor
public class RenderingService implements IService, Renderer {

  private final AssetCacheService assetCacheService;
  private final UIRendererService uiRendererService;
  private SpriteRenderer spriteRenderer;

  @Override
  public void start() {
    spriteRenderer = new SpriteRenderer(assetCacheService);
    spriteRenderer.start();
    uiRendererService.start();
  }

  @Override
  public void beginScene(Camera camera) {
    // Clear the screen
    glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    // Begin 3D/sprite scene
    spriteRenderer.beginScene(camera);
  }

  @Override
  public void submit(Mesh mesh, Texture texture, Matrix4f transform) {
    // The mesh is ignored for now, as we are only rendering sprites.
    // This will be addressed in a future refactoring.
    spriteRenderer.submit(texture, transform);
  }

  @Override
  public void endScene() {
    // End and render 3D/sprite scene
    spriteRenderer.endScene();

    // Begin and render UI scene
    uiRendererService.begin();
    // Note: UI submission logic is handled by the UISystem, so we just end the frame here.
    uiRendererService.end();
  }

  @Override
  public void stop() {
    if (spriteRenderer != null) {
      spriteRenderer.close();
    }
    if (uiRendererService != null) {
      uiRendererService.close();
    }
  }
}
