package engine.services.resources;

import engine.IService;
import engine.services.audio.AudioBuffer;
import engine.services.rendering.Mesh;
import engine.services.rendering.Texture;
import engine.services.rendering.gl.Shader;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Singleton
public class AssetCacheService implements IService {

  private final Map<String, Mesh> meshCache = new HashMap<>();
  private final Map<String, Texture> textureCache = new HashMap<>();
  private final Map<String, Shader> shaderCache = new HashMap<>();
  private final Map<String, AudioBuffer> audioBufferCache = new HashMap<>();


  @Override
  public void start() {
    // Define the vertices for a standard quad mesh
    float[] vertices = {
      // Position           // UV Coords
      0.5f,  0.5f, 0.0f,   1.0f, 1.0f, // Top Right
      0.5f, -0.5f, 0.0f,   1.0f, 0.0f, // Bottom Right
      -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, // Bottom Left
      -0.5f,  0.5f, 0.0f,   0.0f, 1.0f  // Top Left
    };

    // Define the indices for the quad
    int[] indices = {
      0, 1, 3, // First Triangle
      1, 2, 3  // Second Triangle
    };

    // Programmatically create and load the quad mesh
    loadProceduralMesh("quad", vertices, indices);
  }

  /**
   * Loads a texture from a file, stores it in the cache, and returns it.
   * If a texture with the same handle already exists, it will be closed and replaced.
   *
   * @param handle   The unique handle for this texture.
   * @param filePath The classpath path to the image file.
   * @return The newly loaded Texture.
   */
  public Texture loadTexture(String handle, String filePath) {
    Texture newTexture = AssetLoaderUtility.loadTexture(filePath);
    Texture oldTexture = textureCache.put(handle, newTexture);
    if (oldTexture != null) {
      oldTexture.close();
    }
    return newTexture;
  }

  /**
   * Loads a shader program from two files, stores it, and returns it.
   * If a shader with the same handle already exists, it will be closed and replaced.
   *
   * @param handle       The unique handle for this shader.
   * @param vertexPath   The classpath path to the vertex shader file.
   * @param fragmentPath The classpath path to the fragment shader file.
   * @return The newly loaded Shader.
   */
  public Shader loadShader(String handle, String vertexPath, String fragmentPath) {
    Shader newShader = AssetLoaderUtility.loadShader(vertexPath, fragmentPath);
    Shader oldShader = shaderCache.put(handle, newShader);
    if (oldShader != null) {
      oldShader.close();
    }
    return newShader;
  }

  /**
   * Creates a new Mesh from raw vertex data and stores it under a given handle.
   * If a mesh with the same handle already exists, it will be closed and replaced.
   *
   * @param handle   The unique string identifier for this mesh.
   * @param vertices The vertex data (e.g., positions, UVs).
   * @param indices  The index data defining the triangles.
   */
  public void loadProceduralMesh(String handle, float[] vertices, int[] indices) {
    Mesh newMesh = new Mesh(vertices, indices);
    Mesh oldMesh = meshCache.put(handle, newMesh);
    if (oldMesh != null) {
      oldMesh.close();
    }
  }

  public Mesh resolveMeshHandle(String handle) {
    Mesh mesh = meshCache.get(handle);
    Objects.requireNonNull(mesh, "Mesh not found: " + handle);
    return mesh;
  }

  public Texture resolveTextureHandle(String handle) {
    Texture texture = textureCache.get(handle);
    Objects.requireNonNull(texture, "Texture not found: " + handle);
    return texture;
  }

  /**
   * Loads an audio buffer from an OGG Vorbis file, stores it in the cache, and returns it.
   * If an audio buffer with the same handle already exists, it will be closed and replaced.
   *
   * @param handle   The unique handle for this audio buffer.
   * @param filePath The classpath path to the OGG file.
   * @return The newly loaded AudioBuffer.
   */
  public AudioBuffer loadAudioBuffer(String handle, String filePath) {
    AudioBuffer newAudioBuffer = AudioBuffer.loadFromOggFile(filePath);
    AudioBuffer oldAudioBuffer = audioBufferCache.put(handle, newAudioBuffer);
    if (oldAudioBuffer != null) {
      oldAudioBuffer.close();
    }
    return newAudioBuffer;
  }

  public Shader resolveShaderHandle(String handle) {
    Shader shader = shaderCache.get(handle);
    Objects.requireNonNull(shader, "Shader not found: " + handle);
    return shader;
  }

  public AudioBuffer resolveAudioBufferHandle(String handle) {
    AudioBuffer audioBuffer = audioBufferCache.get(handle);
    Objects.requireNonNull(audioBuffer, "AudioBuffer not found: " + handle);
    return audioBuffer;
  }

  /**
   * Frees all managed resources. This iterates through all cached assets
   * and calls their respective close() methods to release native resources.
   */
  @PreDestroy
  public void close() {
    meshCache.values().forEach(Mesh::close);
    meshCache.clear();

    textureCache.values().forEach(Texture::close);
    textureCache.clear();

    shaderCache.values().forEach(Shader::close);
    shaderCache.clear();

    audioBufferCache.values().forEach(AudioBuffer::close);
    audioBufferCache.clear();
  }
}
