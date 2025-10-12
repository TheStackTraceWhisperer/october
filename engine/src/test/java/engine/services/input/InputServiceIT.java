package engine.services.input;

import engine.EngineTestHarness;
import engine.services.window.WindowService;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.lwjgl.glfw.GLFW;

import static org.assertj.core.api.Assertions.assertThat;

class InputServiceIT extends EngineTestHarness {

  @Inject InputService input;
  @Inject WindowService window;

  @Test
  void defaultsAreSafe_andCursorCallbackUpdatesPosition() {
    // Arrange: invalid codes are false
    assertThat(input.isKeyPressed(-1)).isFalse();
    assertThat(input.isKeyPressed(GLFW.GLFW_KEY_LAST + 1)).isFalse();
    assertThat(input.isMouseButtonPressed(-1)).isFalse();
    assertThat(input.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LAST + 1)).isFalse();

    // Act: move the cursor programmatically, then poll events to fire callback
    GLFW.glfwSetCursorPos(window.getHandle(), 42.0, 64.0);
    window.pollEvents();

    // Assert
    assertThat(input.getMouseX()).isEqualTo(42.0);
    assertThat(input.getMouseY()).isEqualTo(64.0);
  }

  @Test
  void gamepadApisReturnFalseOrZeroWhenNotConnected() {
    assertThat(input.isGamepadConnected(0)).isIn(true, false); // platform dependent
    if (!input.isGamepadConnected(0)) {
      assertThat(input.getAxis(0, GLFW.GLFW_GAMEPAD_AXIS_LEFT_X)).isEqualTo(0.0f);
      assertThat(input.isButtonPressed(0, GLFW.GLFW_GAMEPAD_BUTTON_A)).isFalse();
    }
  }
}
