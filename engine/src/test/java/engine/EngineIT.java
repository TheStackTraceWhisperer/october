package engine;

import engine.services.window.WindowService;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test to verify that the EngineTestHarness can successfully
 * bootstrap the engine and its core services.
 */
class EngineIT extends EngineTestHarness {

  @Inject
  private WindowService windowService;

  @Test
  void engineAndWindowServiceShouldBeInitialized() {
    // The 'engine' field is inherited from the harness and should be injected.
    assertThat(engine).isNotNull();

    // The WindowService should be injected and have created a valid window handle.
    assertThat(windowService).isNotNull();
    assertThat(windowService.getHandle())
      .withFailMessage("Window handle should have been created by the EngineTestHarness")
      .isNotZero();
  }
}

