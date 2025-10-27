---
title: "Integrate Tilemap Rendering with Main RenderSystem"
labels: ["enhancement", "medium-priority", "rendering"]
---

## Description
Complete the integration of tilemap visualization into the main rendering pipeline.

## Requirements
- Integrate TilemapRenderSystem with existing RenderSystem
- Ensure proper render order (tilemaps behind/between/in front of entities)
- Optimize rendering performance
- Support camera transformations
- Handle layer ordering correctly

## Rendering Pipeline Considerations
- Background layers render before entities
- Foreground layers render after entities
- Mid-ground layers may interleave with entities
- Lighting and effects should work with tilemaps

## Context
From zone-tilemap-sequence.md - Future Enhancements section.

## Related Components
- TilemapRenderSystem (to be implemented)
- RenderSystem
- Camera system
- ZoneService

## Acceptance Criteria
- [ ] Tilemap renders in correct order relative to entities
- [ ] Camera transformations apply correctly
- [ ] Performance is acceptable for large tilemaps
- [ ] Layers can be configured for render order
- [ ] Visual quality is good (no tearing, artifacts)
- [ ] Tests verify rendering integration
