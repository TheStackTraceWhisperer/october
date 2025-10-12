package engine.services.rendering;

import engine.EngineTestHarness;
import engine.services.rendering.gl.Shader;
import engine.services.resources.AssetLoaderUtility;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

class MeshAndShaderIT extends EngineTestHarness {

  @Test
  void creatingAndClosingMeshDoesNotThrow() {
    // Arrange
    float[] vertices = { 0,0,0, 0,0, 1,0,0, 1,0, 0,1,0, 0,1 };
    int[] indices = { 0,1,2 };

    // Act & Assert
    try (Mesh mesh = new Mesh(vertices, indices)) {
      assertThat(mesh.getVaoId()).isNotZero();
    }
  }

  @Test
  void shaderCompilationErrorIsSurfaced() {
    // Arrange: valid vertex, invalid fragment to force compile/link failure
    String vertex = "/shaders/valid_vertex.vert";
    String badFragment = "/shaders/syntax_error.frag";

    // Act
    Throwable thrown = catchThrowable(() -> {
      try (Shader shader = AssetLoaderUtility.loadShader(vertex, badFragment)) {
        // Should not be reached
      }
    });

    // Assert
    assertThat(thrown).isInstanceOf(RuntimeException.class);
  }
}
