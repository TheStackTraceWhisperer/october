package engine.services.zone;

import engine.services.event.EventPublisherService;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class ZoneServiceIT {

  @Inject
  private ZoneService zoneService;

  @BeforeEach
  void setUp() {
    // Reset state before each test
  }

  @Test
  void testZoneServiceIsInjectable() {
    // Given the Micronaut context is initialized
    // Then the ZoneService should be successfully injected
    assertThat(zoneService).isNotNull();
  }

  @Test
  void testGetCurrentZoneReturnsNullWhenNoZoneIsLoaded() {
    // Given no zone has been loaded
    // When we get the current zone
    Zone currentZone = zoneService.getCurrentZone();
    
    // Then it should be null
    assertThat(currentZone).isNull();
  }

  @Test
  void testExecutionOrder() {
    // Given the ZoneService
    // When we check its execution order
    int order = zoneService.executionOrder();
    
    // Then it should be 20
    assertThat(order).isEqualTo(20);
  }
}
