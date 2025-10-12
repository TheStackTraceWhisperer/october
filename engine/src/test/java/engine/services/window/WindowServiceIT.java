package engine.services.window;

import engine.EngineTestHarness;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class WindowServiceIT extends EngineTestHarness {

  @Inject
  WindowService windowService;

  @Test
  void windowIsCreated_andResizeListenerReceivesUpdates() {
    // Arrange
    assertThat(windowService.getHandle()).as("GLFW window handle").isNotZero();
    int originalW = windowService.getWidth();
    int originalH = windowService.getHeight();

    AtomicInteger seenW = new AtomicInteger(-1);
    AtomicInteger seenH = new AtomicInteger(-1);
    windowService.setResizeListener((w, h) -> {
      seenW.set(w);
      seenH.set(h);
    });

    // Act: trigger a resize and poll events to deliver the callback
    int newW = Math.max(64, originalW - 10);
    int newH = Math.max(64, originalH - 10);
    setWindowSize(newW, newH);
    windowService.pollEvents();

    // Assert (robust across WMs): dimensions are valid and callback observed positive framebuffer size
    assertThat(windowService.getWidth()).isGreaterThan(0);
    assertThat(windowService.getHeight()).isGreaterThan(0);

    // The framebuffer size may differ on HiDPI, and some WMs ignore programmatic resizes,
    // but the callback should have run with positive values at least once.
    assertThat(seenW.get()).isGreaterThan(0);
    assertThat(seenH.get()).isGreaterThan(0);
  }

  @Test
  void canSwapBuffersAndPollEvents_withoutExceptions() {
    // Arrange
    long handle = windowService.getHandle();
    assertThat(handle).isNotZero();

    // Act & Assert: these calls should not throw
    windowService.swapBuffers();
    windowService.pollEvents();
  }
}
