#!/bin/bash

# Script to create GitHub issues from TODO documents
# Source documents are in docs/development/ and docs/
# Run this script with: GH_TOKEN=<your-token> ./create-issues-from-todos.sh

set -e

REPO="TheStackTraceWhisperer/october"

echo "Creating GitHub issues for the October project..."
echo "Repository: $REPO"
echo ""

# Check if gh is installed and authenticated
if ! command -v gh &> /dev/null; then
    echo "Error: GitHub CLI (gh) is not installed"
    echo "Please install it from https://cli.github.com/"
    exit 1
fi

if ! gh auth status &> /dev/null; then
    echo "Error: Not authenticated with GitHub CLI"
    echo "Please run: gh auth login"
    echo "Or set GH_TOKEN environment variable"
    exit 1
fi

echo "✓ GitHub CLI authenticated"
echo ""

# Function to create an issue
create_issue() {
    local title="$1"
    local body="$2"
    local labels="$3"
    
    echo "Creating issue: $title"
    
    if [ -n "$labels" ]; then
        gh issue create --repo "$REPO" --title "$title" --body "$body" --label "$labels"
    else
        gh issue create --repo "$REPO" --title "$title" --body "$body"
    fi
    
    echo "✓ Issue created"
    echo ""
}

# ============================================================================
# ISSUES FROM docs/development/integration-todos.md
# ============================================================================

echo "Creating issues from docs/development/integration-todos.md..."
echo "=============================================="
echo ""

# Issue 1: Tilemap Rendering System
create_issue \
"Implement TilemapRenderSystem" \
"## Description
Implement a TilemapRenderSystem that draws visible tilemap layers with proper camera parallax support.

## Requirements
- Draw visible layers from bottom to top
- Apply camera parallax as needed for each layer
- Respect layer visibility and opacity settings
- Optimize rendering to only draw tiles visible in the camera viewport

## Context
From docs/development/integration-todos.md - High Priority item.

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
- [ ] Integration tests verify rendering behavior" \
"enhancement,high-priority,rendering"

# Issue 2: Tilemap Collision Strategy
create_issue \
"Choose and Implement Tilemap Collision Strategy" \
"## Description
Choose and implement a collision strategy for tilemap interactions.

## Options to Consider
1. **Static Collider Generation**: Generate static colliders at zone load time
2. **Query-based Collision**: Perform collision checks in CollisionSystem by querying tilemap data

## Requirements
- Decide on the collision strategy based on performance and flexibility needs
- Implement the chosen strategy
- Integrate with existing CollisionSystem
- Support collision properties from tilemap data

## Context
From docs/development/integration-todos.md - High Priority item.

## Related Components
- CollisionSystem
- ZoneService
- Tilemap data model

## Acceptance Criteria
- [ ] Collision strategy is chosen and documented
- [ ] Implementation is complete and integrated
- [ ] Collision detection works correctly with tilemap
- [ ] Performance is acceptable
- [ ] Tests verify collision behavior" \
"enhancement,high-priority,collision"

# Issue 3: Extend ZoneService for Tilemap Parsing
create_issue \
"Extend ZoneService to Parse Tilemap and Tileset Data" \
"## Description
Extend the ZoneService loader to parse tilemap and tileset data from zone files.

## Requirements
- Parse tilemap structure from JSON zone files
- Parse tileset definitions and tile data
- Load tile images and properties
- Validate tilemap data integrity
- Handle missing or malformed tilemap data gracefully

## Context
From docs/development/integration-todos.md - Medium Priority item.

## Related Components
- ZoneService
- Tilemap data model (Tile, Tileset, Tilelayer, Tilemap)
- Asset loading pipeline

## Acceptance Criteria
- [ ] ZoneService can parse complete tilemap data
- [ ] Tilesets are correctly loaded with tile definitions
- [ ] Tile images are loaded and cached
- [ ] Error handling for malformed data
- [ ] Tests verify parsing correctness" \
"enhancement,medium-priority,zone-loading"

# Issue 4: Create Real Zone with Tilemap
create_issue \
"Provide Real Zone Asset with Tilemap and Triggers" \
"## Description
Create at least one real zone asset with a complete tilemap and triggers to validate rendering and collision systems.

## Requirements
- Create a complete zone JSON file with:
  - Multi-layer tilemap
  - Tileset definitions
  - Trigger definitions
  - Sequence definitions
- Include varied tile types to test rendering
- Include collision tiles to test collision system
- Include triggers to test sequence system

## Context
From docs/development/integration-todos.md - Medium Priority item.

## Related Components
- Zone data format
- Tilemap assets
- Trigger system
- Sequence system

## Acceptance Criteria
- [ ] At least one complete zone asset created
- [ ] Zone includes multi-layer tilemap
- [ ] Zone includes functional triggers
- [ ] Zone can be loaded and rendered
- [ ] Zone demonstrates all major features" \
"enhancement,medium-priority,assets,zone-loading"

# Issue 5: UI Event Type System (Optional)
create_issue \
"Consider Switching UI Events to Typed Event Classes" \
"## Description
Evaluate and potentially implement a switch from string-based UI events to typed event classes for better type safety and refactoring support.

## Current State
- UI button events use string identifiers (e.g., \"START_NEW_GAME\")
- Event listeners match on string values

## Proposed Improvement
- Use typed event classes for UI events
- Improve compile-time type safety
- Better IDE support for refactoring
- Reduced risk of typos

## Context
From docs/development/integration-todos.md - Low Priority/Polish item.

## Trade-offs to Consider
- Increased complexity vs. type safety benefits
- Impact on serialization/deserialization
- Migration effort for existing UI definitions

## Acceptance Criteria
- [ ] Evaluate pros/cons of typed events
- [ ] If proceeding: Design typed event system
- [ ] If proceeding: Migrate existing UI events
- [ ] If proceeding: Update documentation
- [ ] If proceeding: Tests verify functionality" \
"enhancement,low-priority,polish,ui"

# Issue 6: Add Tests for Rendering and Zone Loading
create_issue \
"Add Tests for Fade Overlay Rendering and Zone JSON Loading" \
"## Description
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
From docs/development/integration-todos.md - Low Priority/Polish item.

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
- [ ] Tests are maintainable and clear" \
"enhancement,low-priority,testing,polish"

echo ""
echo "✓ All docs/development/integration-todos.md issues created"
echo ""

# ============================================================================
# ISSUES FROM docs/zone-tilemap-sequence.md
# ============================================================================

echo "Creating issues from docs/zone-tilemap-sequence.md..."
echo "================================================="
echo ""

# Issue 7: Complete Trigger Condition Implementations
create_issue \
"Complete Implementation of Trigger Condition Types" \
"## Description
Complete the implementation of trigger condition types beyond the current ON_LOAD support.

## Trigger Types to Implement

### ON_ENTER_AREA (Priority)
- Spatial trigger that fires when player enters defined bounds
- Requires collision detection or position checking
- Should support rectangular or polygonal areas
- Consider performance implications of checking multiple area triggers

### ON_INTERACT (Priority)
- Fires when player interacts with an entity or area
- Requires input handling integration
- Should support different interaction types (use/examine/talk)

### Additional Trigger Types (Optional)
- ON_EXIT_AREA: Fires when leaving an area
- ON_TIMER: Fires after elapsed time in zone
- ON_CONDITION: Fires when arbitrary game state condition is met

## Context
From docs/zone-tilemap-sequence.md - Future Enhancements section.

## Related Components
- TriggerSystem
- Trigger data model
- CollisionSystem (for spatial triggers)
- Input handling system (for interaction triggers)

## Acceptance Criteria
- [ ] ON_ENTER_AREA implemented and tested
- [ ] ON_INTERACT implemented and tested
- [ ] Integration tests verify trigger firing
- [ ] Documentation updated with trigger types
- [ ] Performance is acceptable with multiple triggers" \
"enhancement,high-priority,triggers,gameplay"

# Issue 8: Blocking Operation Completion Checking
create_issue \
"Implement Completion Checking for Blocking Sequence Operations" \
"## Description
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
From docs/zone-tilemap-sequence.md - Future Enhancements section.

## Related Components
- SequenceSystem
- ActiveSequenceComponent
- MoveToTargetSystem
- FadeService

## Acceptance Criteria
- [ ] Clear completion detection for all blocking operations
- [ ] Robust error handling for failed operations
- [ ] Tests verify completion detection
- [ ] Documentation explains blocking operation lifecycle" \
"enhancement,medium-priority,sequences"

# Issue 9: Expand GameEvent Type Support
create_issue \
"Expand GameEvent Type Support Based on Game Needs" \
"## Description
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
From docs/zone-tilemap-sequence.md - Future Enhancements section.

## Related Components
- SequenceSystem
- GameEvent data model
- Various systems depending on event type

## Acceptance Criteria
- [ ] Priority event types identified
- [ ] Event types implemented with proper blocking behavior
- [ ] Integration with relevant systems
- [ ] Tests for each new event type
- [ ] Documentation updated" \
"enhancement,medium-priority,sequences,gameplay"

# Issue 10: Async Zone Loading
create_issue \
"Implement Async Zone Loading with Thread Safety" \
"## Description
Implement asynchronous zone loading to prevent frame drops during zone transitions, with proper thread safety considerations.

## Current State
- Zone loading is synchronous
- Large zones may cause frame drops during load
- No thread safety considerations

## Requirements
- Load zone data asynchronously
- Show loading screen during zone load
- Handle thread safety for zone data access
- Ensure smooth transition when load completes
- Graceful error handling for failed loads

## Implementation Considerations
- Use CompletableFuture or similar async mechanism
- Protect shared state with proper synchronization
- Consider resource preloading strategy
- Implement progress reporting for large zones

## Context
From docs/zone-tilemap-sequence.md - Future Enhancements and Notes sections.

## Related Components
- ZoneService
- Zone data model
- Asset loading pipeline
- State management

## Acceptance Criteria
- [ ] Async zone loading implemented
- [ ] Thread safety verified
- [ ] Loading screen integration
- [ ] Error handling for failed loads
- [ ] Performance improvement measured
- [ ] Tests verify thread safety" \
"enhancement,low-priority,performance,zone-loading"

# Issue 11: Tilemap Rendering Integration with RenderSystem
create_issue \
"Integrate Tilemap Rendering with Main RenderSystem" \
"## Description
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
From docs/zone-tilemap-sequence.md - Future Enhancements section.

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
- [ ] Tests verify rendering integration" \
"enhancement,medium-priority,rendering"

# Issue 12: Tilemap-Based Collision Detection
create_issue \
"Implement Collision Detection Using Tilemap Properties" \
"## Description
Use tilemap properties to drive the collision detection system, enabling level geometry to be defined in tilemap data.

## Requirements
- Read collision properties from tilemap tiles
- Integrate collision data with CollisionSystem
- Support different collision types (solid, one-way, etc.)
- Optimize collision checks for performance
- Handle dynamic tilemap changes if needed

## Collision Property Examples
- Solid tiles block all movement
- One-way platforms (vertical only)
- Slopes and ramps
- Trigger areas
- Different collision layers

## Context
From docs/zone-tilemap-sequence.md - Future Enhancements section.

## Related Components
- CollisionSystem
- Tilemap data model
- Zone loading

## Acceptance Criteria
- [ ] Collision properties read from tilemap
- [ ] CollisionSystem uses tilemap collision data
- [ ] Different collision types supported
- [ ] Performance is acceptable
- [ ] Tests verify collision behavior
- [ ] Documentation explains collision property format" \
"enhancement,high-priority,collision,tilemap"

echo ""
echo "✓ All docs/zone-tilemap-sequence.md issues created"
echo ""

# ============================================================================
# SUMMARY
# ============================================================================

echo "=============================================="
echo "Issue Creation Complete!"
echo "=============================================="
echo ""
echo "Created issues from:"
echo "  - docs/development/integration-todos.md (6 issues)"
echo "  - docs/zone-tilemap-sequence.md (6 issues)"
echo ""
echo "Total: 12 GitHub issues"
echo ""
echo "Labels used:"
echo "  - enhancement (all issues)"
echo "  - high-priority (issues needing immediate attention)"
echo "  - medium-priority (important but not urgent)"
echo "  - low-priority (nice-to-have improvements)"
echo "  - rendering, collision, zone-loading, sequences, etc. (feature areas)"
echo ""
echo "Next steps:"
echo "  1. Review created issues in GitHub"
echo "  2. Assign issues to milestones if needed"
echo "  3. Prioritize and assign to team members"
echo "  4. Track progress in project board"
echo ""
