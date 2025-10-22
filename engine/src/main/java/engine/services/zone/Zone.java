package engine.services.zone;

import engine.services.zone.sequence.Sequence;
import engine.services.zone.sequence.Trigger;
import engine.services.zone.tilemap.Tilemap;

import java.util.List;
import java.util.Map;

/**
 * Holds the complete definition for a game level, including its layout, scripts, and triggers.
 */
public interface Zone {
  /**
   * @return A unique identifier for the zone (e.g., "forest_glade_01").
   */
  String getId();

  /**
   * @return A user-friendly name (e.g., "Forest Glade").
   */
  String getName();

  /**
   * @return The tilemap data for this zone's layout.
   */
  Tilemap getTilemap();

  /**
   * @return A list of all available sequence "scripts" for this zone.
   */
  List<Sequence> getSequences();

  /**
   * @return A list of all triggers that can fire events within this zone.
   */
  List<Trigger> getTriggers();

  /**
   * @return A key-value store for zone-level metadata (e.g., "musicTrack": "forest_theme").
   */
  Map<String, Object> getProperties();
}
