---
title: "Add Tests for Fade Overlay Rendering and Zone JSON Loading"
labels: ["enhancement", "low-priority", "testing", "polish"]
---

## Description
Add comprehensive tests for fade overlay rendering path and zone JSON loading functionality.

## Requirements

### Fade Overlay Rendering Tests
- Test fade overlay system initialization
- Test fade state transitions
- Test overlay rendering with different opacity values
- Test shader integration for tinting

### Zone JSON Loading Tests
- Test successful zone loading from valid JSON
- Test error handling for malformed JSON
- Test error handling for missing zone files
- Test tilemap parsing
- Test trigger and sequence loading

## Context
From integration-todos.md - Low Priority/Polish item.

## Related Components
- FadeOverlaySystem
- ZoneService
- Zone JSON loader

## Acceptance Criteria
- [ ] Unit tests for FadeOverlaySystem
- [ ] Integration tests for fade rendering
- [ ] Unit tests for zone JSON parsing
- [ ] Integration tests for zone loading
- [ ] Edge cases are covered
- [ ] Tests are maintainable and clear
