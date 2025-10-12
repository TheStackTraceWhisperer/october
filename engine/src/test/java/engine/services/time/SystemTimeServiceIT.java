package engine.services.time;

import engine.EngineTestHarness;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SystemTimeServiceIT extends EngineTestHarness {

  @Inject
  SystemTimeService time;

  @Test
  void updateAdvancesDeltaAndTotalTime() throws Exception {
    // Arrange: initial state set during engine init
    double beforeTotal = time.getTotalTimeSeconds();

    // Act
    Thread.sleep(5);
    time.update();

    // Assert
    assertThat(time.getDeltaTimeSeconds()).isGreaterThan(0.0f);
    assertThat(time.getTotalTimeSeconds()).isGreaterThanOrEqualTo(beforeTotal);
  }
}
