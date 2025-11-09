# October Engine Documentation

This directory contains comprehensive documentation for the October game engine.

## Quick Start

New to the project? Start here:
- See the root [README.md](../README.md) for build and run instructions
- [GitHub Issues Guide](github-issues/README.md) - Creating GitHub issues from TODOs

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

## Development

- [Integration TODOs](development/integration-todos.md) - Integration tasks and completion status
- [General TODOs](development/todos.md) - Ongoing development tasks

## GitHub Issues

- [GitHub Issues Documentation](github-issues/) - Tools and documentation for creating GitHub issues from TODOs

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
├── overview.puml                          # System diagram
├── development/                           # Development documentation
│   ├── integration-todos.md
│   └── todos.md
└── github-issues/                         # GitHub issues tools
    ├── README.md
    ├── QUICK_START_GUIDE.md
    ├── GITHUB_ISSUES_README.md
    ├── README_GITHUB_ISSUES.md
    ├── TODO_CONVERSION_SUMMARY.md
    └── ISSUE_ORGANIZATION_MAP.md
```

## Contributing

When adding documentation:
1. Place technical documentation in the appropriate category
2. Keep documentation concise and factual
3. Update this index when adding new files
4. Use markdown for all documentation
