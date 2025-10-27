# GitHub Issue Templates from TODO Documents

This directory contains 12 pre-formatted GitHub issue templates created from the TODO documents in the repository.

## How to Use These Templates

### Option 1: Copy-Paste into GitHub Web UI
1. Go to https://github.com/TheStackTraceWhisperer/october/issues/new
2. Open one of the template files below
3. Copy the content (excluding the YAML frontmatter `---` lines)
4. Paste into the GitHub issue description
5. Add the labels specified in the frontmatter
6. Submit the issue

### Option 2: Use the Automated Script
Run the `create-issues-from-todos.sh` script in the repository root to create all issues automatically. See `GITHUB_ISSUES_README.md` for details.

## Issue Templates

### From integration-todos.md

#### High Priority (2 issues)
- **01-implement-tilemap-render-system.md**
  - Labels: `enhancement`, `high-priority`, `rendering`
  - Implement TilemapRenderSystem with camera parallax and viewport culling

- **02-tilemap-collision-strategy.md**
  - Labels: `enhancement`, `high-priority`, `collision`
  - Choose and implement collision strategy for tilemaps

#### Medium Priority (2 issues)
- **03-extend-zoneservice-tilemap-parsing.md**
  - Labels: `enhancement`, `medium-priority`, `zone-loading`
  - Extend ZoneService to parse tilemap and tileset data

- **04-real-zone-asset-with-tilemap.md**
  - Labels: `enhancement`, `medium-priority`, `assets`, `zone-loading`
  - Create real zone asset with complete tilemap and triggers

#### Low Priority (2 issues)
- **05-typed-ui-events.md**
  - Labels: `enhancement`, `low-priority`, `polish`, `ui`
  - Consider switching UI events to typed event classes

- **06-tests-fade-zone-loading.md**
  - Labels: `enhancement`, `low-priority`, `testing`, `polish`
  - Add tests for fade overlay rendering and zone JSON loading

### From zone-tilemap-sequence.md

#### High Priority (2 issues)
- **07-complete-trigger-conditions.md**
  - Labels: `enhancement`, `high-priority`, `triggers`, `gameplay`
  - Complete implementation of ON_ENTER_AREA and ON_INTERACT triggers

- **12-tilemap-collision-detection.md**
  - Labels: `enhancement`, `high-priority`, `collision`, `tilemap`
  - Implement collision detection using tilemap properties

#### Medium Priority (3 issues)
- **08-blocking-operation-completion.md**
  - Labels: `enhancement`, `medium-priority`, `sequences`
  - Implement completion checking for blocking sequence operations

- **09-expand-gameevent-types.md**
  - Labels: `enhancement`, `medium-priority`, `sequences`, `gameplay`
  - Expand GameEvent type support for richer cutscenes

- **11-tilemap-rendering-integration.md**
  - Labels: `enhancement`, `medium-priority`, `rendering`
  - Integrate tilemap rendering with main RenderSystem

#### Low Priority (1 issue)
- **10-async-zone-loading.md**
  - Labels: `enhancement`, `low-priority`, `performance`, `zone-loading`
  - Implement async zone loading with thread safety

## Priority Summary

- **High Priority (4 issues)**: Critical for core tilemap functionality
  - Issues: 01, 02, 07, 12
  
- **Medium Priority (5 issues)**: Important enhancements and integrations
  - Issues: 03, 04, 08, 09, 11
  
- **Low Priority (3 issues)**: Polish and performance improvements
  - Issues: 05, 06, 10

## Feature Area Summary

- **Rendering (3 issues)**: 01, 11, and related to 04
- **Collision (2 issues)**: 02, 12
- **Zone Loading (4 issues)**: 03, 04, 10, and related to 06
- **Triggers & Sequences (3 issues)**: 07, 08, 09
- **Testing & Quality (1 issue)**: 06
- **UI/UX (1 issue)**: 05

## Dependencies

Some issues have dependencies on others:

```
03 (ZoneService parsing)
  ├── 04 (Real zone asset) - needs parsing support
  └── 12 (Tilemap collision) - needs parsed tilemap data

01 (TilemapRenderSystem)
  └── 11 (Rendering integration) - needs TilemapRenderSystem to exist

02 (Collision strategy)
  └── 12 (Tilemap collision) - may inform collision implementation
```

## Recommended Implementation Order

### Phase 1: Foundation (High Priority)
1. Issue 03 - Extend ZoneService tilemap parsing
2. Issue 01 - Implement TilemapRenderSystem
3. Issue 02 - Choose collision strategy

### Phase 2: Core Features (High Priority + Medium)
4. Issue 12 - Tilemap collision detection
5. Issue 04 - Real zone asset
6. Issue 07 - Complete trigger conditions
7. Issue 11 - Tilemap rendering integration

### Phase 3: Enhanced Functionality (Medium Priority)
8. Issue 08 - Blocking operation completion
9. Issue 09 - Expand GameEvent types

### Phase 4: Polish (Low Priority)
10. Issue 06 - Add tests
11. Issue 05 - Typed UI events (optional)
12. Issue 10 - Async zone loading

## Notes

- All issues include detailed acceptance criteria
- Each issue is self-contained and actionable
- Labels are suggested but can be adjusted based on your workflow
- Feel free to add milestones, assignees, and projects as needed
- Original TODO documents remain in the repository for reference
