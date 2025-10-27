---
title: "Implement TilemapRenderSystem"
labels: ["enhancement", "high-priority", "rendering"]
---

## Description
Implement a TilemapRenderSystem that draws visible tilemap layers with proper camera parallax support.

## Requirements
- Draw visible layers from bottom to top
- Apply camera parallax as needed for each layer
- Respect layer visibility and opacity settings
- Optimize rendering to only draw tiles visible in the camera viewport

## Context
From integration-todos.md - High Priority item.

## Related Components
- ZoneService (provides current zone and tilemap data)
- Camera system (for viewport and parallax calculations)
- Rendering pipeline

## Acceptance Criteria
- [ ] TilemapRenderSystem renders all visible tilemap layers
- [ ] Layers are rendered in correct order (bottom to top)
- [ ] Camera parallax is correctly applied
- [ ] Only visible tiles are rendered (viewport culling)
- [ ] Layer opacity is respected
- [ ] Integration tests verify rendering behavior
