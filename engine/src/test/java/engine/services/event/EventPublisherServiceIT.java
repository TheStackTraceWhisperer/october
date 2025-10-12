package engine.services.event;

import api.events.EngineStarted;
import engine.EngineTestHarness;
import io.micronaut.context.event.ApplicationEventListener;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventPublisherServiceIT extends EngineTestHarness {

  @Inject EventPublisherService publisher;
  @Inject StartedListener listener;

  @Test
  void publishesEventsThroughMicronaut() {
    // Arrange
    int before = listener.count;

    // Act
    publisher.publish(new EngineStarted());

    // Assert
    assertThat(listener.count).isEqualTo(before + 1);
  }

  @Singleton
  static class StartedListener implements ApplicationEventListener<EngineStarted> {
    volatile int count = 0;

    @Override
    public void onApplicationEvent(EngineStarted event) {
      count++;
    }
  }
}
