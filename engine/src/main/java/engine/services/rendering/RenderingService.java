package engine.services.rendering;

import engine.IService;
import engine.services.rendering.gl.Shader;
import engine.services.resources.AssetCacheService;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

@Singleton
@RequiredArgsConstructor
public class RenderingService implements IService, Renderer {

  private final AssetCacheService assetCacheService;

  private Shader defaultShader;

  public void start() {
    // Load the default shader program from files.
    this.defaultShader = assetCacheService.loadShader(
      "default",
      "/shaders/default.vert",
      "/shaders/default.frag"
    );
  }

  @Override
  public void beginScene(Camera camera) {
    // Clear the screen to a dark grey color
    glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    // Prepare the shader for the scene by binding it and setting the camera matrices
    defaultShader.bind();
    defaultShader.setUniform("uProjection", camera.getProjectionMatrix());
    defaultShader.setUniform("uView", camera.getViewMatrix());
  }

  @Override
  public void submit(Mesh mesh, Texture texture, Matrix4f transform) {
    // Bind the specific texture for this sprite to texture unit 0
    texture.bind(0);
    defaultShader.setUniform("uTextureSampler", 0); // Tell the shader to use texture unit 0

    // Set the model matrix for this specific object
    defaultShader.setUniform("uModel", transform);

    // Bind the mesh's VAO
    glBindVertexArray(mesh.getVaoId());

    // Draw the object using its index buffer
    glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);

    // Unbind the VAO for good practice
    glBindVertexArray(0);
  }

  @Override
  public void endScene() {
    // Unbind the shader program
    defaultShader.unbind();
  }
}
