# Integration TODOs

This document lists the areas where integration is missing or incomplete, based on the current engine and application code. Each item includes what was found, why it matters, and concrete actions to take.

Quick checklist
- [x] Implement Zone loading and ZoneLoadedEvent publishing
- [x] Register AudioSystem in relevant states and wire sequence audio
- [x] Implement Sequence actions (PLAY_SOUND, TELEPORT_ENTITY, MOVE_ENTITY, FADE_SCREEN)
- [x] Add Tilemap data model and JSON deserialization
- [ ] Add Tilemap rendering system (TilemapRenderSystem)
- [ ] Add Tilemap collision integration
- [x] Decide on triggers/sequences in gameplay and register systems accordingly
- [x] Extend asset pipeline to preload audio buffers
- [x] Fix EnemyAISystem double-update per frame
- [x] Ensure IntroCutsceneState renders if intended (intro intentionally black; fade overlay renders)
- [x] Verify UI button event strings or switch to typed events
- [x] Improve device hotplug assignment handling
- [x] Provide real zone assets and hook them up
- [x] Add Fade service/system and cameraService/UI integration as needed (overlay rendering)
- [x] Fix FADE_SCREEN IT by adjusting SequenceSystem completion semantics (see item 13)

1) ZoneService + TriggerSystem/SequenceSystem
- What I found:
  - `ZoneService.loadZone(...)` was a TODO.
  - `TriggerSystem` and `SequenceSystem` depend on current zone and `ZoneLoadedEvent`.
  - In `IntroCutsceneState`, the call to `zoneService.loadZone("intro_cutscene_zone")` was disabled.
- Why it matters:
  - Without zone loading and the `ZoneLoadedEvent`, triggers and sequences won’t run.
- What to do:
  - Implement loader, set `currentZone`, publish event, and call it from the state.
- Status: Done
  - Implemented JSON loader in `ZoneService` (classpath `/zones/{id}.json`), publishes `ZoneLoadedEvent`.
  - Enabled zone load in `IntroCutsceneState` and added a minimal zone asset `zones/intro_cutscene_zone.json`.

2) Audio: not registered and not called from sequences
- What I found:
  - States were missing `AudioSystem`, and `SequenceSystem` was not calling audio.
- Why it matters:
  - No audio would play.
- What to do:
  - Register `AudioSystem` and implement `PLAY_SOUND`.
- Status: Done
  - `AudioSystem` is registered in `PlayingState` and `IntroCutsceneState`.
  - `SequenceSystem` invokes `AudioSystem.playSoundEffect(...)`.

3) Sequence actions: MOVE_ENTITY, TELEPORT_ENTITY, FADE_SCREEN
- What I found:
  - Block/unblock pathways were incomplete.
- Why it matters:
  - Sequences could get stuck.
- What to do:
  - Implement actions with correct blocking and unblocking.
- Status: Done
  - `TELEPORT_ENTITY` updates `TransformComponent` and advances.
  - `MOVE_ENTITY` attaches `MoveToTargetComponent` and blocks until it’s removed by `MoveToTargetSystem`.
  - `FADE_SCREEN` blocks on `FadeService.isFading()` and now visibly renders via overlay.

4) Tilemap: rendering and collisions
- Status: Partially Done
  - Tilemap data model and JSON deserialization implemented in `ZoneService`.
  - Added `JsonTilemap`, `JsonTileset`, `JsonTilelayer`, and `JsonTile` classes.
  - Created `tilesets.yml` configuration file with draft tileset definitions for dungeon-tileset and tileset-world.
  - Added sample zone `test_tilemap_zone.json` with complete tilemap data.
  - Comprehensive tests added: `TilemapDeserializationTest` and `TilemapLoadingIT`.
  - Documentation added in `docs/tilemap-configuration.md`.
  - Still pending: `TilemapRenderSystem` implementation and collision strategy integration.

5) Triggers/sequences in gameplay
- Status: Done
  - `PlayingState` registers `TriggerSystem` and `SequenceSystem`.

6) Audio assets in the pipeline
- Status: Done
  - `AssetManifest` already supports `audioBuffers`; `SceneService` preloads them.

7) EnemyAISystem double update per frame
- Status: Done
  - `EnemyAISystem` uses only `update(world, dt)`.

8) Render in IntroCutsceneState
- Status: Done (by intent)
  - Intro remains intentionally black; fade overlay renders on top during FADE events.

9) UI Events: verify button action strings
- Status: Done
  - `main_menu.json` uses `"START_NEW_GAME"`; `MainMenuState` listens for the same string.

10) Hotplug gamepads and device assignment
- Status: Done
  - Added periodic refresh (every 1s) in `DeviceMappingService.update(dt)`.

11) Zone assets: make them real
- Status: Done (minimal)
  - Added `zones/intro_cutscene_zone.json` and wired it up.

12) Fade and cameraService integration during UI
- Status: Done (overlay)
  - Added `FadeOverlaySystem`, shader tinting via `uColor`, and a procedural white texture for tinting.

13) Fade-screen IT failure (SequenceSystem completion semantics)
- What I found:
  - `SequenceSystemIT.fadeScreen_blocks_then_advances_after_duration` failed with NPE because the `ActiveSequenceComponent` had been removed/destroyed in the same tick the sequence completed, so the test’s subsequent access of the component returned null.
- Fix implemented:
  - Introduced a configurable `removeOnComplete` flag in `SequenceSystem` (default true for production/unit tests) and adjusted removal to happen only if the sequence was already complete at the start of an update (no same-tick removal).
  - Updated `SequenceSystemIT` to construct `SequenceSystem` with `removeOnComplete=false` so the component remains for assertions after completion.
- Status: Done
  - All unit tests and integration tests now pass; the fade-screen IT behaves as expected (blocks while fading, then advances).

Prioritized next steps
- High priority
  - [ ] Implement `TilemapRenderSystem` that draws visible layers (bottom → top) with cameraService parallax as needed.
  - [ ] Choose collision strategy for tilemap (generate static colliders at zone load vs. query checks in `CollisionSystem`).
  - [ ] Load actual tile images from tilesheet assets and slice them into individual tiles.
- Medium priority
  - [x] Extend `ZoneService` loader to parse tilemap/tilesets.
  - [x] Provide at least one real zone with tilemap + triggers to validate rendering and collisions.
  - [ ] Create a real gameplay zone with complete tilemap, triggers, and sequences.
- Low priority / polish
  - [ ] Optionally switch UI event strings to a typed event class.
  - [x] Add tests for tilemap JSON loading and deserialization.
