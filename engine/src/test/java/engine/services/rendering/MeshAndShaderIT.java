package engine.services.rendering;

import engine.EngineTestHarness;
import engine.services.resources.AssetCacheService;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MeshAndShaderIT extends EngineTestHarness {

  @Inject AssetCacheService cache;

  @Test
  void creatingAndClosingMeshDoesNotThrow() {
    // Arrange
    float[] vertices = { 0,0,0, 0,0, 1,0,0, 1,0, 0,1,0, 0,1 };
    int[] indices = { 0,1,2 };

    // Act & Assert
    try (Mesh mesh = new Mesh(vertices, indices)) {
      // no-op
    }
  }

  @Test
  void shaderCompilationErrorIsSurfaced() {
    // Arrange: valid vertex, invalid fragment to force compile/link failure
    String vertex = "/shaders/valid_vertex.vert";
    String badFragment = "/shaders/syntax_error.frag";

    // Act & Assert
    assertThatThrownBy(() -> cache.loadShader("bad", vertex, badFragment))
      .isInstanceOf(RuntimeException.class);
  }
}
