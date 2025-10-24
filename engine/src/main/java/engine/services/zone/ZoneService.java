package engine.services.zone;

import com.fasterxml.jackson.databind.ObjectMapper;
import engine.IService;
import engine.services.event.EventPublisherService;
import engine.services.zone.sequence.GameEvent;
import engine.services.zone.sequence.Sequence;
import engine.services.zone.sequence.Trigger;
import engine.services.zone.tilemap.Tilemap;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * A core service responsible for loading, managing, and providing access to the currently active Zone.
 */
@Slf4j
@Singleton
@RequiredArgsConstructor
public class ZoneService implements IService {
  private static final ObjectMapper MAPPER = new ObjectMapper();

  private final EventPublisherService eventPublisherService;

  @Getter
  private Zone currentZone;

  @Override
  public int executionOrder() {
    return 20;
  }

  /**
   * Loads a zone from the given zone ID by attempting to read a JSON file from classpath at
   * "/zones/{zoneId}.json". On success, sets it as current and publishes ZoneLoadedEvent.
   * If the resource is not found or fails to parse, falls back to a minimal BasicZone.
   */
  public void loadZone(String zoneId) {
    log.debug("Loading zone: {}", zoneId);

    Zone loaded = null;
    String resourcePath = "/zones/" + zoneId + ".json";
    try (InputStream is = ZoneService.class.getResourceAsStream(resourcePath)) {
      if (is != null) {
        JsonZone jsonZone = MAPPER.readValue(is, JsonZone.class);
        loaded = jsonZone;
        log.info("Loaded zone from resource {}: {}", resourcePath, jsonZone.getName());
      } else {
        log.warn("Zone resource not found: {}. Falling back to BasicZone.", resourcePath);
      }
    } catch (Exception e) {
      log.error("Failed to load zone from {}. Falling back to BasicZone.", resourcePath, e);
    }

    if (loaded == null) {
      loaded = new BasicZone(zoneId, zoneId);
    }

    this.currentZone = loaded;

    // Emit the event so systems can react
    eventPublisherService.publish(new ZoneLoadedEvent(currentZone));
    log.info("Zone loaded and event published: {}", currentZone.getId());
  }

  /**
   * Gets the currently active zone.
   * 
   * @return The current zone, or null if no zone is loaded
   */
  public Zone getCurrentZone() {
    return currentZone;
  }

  /**
   * Minimal Zone implementation used if no resource is available.
   */
  private static class BasicZone implements Zone {
    private final String id;
    private final String name;

    BasicZone(String id, String name) {
      this.id = id;
      this.name = name;
    }

    @Override
    public String getId() { return id; }

    @Override
    public String getName() { return name; }

    @Override
    public Tilemap getTilemap() { return null; }

    @Override
    public List<Sequence> getSequences() { return Collections.emptyList(); }

    @Override
    public List<Trigger> getTriggers() { return Collections.emptyList(); }

    @Override
    public Map<String, Object> getProperties() { return Collections.emptyMap(); }
  }

  // --- JSON data classes for simple zone deserialization ---
  public static class JsonGameEvent implements GameEvent {
    private String type;
    private Map<String, Object> properties;

    public JsonGameEvent() {}

    @Override public String getType() { return type; }
    @Override public Map<String, Object> getProperties() { return properties != null ? properties : Collections.emptyMap(); }

    public void setType(String type) { this.type = type; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
  }

  public static class JsonSequence implements Sequence {
    private String id;
    private List<JsonGameEvent> events;

    public JsonSequence() {}

    @Override public String getId() { return id; }
    @Override public List<GameEvent> getEvents() { return events != null ? List.copyOf(events) : List.of(); }

    public void setId(String id) { this.id = id; }
    public void setEvents(List<JsonGameEvent> events) { this.events = events; }
  }

  public static class JsonTrigger implements Trigger {
    private String id;
    private String type;
    private List<JsonGameEvent> events;
    private Map<String, Object> properties;

    public JsonTrigger() {}

    @Override public String getId() { return id; }
    @Override public String getType() { return type; }
    @Override public List<GameEvent> getEvents() { return events != null ? List.copyOf(events) : List.of(); }
    @Override public Map<String, Object> getProperties() { return properties != null ? properties : Collections.emptyMap(); }

    public void setId(String id) { this.id = id; }
    public void setType(String type) { this.type = type; }
    public void setEvents(List<JsonGameEvent> events) { this.events = events; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
  }

  public static class JsonZone implements Zone {
    private String id;
    private String name;
    private Tilemap tilemap; // not parsed yet
    private List<JsonSequence> sequences;
    private List<JsonTrigger> triggers;
    private Map<String, Object> properties;

    public JsonZone() {}

    @Override public String getId() { return id; }
    @Override public String getName() { return name; }
    @Override public Tilemap getTilemap() { return null; }
    @Override public List<Sequence> getSequences() { return sequences != null ? List.copyOf(sequences) : List.of(); }
    @Override public List<Trigger> getTriggers() { return triggers != null ? List.copyOf(triggers) : List.of(); }
    @Override public Map<String, Object> getProperties() { return properties != null ? properties : Collections.emptyMap(); }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setTilemap(Tilemap tilemap) { this.tilemap = tilemap; }
    public void setSequences(List<JsonSequence> sequences) { this.sequences = sequences; }
    public void setTriggers(List<JsonTrigger> triggers) { this.triggers = triggers; }
    public void setProperties(Map<String, Object> properties) { this.properties = properties; }
  }
}
