# October Engine Documentation

This directory contains comprehensive documentation for the October game engine.

## Quick Start

New to the project? See the root [README.md](../README.md) for build and run instructions.

## Architecture & Design

- [Architecture Rules](ARCHITECTURE_RULES.md) - Architectural patterns and ArchUnit enforcement rules
- [Testing Strategy](TESTING_STRATEGY.md) - Test organization, unit tests, and integration tests
- [System Overview](overview.puml) - PlantUML diagram of system architecture

## Features & Implementation

### Sprite Sheets & Animation
- [Sprite Sheet Usage](SPRITE_SHEET_USAGE.md) - Complete guide to sprite sheets, animations, and directional sprites

### Tilemaps
- [Tilemap Implementation Summary](TILEMAP_IMPLEMENTATION_SUMMARY.md) - Overview of tilemap system implementation
- [Tilemap Configuration](tilemap-configuration.md) - Configuration format and usage
- [Zone, Tilemap, and Sequence Architecture](zone-tilemap-sequence.md) - Architecture for zones, tilemaps, triggers, and sequences

## Directory Structure

```
docs/
├── README.md                              # This file
├── ARCHITECTURE_RULES.md                  # Architecture and testing rules
├── TESTING_STRATEGY.md                    # Testing organization
├── SPRITE_SHEET_USAGE.md                  # Sprite sheet guide
├── TILEMAP_IMPLEMENTATION_SUMMARY.md      # Tilemap overview
├── tilemap-configuration.md               # Tilemap config
├── zone-tilemap-sequence.md               # Zone/tilemap/sequence architecture
└── overview.puml                          # System diagram
```

## Contributing

When adding documentation:
1. Place technical documentation in the appropriate category
2. Keep documentation concise and factual
3. Update this index when adding new files
4. Use markdown for all documentation
