---
title: "Implement Completion Checking for Blocking Sequence Operations"
labels: ["enhancement", "medium-priority", "sequences"]
---

## Description
Improve the sequence system's handling of blocking operations by implementing proper completion checking mechanisms.

## Current State
- Blocking operations (MOVE_ENTITY, FADE_SCREEN) set isBlocked flag
- Sequence waits for external systems to clear the flag
- Completion detection could be more robust

## Requirements
- Implement clear completion detection for all blocking operations
- Consider callback-based or polling-based approaches
- Ensure sequence system can reliably detect when to unblock
- Handle edge cases (cancelled operations, failures)

## Blocking Operation Types
- MOVE_ENTITY: Wait for entity to reach target
- FADE_SCREEN: Wait for fade to complete
- Future operations: WAIT_FOR_INPUT, WAIT_FOR_ANIMATION, etc.

## Context
From zone-tilemap-sequence.md - Future Enhancements section.

## Related Components
- SequenceSystem
- ActiveSequenceComponent
- MoveToTargetSystem
- FadeService

## Acceptance Criteria
- [ ] Clear completion detection for all blocking operations
- [ ] Robust error handling for failed operations
- [ ] Tests verify completion detection
- [ ] Documentation explains blocking operation lifecycle
