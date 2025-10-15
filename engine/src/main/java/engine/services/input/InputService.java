package engine.services.input;

import engine.IService;
import engine.services.window.WindowService;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWGamepadState;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import java.util.Arrays;

@Singleton
@RequiredArgsConstructor
public class InputService implements IService {
  private final WindowService windowService;

  private static final int MAX_GAMEPADS = 8;

  private final boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST + 1];
  private final boolean[] mouseButtons = new boolean[GLFW.GLFW_MOUSE_BUTTON_LAST + 1];
  @Getter
  private double mouseX;
  @Getter
  private double mouseY;

  @Override
  public int priority() {
    return 15;
  }

  /**
   * Installs the necessary GLFW callbacks on the given window service to capture input events.
   *
   */
  @Override
  public void start() {
    GLFW.glfwSetKeyCallback(windowService.getHandle(), new GLFWKeyCallback() {
      @Override
      public void invoke(long window, int key, int scancode, int action, int mods) {
        if (key >= 0 && key <= GLFW.GLFW_KEY_LAST) {
          keys[key] = (action != GLFW.GLFW_RELEASE);
        }
      }
    });

    GLFW.glfwSetMouseButtonCallback(windowService.getHandle(), new GLFWMouseButtonCallback() {
      @Override
      public void invoke(long window, int button, int action, int mods) {
        if (button >= 0 && button <= GLFW.GLFW_MOUSE_BUTTON_LAST) {
          mouseButtons[button] = (action != GLFW.GLFW_RELEASE);
        }
      }
    });

    GLFW.glfwSetCursorPosCallback(windowService.getHandle(), new GLFWCursorPosCallback() {
      @Override
      public void invoke(long window, double xpos, double ypos) {
        mouseX = xpos;
        mouseY = ypos;
      }
    });
  }

  /**
   * Clears all input states. Useful for resetting state between frames if needed,
   * though the callback approach makes this less necessary.
   */
  public void clear() {
    Arrays.fill(keys, false);
    Arrays.fill(mouseButtons, false);
    mouseX = 0.0;
    mouseY = 0.0;
  }

  public boolean isKeyPressed(int keyCode) {
    if (keyCode < 0 || keyCode > GLFW.GLFW_KEY_LAST) {
      return false;
    }
    return keys[keyCode];
  }

  public boolean isMouseButtonPressed(int button) {
    if (button < 0 || button > GLFW.GLFW_MOUSE_BUTTON_LAST) {
      return false;
    }
    return mouseButtons[button];
  }

  public boolean isGamepadConnected(int port) {
    if (port < 0 || port >= MAX_GAMEPADS) {
      return false;
    }

    int joystickId = GLFW.GLFW_JOYSTICK_1 + port;
    return GLFW.glfwJoystickPresent(joystickId) && GLFW.glfwJoystickIsGamepad(joystickId);
  }

  public float getAxis(int port, int axis) {
    if (!isGamepadConnected(port)) {
      return 0.0f;
    }

    int joystickId = GLFW.GLFW_JOYSTICK_1 + port;
    GLFWGamepadState state = GLFWGamepadState.malloc();
    try {
      if (GLFW.glfwGetGamepadState(joystickId, state)) {
        return state.axes(axis);
      }
      return 0.0f;
    } finally {
      state.free();
    }
  }

  public boolean isButtonPressed(int port, int button) {
    if (!isGamepadConnected(port)) {
      return false;
    }

    int joystickId = GLFW.GLFW_JOYSTICK_1 + port;
    GLFWGamepadState state = GLFWGamepadState.malloc();
    try {
      if (GLFW.glfwGetGamepadState(joystickId, state)) {
        return state.buttons(button) == GLFW.GLFW_PRESS;
      }
      return false;
    } finally {
      state.free();
    }
  }
}
