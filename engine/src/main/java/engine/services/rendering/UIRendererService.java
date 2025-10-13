package engine.services.rendering;

import engine.IService;
import engine.services.rendering.gl.Shader;
import engine.services.resources.AssetCacheService;
import engine.services.window.WindowService;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

@Singleton
@RequiredArgsConstructor
public class UIRendererService implements IService, AutoCloseable {

  private final AssetCacheService assetCacheService;
  private final WindowService windowService;
  private final SpriteBatch spriteBatch = new SpriteBatch();
  private Camera uiCamera;
  private Shader uiShader;
  private InstancedMesh quadMesh;

  @Override
  public void start() {
    this.uiCamera = new Camera();
    this.uiShader = assetCacheService.loadShader("ui", "/shaders/default.vert", "/shaders/default.frag");
    this.quadMesh = new InstancedMesh(
      new float[]{
        -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, // Top-left
        0.5f, 0.5f, 0.0f, 1.0f, 1.0f, // Top-right
        0.5f, -0.5f, 0.0f, 1.0f, 0.0f, // Bottom-right
        -0.5f, -0.5f, 0.0f, 0.0f, 0.0f  // Bottom-left
      },
      new int[]{0, 3, 2, 2, 1, 0}
    );
    windowService.setResizeListener(this::resize);
    resize(windowService.getWidth(), windowService.getHeight());
  }

  public void begin() {
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    spriteBatch.clear();
    uiShader.bind();
    uiShader.setUniform("uProjection", uiCamera.getProjectionMatrix());
    uiShader.setUniform("uView", new Matrix4f().identity());
  }

  public void submit(Texture texture, Matrix4f transform) {
    spriteBatch.addSprite(texture, transform);
  }

  public void end() {
    for (Texture texture : spriteBatch.getTextures()) {
      var transforms = spriteBatch.getSpritesForTexture(texture);
      if (!transforms.isEmpty()) {
        texture.bind(0);
        uiShader.setUniform("uTextureSampler", 0);
        quadMesh.renderInstanced(transforms);
      }
    }
    uiShader.unbind();
    glDisable(GL_BLEND);
  }

  public void resize(int width, int height) {
    if (uiCamera != null) {
      uiCamera.getProjectionMatrix().identity().ortho(0.0f, width, 0.0f, height, -1.0f, 1.0f);
    }
  }

  @Override
  public void close() {
    if (quadMesh != null) {
      quadMesh.close();
    }
  }
}
