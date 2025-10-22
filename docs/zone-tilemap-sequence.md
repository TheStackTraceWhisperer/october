# Zone, Tilemap, and Sequence Components

This document describes the implementation of the Zone, Tilemap, and Sequence components in the October engine.

## Overview

The Zone, Tilemap, and Sequence components provide a framework for:
- Defining game levels with multi-layered tilemaps
- Creating data-driven cutscenes and scripted events
- Triggering events based on game state conditions

## Architecture

### Data Models (Immutable)

All data models are defined as interfaces and are intended to be immutable data carriers.

#### Tilemap Component

Located in `engine.services.zone.tilemap`:

- **Tile**: Represents a single tile definition from a tileset
  - `int getId()`: Unique identifier within the tileset
  - `Image getImage()`: Graphical asset reference
  - `Map<String, Object> getProperties()`: Arbitrary metadata

- **Tileset**: Collection of tile definitions from a single tilesheet
  - `String getName()`: Tileset name
  - `Image getSourceImage()`: Full tilesheet image
  - `int getTileWidth/getTileHeight()`: Tile dimensions in pixels
  - `List<Tile> getTiles()`: All tile definitions
  - `Tile getTileById(int id)`: Lookup method

- **Tilelayer**: Single layer of a map as a 2D grid
  - `String getName()`: Layer name (e.g., "background", "foreground")
  - `int getWidth/getHeight()`: Dimensions in tile units
  - `int[][] getTileIds()`: 2D array of tile IDs (0 or -1 = empty)
  - `boolean isVisible()`: Visibility state
  - `float getOpacity()`: Layer opacity (0.0 to 1.0)

- **Tilemap**: Complete multi-layered map structure
  - `int getWidth/getHeight()`: Map dimensions in tile units
  - `int getTileWidth/getTileHeight()`: Pixel dimensions of tiles
  - `List<Tileset> getTilesets()`: All tilesets used
  - `List<Tilelayer> getTilelayers()`: Ordered layers (bottom to top)
  - `Map<String, Object> getProperties()`: Map-level metadata

#### Sequence Component

Located in `engine.services.zone.sequence`:

- **GameEvent**: Single atomic command
  - `String getType()`: Command type (e.g., "MOVE_ENTITY", "WAIT")
  - `Map<String, Object> getProperties()`: Command parameters

- **Sequence**: Ordered list of GameEvents forming a script
  - `String getId()`: Unique identifier
  - `List<GameEvent> getEvents()`: Commands to execute

- **Trigger**: Condition and events that fire when met
  - `String getId()`: Unique identifier
  - `String getType()`: Condition type (e.g., "ON_LOAD", "ON_ENTER_AREA")
  - `List<GameEvent> getEvents()`: Events to execute when triggered
  - `Map<String, Object> getProperties()`: Condition parameters

#### Zone Component

Located in `engine.services.zone`:

- **Zone**: Top-level container for a game level
  - `String getId()`: Unique identifier (e.g., "forest_glade_01")
  - `String getName()`: User-friendly name
  - `Tilemap getTilemap()`: Layout data
  - `List<Sequence> getSequences()`: Available scripts
  - `List<Trigger> getTriggers()`: Event triggers
  - `Map<String, Object> getProperties()`: Zone-level metadata

### Services

#### ZoneService

Located in `engine.services.zone.ZoneService`:

**Responsibilities:**
- Holds the reference to the current zone
- Loads zone data from files (deserializes Zone and sub-components)
- Emits `ZoneLoadedEvent` after successful loading
- Provides access to the current zone

**Key Methods:**
- `void loadZone(String zoneId)`: Loads a zone by ID
- `Zone getCurrentZone()`: Returns the active zone

**Execution Order:** 20

**Service Integration:**
The Tilemap is managed entirely by the ZoneService. Other systems (RenderSystem, CollisionSystem) query ZoneService to access the current Tilemap.

### ECS Components

#### ActiveSequenceComponent

Located in `engine.services.world.components.ActiveSequenceComponent`:

A stateful component that tracks sequence execution state:
- `String sequenceId`: ID of the sequence to execute
- `int currentIndex`: Current GameEvent being processed
- `float waitTimer`: Countdown for WAIT commands
- `boolean isBlocked`: Flag for long-running operations

The SequenceSystem acts upon entities with this component.

### Systems

#### TriggerSystem

Located in `engine.services.world.systems.TriggerSystem`:

**Purpose:** Observes game state and fires events from Trigger data.

**Behavior:**
1. Gets the current zone from ZoneService
2. Iterates through zone triggers
3. Checks if trigger conditions are met:
   - `ON_LOAD`: Fires after specified delay from zone load
   - `ON_ENTER_AREA`: Fires when player enters bounds (TODO)
   - `ON_INTERACT`: Fires on player interaction (TODO)
4. Executes trigger events (typically `START_SEQUENCE`)
5. Respects `isRepeatable` property

**Event Handling:**
- Listens for `ZoneLoadedEvent` to reset state
- Creates entities with `ActiveSequenceComponent` for sequence execution

#### SequenceSystem

Located in `engine.services.world.systems.SequenceSystem`:

**Purpose:** Interprets ActiveSequence components and executes GameEvent commands.

**Update Loop:**
1. Query entities with `ActiveSequenceComponent`
2. Skip if `waitTimer > 0` or `isBlocked == true`
3. Get current GameEvent using `sequenceId` and `currentIndex`
4. Execute event based on type:
   - **Instant actions** (PLAY_SOUND, TELEPORT_ENTITY): Execute and advance index
   - **Timed actions** (WAIT): Set waitTimer, don't advance
   - **Blocking actions** (MOVE_ENTITY, FADE_SCREEN): Set isBlocked, don't advance
5. Remove `ActiveSequenceComponent` when sequence completes

**Supported Event Types:**
- `WAIT`: Pauses sequence for specified duration
- `PLAY_SOUND`: Plays a sound (instant)
- `TELEPORT_ENTITY`: Instantly moves an entity (instant)
- `MOVE_ENTITY`: Moves an entity over time (blocking)
- `FADE_SCREEN`: Screen fade effect (blocking)

## Integration Pattern

### Application State Setup

The `ApplicationState` (e.g., `IntroCutsceneState`) is responsible for:

1. **Registering Systems** in `onEnter()`:
```java
worldService.addSystem(triggerSystem);
worldService.addSystem(sequenceSystem);
worldService.addSystem(movementSystem);
```

2. **Loading Zone**:
```java
zoneService.loadZone("intro_cutscene_zone");
```

3. **Cleanup** in `onExit()`:
```java
worldService.clearSystems();
```

### Data Flow

```
File → ZoneService → Zone (Data Model)
         ↓
Zone → TriggerSystem → ActiveSequence (Component)
         ↓
ActiveSequence → SequenceSystem → Other Systems/Services
```

### System Interaction

- **TriggerSystem** and **SequenceSystem** depend on **ZoneService** for data
- **SequenceSystem** communicates with:
  - Systems: By adding/removing ECS components
  - Services: By calling methods directly (e.g., `audioService.playSound()`)

## Testing

### Unit Tests

- `ActiveSequenceComponentTest`: Tests component initialization and state management
- `ZoneServiceIT`: Tests service injection and basic functionality

### Integration Tests

- `TriggerSystemTest`: Tests trigger conditions and event firing
  - ON_LOAD triggers with/without delay
  - Repeatable vs non-repeatable triggers
  - Multiple triggers in a zone

- `SequenceSystemTest`: Tests sequence execution
  - Wait events and timer management
  - Instant actions advancing index
  - Blocking actions setting isBlocked flag
  - Sequence completion and cleanup
  - Unknown event type handling

### Test Utilities

Located in `engine.services.zone` test package:
- `SimpleZone`: Test implementation of Zone
- `SimpleTrigger`: Test implementation of Trigger
- `SimpleGameEvent`: Test implementation of GameEvent
- `SimpleSequence`: Test implementation of Sequence

## Future Enhancements

1. **Zone File Loading**: Implement actual file parsing in `ZoneService.loadZone()`
2. **Trigger Conditions**: Complete implementation of:
   - `ON_ENTER_AREA`: Spatial triggers
   - `ON_INTERACT`: Interaction triggers
3. **Blocking Operation Resolution**: Implement checking for completion of blocking operations
4. **Additional Event Types**: Expand GameEvent type support based on game needs
5. **Tilemap Rendering**: Integrate with RenderSystem for tilemap visualization
6. **Collision Detection**: Use Tilemap properties for collision system

## Notes

- All interfaces are designed to be implemented by concrete classes that load from JSON or other formats
- The ZoneService currently has a placeholder for file loading
- The system is designed to be extended with additional trigger types and event types as needed
- Thread safety is not currently implemented but may be needed for async zone loading
