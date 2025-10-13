package engine.services.resources;

import engine.EngineTestHarness;
import engine.services.rendering.Mesh;
import engine.services.rendering.Texture;
import engine.services.rendering.gl.Shader;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AssetCacheAndLoaderIT extends EngineTestHarness {

  @Inject
  AssetCacheService cache;

  @Test
  void loadsAndResolvesProceduralMeshAndTexture() {
    // Arrange
    float[] vertices = {
      0,0,0, 0,0,
      1,0,0, 1,0,
      0,1,0, 0,1
    };
    int[] indices = {0,1,2};

    // Act
    cache.loadProceduralMesh("tri", vertices, indices);

    // Assert
    Mesh m = cache.resolveMeshHandle("tri");
    assertThat(m).isNotNull();

    var tex = cache.loadTexture("t-valid", "/textures/valid_texture.png");
    assertThat(cache.resolveTextureHandle("t-valid")).isSameAs(tex);
  }

  @Test
  void reloadingAssetWithSameHandle_returnsNewInstance() {
    // Arrange
    Texture originalTexture = cache.loadTexture("reload-test", "/textures/valid_texture.png");

    // Act: Load a different texture with the same handle
    Texture reloadedTexture = cache.loadTexture("reload-test", "/textures/test.png");

    // Assert: The cache returns the new instance, and the original is a different object
    assertThat(reloadedTexture).isNotSameAs(originalTexture);
  }

  @Test
  void shaderLoadingFromResourcesWorks_andInvalidPathsFail() {
    // Arrange & Act
    Shader s = cache.loadShader("test-shader", "/shaders/valid_vertex.vert", "/shaders/valid_fragment.frag");

    // Assert
    assertThat(s).isNotNull();

    // Negative: missing resource
    assertThatThrownBy(() -> cache.loadShader("missing", "/shaders/does_not_exist.vert", "/shaders/valid_fragment.frag"))
      .isInstanceOf(RuntimeException.class);
  }

  @Test
  void rawResourceReadHelpersWork_andMissingThrow() throws Exception {
    // Arrange & Act
    ByteBuffer buf = AssetLoaderUtility.readResourceToByteBuffer("/textures/test.png");

    // Assert
    assertThat(buf.remaining()).isGreaterThan(0);

    // Negative
    assertThatThrownBy(() -> AssetLoaderUtility.readResourceToByteBuffer("/textures/does_not_exist.png"))
      .isInstanceOf(Exception.class);
  }
}
