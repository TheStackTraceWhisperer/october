package engine.services.rendering;

import engine.IService;
import engine.services.rendering.gl.Shader;
import engine.services.resources.AssetCacheService;
import engine.services.window.WindowService;
import engine.services.world.components.UITransformComponent;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

@Singleton
@RequiredArgsConstructor
public class UIRendererService implements IService {

  private final AssetCacheService assetCacheService;
  private final WindowService windowService;
  private Camera uiCamera; // Does not inject, will be created locally
  private Shader uiShader;

  @Override
  public void start() {
    this.uiCamera = new Camera(); // Create a dedicated camera for the UI
    this.uiShader = assetCacheService.loadShader("ui", "/shaders/default.vert", "/shaders/default.frag");
    windowService.setResizeListener(this::resize);
    // Set initial projection
    resize(windowService.getWidth(), windowService.getHeight());
  }

  public void begin() {
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    uiShader.bind();
    uiShader.setUniform("uProjection", uiCamera.getProjectionMatrix());
    uiShader.setUniform("uView", new Matrix4f().identity());
  }

  public void submit(UITransformComponent transform, String textureHandle) {
    if (textureHandle == null) {
      return;
    }
    Texture texture = assetCacheService.resolveTextureHandle(textureHandle);
    Matrix4f modelMatrix = calculateModelMatrix(transform);

    texture.bind(0);
    uiShader.setUniform("uTextureSampler", 0);
    uiShader.setUniform("uModel", modelMatrix);

    Mesh quadMesh = assetCacheService.resolveMeshHandle("quad");
    glBindVertexArray(quadMesh.getVaoId());
    glDrawElements(GL_TRIANGLES, quadMesh.getVertexCount(), GL_UNSIGNED_INT, 0);
    glBindVertexArray(0);
  }

  public void end() {
    uiShader.unbind();
    glDisable(GL_BLEND);
  }

  public void resize(int width, int height) {
    if (uiCamera != null) {
      uiCamera.getProjectionMatrix().identity().ortho(0.0f, width, 0.0f, height, -1.0f, 1.0f);
    }
  }

  private Matrix4f calculateModelMatrix(UITransformComponent transform) {
    float[] bounds = transform.screenBounds;
    float width = bounds[2] - bounds[0];
    float height = bounds[3] - bounds[1];
    float posX = bounds[0] + width / 2.0f;
    float posY = bounds[1] + height / 2.0f;
    return new Matrix4f().translate(posX, posY, transform.offset.z).scale(width, height, 1.0f);
  }
}