package engine.services.rendering;

import engine.services.rendering.gl.Shader;
import engine.services.resources.AssetCacheService;
import org.joml.Matrix4f;

/**
 * A specialized renderer for drawing sprites using instanced rendering.
 * <p>
 * This renderer is responsible for managing the shader, mesh, and batching
 * required to efficiently draw large numbers of sprites.
 */
public class SpriteRenderer implements AutoCloseable {

  private final AssetCacheService assetCacheService;
  private Shader instancedShader;
  private SpriteBatch spriteBatch;
  private InstancedMesh quadMesh;

  public SpriteRenderer(AssetCacheService assetCacheService) {
    this.assetCacheService = assetCacheService;
  }

  /**
   * Initializes the renderer by loading shaders and creating the quad mesh.
   */
  public void start() {
    this.instancedShader = assetCacheService.loadShader(
      "default",
      "/shaders/default.vert",
      "/shaders/default.frag"
    );
    this.spriteBatch = new SpriteBatch();

    float[] vertices = {
      -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, // Top-left
      0.5f, 0.5f, 0.0f, 1.0f, 1.0f, // Top-right
      0.5f, -0.5f, 0.0f, 1.0f, 0.0f, // Bottom-right
      -0.5f, -0.5f, 0.0f, 0.0f, 0.0f  // Bottom-left
    };
    int[] indices = {0, 3, 2, 2, 1, 0};
    this.quadMesh = new InstancedMesh(vertices, indices);
  }

  /**
   * Prepares the renderer for a new scene.
   *
   * @param camera The camera providing the view and projection matrices.
   */
  public void beginScene(Camera camera) {
    spriteBatch.clear();
    instancedShader.bind();
    instancedShader.setUniform("uProjection", camera.getProjectionMatrix());
    instancedShader.setUniform("uView", camera.getViewMatrix());
  }

  /**
   * Submits a sprite to be rendered in the current batch.
   *
   * @param texture   The texture to use for the sprite.
   * @param transform The transformation matrix for the sprite.
   */
  public void submit(Texture texture, Matrix4f transform) {
    spriteBatch.addSprite(texture, transform);
  }

  /**
   * Renders all batched sprites.
   */
  public void endScene() {
    for (Texture texture : spriteBatch.getTextures()) {
      var transforms = spriteBatch.getSpritesForTexture(texture);
      if (!transforms.isEmpty()) {
        texture.bind(0);
        instancedShader.setUniform("uTextureSampler", 0);
        quadMesh.renderInstanced(transforms);
      }
    }
    instancedShader.unbind();
  }

  @Override
  public void close() {
    if (quadMesh != null) {
      quadMesh.close();
    }
    // Shaders are managed by AssetCacheService, so we don't close them here.
  }
}
