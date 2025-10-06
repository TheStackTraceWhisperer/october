package engine.services.rendering;

import org.joml.Matrix4f;

public interface Renderer {
void beginScene(Camera camera);
void submit(Mesh mesh, Texture texture, Matrix4f transform);
void endScene();
}
