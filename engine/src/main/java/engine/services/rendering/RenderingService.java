package engine.services.rendering;

import engine.IService;
import engine.services.rendering.gl.Shader;
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

  private Shader instancedShader;
  private SpriteBatch spriteBatch;
  private InstancedMesh quadMesh;

  @Override
  public void start() {
    // Load the instanced shader program from files.
    this.instancedShader = assetCacheService.loadShader(
      "default",
      "/shaders/default.vert",
      "/shaders/default.frag"
    );
    this.spriteBatch = new SpriteBatch();

    // Define vertices for a quad that covers the entire screen in Normalized Device Coordinates
    float[] vertices = {
      // Positions        // Texture Coords
      -0.5f, 0.5f, 0.0f,   0.0f, 1.0f, // Top-left
      0.5f, 0.5f, 0.0f,    1.0f, 1.0f, // Top-right
      0.5f, -0.5f, 0.0f,   1.0f, 0.0f, // Bottom-right
      -0.5f, -0.5f, 0.0f,  0.0f, 0.0f  // Bottom-left
    };

    int[] indices = {
      0, 3, 2, // First triangle
      2, 1, 0  // Second triangle
    };

    this.quadMesh = new InstancedMesh(vertices, indices);
  }

  @Override
  public void beginScene(Camera camera) {
    // Clear the screen
    glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    // Clear the sprite batch for this frame
    spriteBatch.clear();

    // Prepare the shader for the scene
    instancedShader.bind();
    instancedShader.setUniform("uProjection", camera.getProjectionMatrix());
    instancedShader.setUniform("uView", camera.getViewMatrix());
  }

  @Override
  public void submit(Mesh mesh, Texture texture, Matrix4f transform) {
    // Add sprite to batch instead of rendering immediately
    spriteBatch.addSprite(texture, new Matrix4f(transform));
  }

  @Override
  public void endScene() {
    // Render all batches
    for (Texture texture : spriteBatch.getTextures()) {
      var transforms = spriteBatch.getSpritesForTexture(texture);

      if (!transforms.isEmpty()) {
        // Bind texture
        texture.bind(0);
        instancedShader.setUniform("uTextureSampler", 0);

        // Render all instances for this texture
        quadMesh.renderInstanced(transforms);
      }
    }

    // Unbind shader
    instancedShader.unbind();
  }
}
