---
title: "Expand GameEvent Type Support Based on Game Needs"
labels: ["enhancement", "medium-priority", "sequences", "gameplay"]
---

## Description
Expand the set of supported GameEvent types to enable richer cutscenes and scripted sequences.

## Currently Supported Events
- WAIT: Pause sequence
- PLAY_SOUND: Play audio
- TELEPORT_ENTITY: Instant entity movement
- MOVE_ENTITY: Animated entity movement
- FADE_SCREEN: Screen fade effect

## Potential New Event Types

### High Priority
- SHOW_DIALOG: Display dialog/text box
- WAIT_FOR_INPUT: Wait for player confirmation
- SET_CAMERA_TARGET: Change camera focus
- SPAWN_ENTITY: Create new entity

### Medium Priority
- PLAY_ANIMATION: Trigger entity animation
- CHANGE_ZONE: Transition to different zone
- SET_VARIABLE: Modify game state
- CONDITIONAL: Branch based on condition

### Low Priority
- CAMERA_SHAKE: Screen shake effect
- PARTICLE_EFFECT: Visual effects
- MODIFY_ENTITY: Change entity properties
- ENABLE_SYSTEM/DISABLE_SYSTEM: Toggle systems

## Context
From zone-tilemap-sequence.md - Future Enhancements section.

## Related Components
- SequenceSystem
- GameEvent data model
- Various systems depending on event type

## Acceptance Criteria
- [ ] Priority event types identified
- [ ] Event types implemented with proper blocking behavior
- [ ] Integration with relevant systems
- [ ] Tests for each new event type
- [ ] Documentation updated
