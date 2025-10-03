package engine.services.glfw;

import engine.IService;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class GlfwContextService implements IService {
  private boolean initialized = false;
  private GLFWErrorCallback errorCallback;

  @Override
  public int priority() {
    return Integer.MIN_VALUE;
  }

  public void start() {
    // Install an error callback that logs to our SLF4J logger.
    this.errorCallback = GLFWErrorCallback.create((error, description) ->
      log.error("[GLFW Error] Code: {}, Description: {}", error, GLFWErrorCallback.getDescription(description))
    );
    this.errorCallback.set();

    if (!GLFW.glfwInit()) {
      if (this.errorCallback != null) {
        this.errorCallback.free();
        this.errorCallback = null;
      }
      throw new IllegalStateException("Unable to initialize GLFW");
    }
    this.initialized = true;
  }

  /**
   * Terminates GLFW and frees the error callback. Safe to call multiple times.
   */
  public void stop() {
    if (!initialized) {
      return;
    }

    GLFW.glfwTerminate();

    // Clear the GLFW error callback and free it
    GLFWErrorCallback prev = GLFW.glfwSetErrorCallback(null);
    if (prev != null) {
      prev.free();
    }

    errorCallback = null;
    initialized = false;
  }
}
