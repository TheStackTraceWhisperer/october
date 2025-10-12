package engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.lwjgl.glfw.GLFW;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.DisplayName.class)
class ApplicationLoopPolicyIT extends EngineTestHarness {

  @Test
  void framesPolicy_respectsFrameLimit() {
    // Arrange
    ApplicationLoopPolicy policy = ApplicationLoopPolicy.frames(3);

    // Act & Assert
    assertThat(policy.continueRunning(0, engine.getWindowService().getHandle())).isTrue();
    assertThat(policy.continueRunning(1, engine.getWindowService().getHandle())).isTrue();
    assertThat(policy.continueRunning(2, engine.getWindowService().getHandle())).isTrue();
    assertThat(policy.continueRunning(3, engine.getWindowService().getHandle())).isFalse();
  }

  @Test
  void skipPolicy_neverRuns() {
    // Arrange
    ApplicationLoopPolicy policy = ApplicationLoopPolicy.skip();

    // Act
    boolean shouldRun = policy.continueRunning(0, engine.getWindowService().getHandle());

    // Assert
    assertThat(shouldRun).isFalse();
  }

  @Test
  void timedPolicy_zeroDuration_neverRuns() {
    // Arrange
    ApplicationLoopPolicy policy = ApplicationLoopPolicy.timed(Duration.ZERO);

    // Act & Assert
    assertThat(policy.continueRunning(0, engine.getWindowService().getHandle())).isFalse();
  }

  @Test
  void timedPolicy_positiveDuration_runsThenStops() throws InterruptedException {
    // Arrange
    ApplicationLoopPolicy policy = ApplicationLoopPolicy.timed(Duration.ofMillis(50));

    // Act
    boolean first = policy.continueRunning(0, engine.getWindowService().getHandle());
    Thread.sleep(60);
    boolean second = policy.continueRunning(1, engine.getWindowService().getHandle());

    // Assert
    assertThat(first).isTrue();
    assertThat(second).isFalse();
  }

  @Test
  void standardPolicy_respectsWindowCloseFlag() {
    // Arrange
    long handle = engine.getWindowService().getHandle();

    // Act
    GLFW.glfwSetWindowShouldClose(handle, true);

    // Assert
    assertThat(ApplicationLoopPolicy.standard().continueRunning(0, handle)).isFalse();

    // Cleanup: restore for other tests
    GLFW.glfwSetWindowShouldClose(handle, false);
  }

  @Test
  void combinators_any_all_behaveCorrectly() {
    // Arrange
    ApplicationLoopPolicy always = (f, h) -> true;
    ApplicationLoopPolicy never = (f, h) -> false;

    // Act & Assert
    assertThat(ApplicationLoopPolicy.any(always, never).continueRunning(0, engine.getWindowService().getHandle())).isTrue();
    assertThat(ApplicationLoopPolicy.any(never, never).continueRunning(0, engine.getWindowService().getHandle())).isFalse();

    assertThat(ApplicationLoopPolicy.all(always, always).continueRunning(0, engine.getWindowService().getHandle())).isTrue();
    assertThat(ApplicationLoopPolicy.all(always, never).continueRunning(0, engine.getWindowService().getHandle())).isFalse();
  }
}

