package engine;

import io.micronaut.context.event.ApplicationEventPublisher;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@SuppressWarnings({"rawtypes", "unchecked"})
@Singleton
@RequiredArgsConstructor
public class EventPublisher {
  private final ApplicationEventPublisher applicationEventPublisher;

  public <T> void publish(T event) {
    applicationEventPublisher.publishEvent(event);
  }
}
