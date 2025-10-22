package engine.services.zone;

import engine.IService;
import engine.services.event.EventPublisherService;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * A core service responsible for loading, managing, and providing access to the currently active Zone.
 */
@Slf4j
@Singleton
@RequiredArgsConstructor
public class ZoneService implements IService {
  private final EventPublisherService eventPublisherService;

  @Getter
  private Zone currentZone;

  @Override
  public int executionOrder() {
    return 20;
  }

  /**
   * Loads a zone from the given zone ID.
   * This method reads zone data from a file (e.g., JSON), deserializes it into the Zone data model
   * and all its sub-components (Tilemap, Triggers, etc.), and sets it as the currentZone.
   * After successfully loading a zone, it emits a ZoneLoadedEvent.
   * 
   * @param zoneId The unique identifier of the zone to load
   */
  public void loadZone(String zoneId) {
    log.debug("Loading zone: {}", zoneId);
    
    // TODO: Implement zone loading from file
    // For now, this is a placeholder that will need to be implemented
    // when actual zone file loading is required
    
    log.warn("Zone loading not yet implemented for zone: {}", zoneId);
    
    // Once a zone is loaded, emit the event
    // eventPublisherService.publish(new ZoneLoadedEvent(currentZone));
  }

  /**
   * Gets the currently active zone.
   * 
   * @return The current zone, or null if no zone is loaded
   */
  public Zone getCurrentZone() {
    return currentZone;
  }
}
