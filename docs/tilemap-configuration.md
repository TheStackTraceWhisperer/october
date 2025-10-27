# Tilemap Configuration Guide

This document describes how to configure tilemaps in zone JSON files and the tilesets.yml configuration.

## Overview

The tilemap system provides a multi-layered tile-based rendering solution for zones. Each zone can have an optional tilemap that defines the visual layout of the level using tilesets and layers.

## Tilesets Configuration (tilesets.yml)

The `tilesets.yml` file defines the available tilesets and their properties. This file serves as documentation and reference for tileset properties.

### Structure

```yaml
tilesets:
  - name: "tileset-name"
    sourceImage: "image-file.png"
    tileWidth: 16
    tileHeight: 16
    columns: 6
    rows: 5
    tiles:
      - id: 0
        properties:
          type: "floor"
          collision: false
      - id: 1
        properties:
          type: "wall"
          collision: true
```

### Fields

- **name**: Unique identifier for the tileset
- **sourceImage**: Path to the tilesheet image (relative to `textures/ENVIRONMENT/tilesets/`)
- **tileWidth**: Width of each tile in pixels
- **tileHeight**: Height of each tile in pixels
- **columns**: Number of tile columns in the tilesheet
- **rows**: Number of tile rows in the tilesheet
- **tiles**: List of tile definitions with properties

### Tile Properties

Each tile can have arbitrary properties stored as key-value pairs:

- **type**: Semantic type of the tile (e.g., "floor", "wall", "water")
- **collision**: Boolean indicating if the tile should block movement
- Custom properties can be added as needed

## Zone Tilemap Configuration

Tilemaps are defined within zone JSON files in the `tilemap` field.

### Example Zone with Tilemap

```json
{
  "id": "example_zone",
  "name": "Example Zone",
  "tilemap": {
    "width": 10,
    "height": 10,
    "tileWidth": 16,
    "tileHeight": 16,
    "tilesets": [
      {
        "name": "dungeon-tileset",
        "tileWidth": 16,
        "tileHeight": 16,
        "tiles": [
          {
            "id": 0,
            "properties": {
              "type": "floor",
              "collision": false
            }
          },
          {
            "id": 6,
            "properties": {
              "type": "wall",
              "collision": true
            }
          }
        ]
      }
    ],
    "tilelayers": [
      {
        "name": "background",
        "width": 10,
        "height": 10,
        "visible": true,
        "opacity": 1.0,
        "tileIds": [
          [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
          [0, 1, 1, 1, 1, 1, 1, 1, 1, 0],
          [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        ]
      },
      {
        "name": "walls",
        "width": 10,
        "height": 10,
        "visible": true,
        "opacity": 1.0,
        "tileIds": [
          [6, 6, 6, 6, 6, 6, 6, 6, 6, 6],
          [6, -1, -1, -1, -1, -1, -1, -1, -1, 6],
          [6, 6, 6, 6, 6, 6, 6, 6, 6, 6]
        ]
      }
    ],
    "properties": {
      "theme": "dungeon"
    }
  },
  "sequences": [],
  "triggers": [],
  "properties": {}
}
```

### Tilemap Fields

- **width**: Map width in tile units
- **height**: Map height in tile units
- **tileWidth**: Width of each tile in pixels
- **tileHeight**: Height of each tile in pixels
- **tilesets**: Array of tileset definitions used in this map
- **tilelayers**: Array of tile layers (rendered bottom to top)
- **properties**: Optional map-level metadata

### Tileset Definition (in Zone)

Each tileset in the zone's tilemap includes:

- **name**: Name matching a tileset in tilesets.yml
- **tileWidth**: Tile width in pixels
- **tileHeight**: Tile height in pixels
- **tiles**: Array of tile definitions with IDs and properties

Note: The `sourceImage` is not included in zone JSON as it will be loaded from the asset system.

### Tilelayer Definition

Each layer represents a 2D grid of tiles:

- **name**: Layer identifier (e.g., "background", "foreground", "walls")
- **width**: Layer width in tile units (should match tilemap width)
- **height**: Layer height in tile units (should match tilemap height)
- **visible**: Whether this layer should be rendered (default: true)
- **opacity**: Layer opacity from 0.0 to 1.0 (default: 1.0)
- **tileIds**: 2D array of tile IDs, where -1 or 0 represents empty tiles

### Tile ID Format

The `tileIds` array is a 2D array where:
- Each row represents a horizontal line of tiles
- Each element is a tile ID from the tileset
- `-1` or `0` typically represents an empty/transparent tile
- Positive integers reference specific tiles by their ID

## Design Patterns

The tilemap implementation follows the same patterns as Sequence and Zone:

1. **Immutable Interfaces**: All data models (Tile, Tileset, Tilelayer, Tilemap) are defined as interfaces
2. **JSON Deserialization**: Concrete implementations are nested in `ZoneService` as `JsonTile`, `JsonTileset`, etc.
3. **Defensive Copying**: Lists are returned using `List.copyOf()` to prevent modification
4. **Null Safety**: Empty collections are returned instead of null

## Available Tilesets

Based on the current asset files:

### dungeon-tileset.png
- Dimensions: 96x80 pixels
- Tile size: 16x16 pixels
- Grid: 6 columns x 5 rows (30 tiles)
- Theme: Dungeon/interior environments

### tileset-world.png
- Dimensions: 112x272 pixels
- Tile size: 16x16 pixels
- Grid: 7 columns x 17 rows (119 tiles)
- Theme: Outdoor/world environments

## Future Enhancements

1. **Tilemap Rendering**: Integration with RenderSystem for visualization
2. **Collision Detection**: Use tile properties for collision system
3. **Image Loading**: Load actual tile images from tilesheet assets
4. **Tile Slicing**: Automatically slice tilesheet images into individual tiles
5. **Layer Parallax**: Support for parallax scrolling on different layers
6. **Animated Tiles**: Support for tile animation sequences
