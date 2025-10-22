# Integration TODOs

This document lists the areas where integration is missing or incomplete, based on the current engine and application code. Each item includes what was found, why it matters, and concrete actions to take.

Quick checklist
- [ ] Implement Zone loading and ZoneLoadedEvent publishing
- [ ] Register AudioSystem in relevant states and wire sequence audio
- [ ] Implement Sequence actions (PLAY_SOUND, TELEPORT_ENTITY, MOVE_ENTITY, FADE_SCREEN)
- [ ] Add Tilemap rendering and collisions integration
- [ ] Decide on triggers/sequences in gameplay and register systems accordingly
- [ ] Extend asset pipeline to preload audio buffers
- [ ] Fix EnemyAISystem double-update per frame
- [ ] Ensure IntroCutsceneState renders if intended (register rendering/UI systems)
- [ ] Verify UI button event strings or switch to typed events
- [ ] Improve device hotplug assignment handling
- [ ] Provide real zone assets and hook them up
- [ ] Add Fade service/system and camera/UI integration as needed

1) ZoneService + TriggerSystem/SequenceSystem
- What I found:
  - `ZoneService.loadZone(...)` is a TODO (does not set `currentZone` or publish `ZoneLoadedEvent`).
  - `TriggerSystem` and `SequenceSystem` both depend on `ZoneService.getCurrentZone()`, and `TriggerSystem` listens for `ZoneLoadedEvent` to reset internal state.
  - In `IntroCutsceneState`, the call to `zoneService.loadZone("intro_cutscene_zone")` is commented out.
- Why it matters:
  - Without zone loading and the `ZoneLoadedEvent`, triggers and sequences won’t run.
- What to do:
  - Implement `ZoneService.loadZone(String zoneId)` to deserialize a zone (tilemap, triggers, sequences), set `currentZone`, and publish `new ZoneLoadedEvent(currentZone)` via `EventPublisherService`.
  - Un-comment `zoneService.loadZone(...)` in `IntroCutsceneState` and point it to a real zone asset.

2) Audio: not registered and not called from sequences
- What I found:
  - `PlayingState` registers: `PlayerInputSystem`, `MovementSystem`, `EnemyAISystem`, `CollisionSystem`, `RenderSystem`, `UISystem`. No `AudioSystem`.
  - `IntroCutsceneState` registers `TriggerSystem`, `SequenceSystem`, `MovementSystem`. No `AudioSystem` or `RenderSystem`.
  - `SequenceSystem` has TODOs for `PLAY_SOUND`, but doesn’t call `AudioSystem`/`AudioService`.
- Why it matters:
  - No audio will play in gameplay or cutscenes; sequence events won’t trigger sound.
- What to do:
  - Register `AudioSystem` in states that should have audio (e.g., `PlayingState`, `IntroCutsceneState`).
  - Inject `AudioSystem` (or a thin `AudioFacade`) into `SequenceSystem` and implement `PLAY_SOUND` using `AssetCacheService` to resolve buffer handles and `AudioSystem.playSoundEffect(...)` to attach/trigger the sound.

3) Sequence actions: MOVE_ENTITY, TELEPORT_ENTITY, FADE_SCREEN
- What I found:
  - `SequenceSystem` has TODOs for these and sets `blocked = true` without a path to unblock.
- Why it matters:
  - Narrative sequences won’t execute movement/teleport/fade; blocked sequences will get stuck.
- What to do:
  - `TELEPORT_ENTITY`: Find target (by tag or ID in properties), update `TransformComponent` position; increment currentIndex.
  - `MOVE_ENTITY`: Introduce a `MoveToTargetComponent` and a new `MoveToTargetSystem` that moves entities toward a goal; `SequenceSystem` adds the component and waits for its removal to clear `blocked`.
  - `FADE_SCREEN`: Add a `FadeSystem` or lightweight `FadeService` that renders a fullscreen quad via `UIRendererService` (or `RenderingService`); `SequenceSystem` triggers `startFade` and waits until completion to unblock.

4) Tilemap: rendering and collisions
- What I found:
  - There are tilemap data classes (`Tilemap`, `Tilelayer`, `Tileset`), but `RenderSystem` only draws sprites. `CollisionSystem` only handles entity-vs-entity AABB.
- Why it matters:
  - Zones with tilemaps won’t render; walls from tilemaps won’t collide with entities.
- What to do:
  - Tilemap rendering: add a `TilemapRenderSystem` (or extend `RenderSystem`) to draw tile layers using `Camera`.
  - Collisions vs tilemap: generate colliders at scene/zone load time for solid tiles (preferred for ECS), or add tilemap collision checks in `CollisionSystem`.

5) Triggers/sequences in gameplay
- What I found:
  - `TriggerSystem` and `SequenceSystem` are added only in `IntroCutsceneState`, not in `PlayingState`.
- Why it matters:
  - In-game triggers won’t run during gameplay.
- What to do:
  - Decide if triggers/sequences should be active during gameplay. If yes, register both in `PlayingState` and ensure a zone is loaded.

6) Audio assets in the pipeline
- What I found:
  - `SceneService.AssetManifest` loads textures and meshes only; no audio buffers.
- Why it matters:
  - If scenes/zones reference audio, there’s no preload path; `PLAY_SOUND` may fail to resolve buffers.
- What to do:
  - Extend `AssetManifest` (e.g., `audioBuffers`) and add corresponding loader calls (to `AssetCacheService.loadAudioBuffer`) in `SceneService.loadAssets`.
  - Reference these handles in `MusicComponent`/`SoundEffectComponent` or sequence event properties.

7) EnemyAISystem double update per frame
- What I found:
  - `EnemyAISystem` implements both `update(World)` and `update(World, float)`. The engine calls both phases each frame; AI moves twice.
- Why it matters:
  - Incorrect AI movement speed.
- What to do:
  - Remove `update(World)` in `EnemyAISystem`; use only `update(World, float)`.
  - Alternatively, adjust `SystemManager` to use one update phase (broader change, not recommended right now).

8) Render in IntroCutsceneState
- What I found:
  - `IntroCutsceneState` clears screen to black but doesn’t register `RenderSystem`. If intro should be visual, nothing draws.
- Why it matters:
  - Intro zones or sequences won’t be visible.
- What to do:
  - If the intro is visual, register `RenderSystem` (and `UISystem` if needed) in `IntroCutsceneState`. If it’s intentionally black, keep as-is.

9) UI Events: verify button action strings
- What I found:
  - `UISystem` publishes `button.actionEvent` (a String). `MainMenuState` listens for a String `"START_NEW_GAME"`.
- Why it matters:
  - If scene config uses a different string, transitions won’t fire.
- What to do:
  - Ensure main menu scene’s `UIButtonComponent.actionEvent` is exactly `"START_NEW_GAME"`, or switch to a typed event class to avoid string mismatches.

10) Hotplug gamepads and device assignment
- What I found:
  - `DeviceMappingService` refreshes assignments at start only; no per-frame hotplug refresh.
- Why it matters:
  - Plug/unplug after startup won’t update bindings.
- What to do:
  - Refresh assignments periodically (e.g., on a timer) or detect changes (poll `glfwJoystickPresent`/`glfwJoystickIsGamepad`) and rebind.

11) Zone assets: make them real
- What I found:
  - Scenes exist (e.g., `/scenes/playing-scene.json`, `/scenes/main_menu.json`). Zone IDs in `IntroCutsceneState` aren’t loaded, and `ZoneService` has no loader.
- Why it matters:
  - Triggers/sequences depend on actual zone content and `ZoneLoadedEvent`.
- What to do:
  - Provide actual zone JSON files in resources, wire `ZoneService.loadZone` to load them, and call it from states that need zones.

12) Fade and camera integration during UI
- What I found:
  - Camera resize is wired in `PlayingState`; `UISystem` uses `WindowService` for size. No fade service yet.
- Why it matters:
  - Sequences calling `FADE_SCREEN` need a rendering path.
- What to do:
  - Create a minimal `FadeService`/`FadeSystem` that renders a fullscreen quad via `UIRendererService`/`RenderingService`. Expose `startFade`/`isFadeComplete` for `SequenceSystem` to unblock.

Prioritized next steps
- High priority
  - [ ] Implement `ZoneService.loadZone` and publish `ZoneLoadedEvent`; un-comment load in `IntroCutsceneState`.
  - [ ] Register `AudioSystem` in `PlayingState` and `IntroCutsceneState`.
  - [ ] Fix `EnemyAISystem` to use only `update(world, dt)`.
  - [ ] Implement `PLAY_SOUND` in `SequenceSystem` by invoking `AudioSystem`.

- Medium priority
  - [ ] Implement `TELEPORT_ENTITY` and `MOVE_ENTITY` (add `MoveToTargetSystem` and component).
  - [ ] Add `FadeSystem` and integrate `FADE_SCREEN` in sequences.
  - [ ] Add a `TilemapRenderSystem` and tilemap collision checks or collider generation.

- Low priority / polish
  - [ ] Extend `AssetManifest` to include audio buffers and load them in `SceneService`.
  - [ ] Add hotplug-aware device assignment in `DeviceMappingService`.
  - [ ] Consider typed UI events instead of raw String.

Acceptance criteria (per item)
- Zone loading: Loading a zone sets `currentZone` and fires `ZoneLoadedEvent`; `TriggerSystem` logs at least one processed trigger when configured.
- Audio system: Registering `AudioSystem` does not break update loop; `SequenceSystem` PLAY_SOUND audibly plays a test buffer.
- Sequence actions: TELEPORT moves entity immediately; MOVE_ENTITY moves and then unblocks; FADE_SCREEN blocks until fade completes.
- Tilemap: Rendering shows a visible layer; collisions stop player/enemy against walls.
- Enemy AI: Enemies no longer move twice per frame.
- UI events: Clicking the Start button moves from Main Menu to Playing state.

Diagram updates (after implementation)
- Add edges:
  - `SequenceSystem` → `AudioSystem`
  - `SequenceSystem` → `FadeSystem`/`FadeService`
  - `SequenceSystem` → `MoveToTargetSystem` (if added)
  - `TilemapRenderSystem` connections as appropriate
- Optionally document zone asset loader relationship once implemented.

