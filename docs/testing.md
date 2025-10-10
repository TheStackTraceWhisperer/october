# October Engine Test Specifications

This document outlines a comprehensive set of test specifications for the game engine. Tests are categorized as either **Unit** or **Integration** tests. All integration tests must be implemented using the `EngineTestHarness` to ensure a consistent and valid testing environment with an active OpenGL context.

---
## Core Engine & Services

### Engine Lifecycle
- [ ] **Integration**: The engine should initialize and shut down without errors.
  -   Create a simple integration test class that extends `EngineTestHarness`.
  -   The test method should be empty; the setup (`@BeforeAll`) and teardown (`@AfterAll`) methods of the harness will handle engine initialization and shutdown. The test passes if no exceptions are thrown.
- [ ] **Integration**: All core services (e.g., `WindowService`, `WorldService`, `RenderingService`) should be successfully injected and available within the test context.
  -   Within an `EngineTestHarness` test, inject all major services using `@Inject`.
  -   Assert that none of the injected services are null.

### Application Loop Policy
- [ ] **Integration**: The `standard` policy should stop when the window closes.
  -   In an `EngineTestHarness` test, get the window handle from the `Engine`.
  -   Assert the policy returns `true`.
  -   Set the window to close via `GLFW.glfwSetWindowShouldClose(windowHandle, true)`.
  -   Assert the policy now returns `false`.
- [ ] **Unit**: The `frames` policy should run for a specified number of frames.
  -   Instantiate `frames(5)`.
  -   Call `continueRunning` in a loop, asserting it returns `true` for frames 0-4 and `false` for frame 5.
- [ ] **Integration**: The `timed` policy should run for a specified duration.
  -   In an `EngineTestHarness` test, instantiate `timed(Duration.ofMillis(100))`.
  -   Assert the policy returns `true`.
  -   Sleep the thread for more than 100ms.
  -   Assert the policy now returns `false`.
- [ ] **Unit**: The `all` and `any` policies should correctly apply logical AND/OR to their sub-policies.
  -   Create mock `ApplicationLoopPolicy` instances that return predefined boolean sequences.
  -   Combine them with `all` and `any` and assert that the output matches the expected logical result.

### State Management (`ApplicationStateService`)
- [ ] **Unit**: Should push a new state onto the stack and call its `onEnter` method.
  -   Mock `ApplicationContext` and `ApplicationState`.
  -   Call `pushState` and verify that the stack size increases and the mock state's `onEnter` method was called.
- [ ] **Unit**: Should pop the current state from the stack and call its `onExit` method.
  -   Push a mock state onto the service.
  -   Call `popState` and verify that the stack size decreases and the mock state's `onExit` method was called.
- [ ] **Unit**: Should change the current state, calling `onExit` on the old state and `onEnter` on the new one.
  -   Push an initial mock state.
  -   Call `changeState` with a new mock state.
  -   Verify that the old state's `onExit` and the new state's `onEnter` were called in the correct order.

### Event System (`EventPublisherService`)
- [ ] **Unit**: Should publish an event to the underlying Micronaut event publisher.
  -   Create a mock `ApplicationEventPublisher`.
  -   Inject it into the `EventPublisherService`.
  -   Call `publish` with a sample event object.
  -   Verify that the mock publisher's `publishEvent` method was called with the correct event object.

### Plugin System (`PluginService`)
- [ ] **Unit**: Should call the `start` method on all registered plugins.
  -   Create a list of mock `IPlugin` objects.
  -   Instantiate `PluginService` with the mock plugins.
  -   Call the service's `start` method.
  -   Verify that the `start` method was called on each mock plugin.
- [ ] **Unit**: Should call the `stop` method on all registered plugins.
  -   (Similar setup to the start test) Call the service's `stop` method and verify `stop` was called on each mock plugin.

### Time Service (`SystemTimeService`)
- [ ] **Unit**: Should correctly calculate delta time between updates.
  -   Call `start`.
  -   Call `update`.
  -   Sleep the thread for a known duration (e.g., 16ms).
  -   Call `update` again.
  -   Assert that `getDeltaTimeSeconds` returns a value close to 0.016.

---
## Entity-Component-System

### World (`WorldService`)
- [ ] **Unit**: Should create entities with unique, sequential IDs.
- [ ] **Unit**: Should correctly add, retrieve, and remove components from entities.
- [ ] **Unit**: Should correctly report whether an entity has a specific component.
- [ ] **Unit**: Should return a list of all entities that have a given set of components.
- [ ] **Unit**: Should remove all of an entity's components when the entity is destroyed.
  -   For all the above unit tests, instantiate a `WorldService` directly. No mocks are needed as the service and its internal managers (`EntityManager`, `ComponentManager`) are self-contained.
  -   Perform actions (e.g., `createEntity`, `addComponent`) and then use assertion methods to verify the state (e.g., `hasComponent`, `getEntitiesWith`).

### Scene Loading (`SceneService`)
- [ ] **Unit**: Should successfully load a valid scene file and create the specified entities and components.
  -   Create a mock `WorldService` and `AssetCacheService`.
  -   Provide a sample `main_menu.json` file in the test resources.
  -   Call `sceneService.load(...)`.
  -   Verify that `worldService.createEntity` and `worldService.addComponent` were called the expected number of times with the correct component data.
- [ ] **Unit**: Should throw an `IllegalStateException` if `initialize` is not called before `load`.
- [ ] **Unit**: Should throw an `IOException` if the scene file is not found.
- [ ] **Integration**: Should successfully load a scene with real assets.
  -   In an `EngineTestHarness` test, load a simple scene that references real textures available in test resources.
  -   Assert that entities are created in the `WorldService`.

---
## Rendering

### Shader
- [ ] **Integration**: Should successfully compile and link a valid vertex and fragment shader.
- [ ] **Integration**: Should throw a `RuntimeException` if the vertex or fragment shader has a compile error.
- [ ] **Integration**: Should successfully set `mat4` and `vec3` uniforms.
  -   All shader tests must be integration tests extending `EngineTestHarness` to have access to a live OpenGL context.
  -   Load shader sources from test resource files.
  -   Use `assertDoesNotThrow` for valid shaders and `assertThrows` for invalid ones.

### Mesh & Texture
- [ ] **Integration**: Should successfully create a `Mesh` from vertex data.
- [ ] **Integration**: Should successfully load a valid PNG file into a `Texture`.
- [ ] **Integration**: Should throw a `RuntimeException` when attempting to load a corrupted or invalid image file.
  -   These tests require an OpenGL context and must extend `EngineTestHarness`.

### Core Renderer (`RenderingService`)
- [ ] **Integration**: Should initialize and be able to begin and end a scene without OpenGL errors.
- [ ] **Integration**: Should be able to submit a mesh and texture for rendering without OpenGL errors.
  -   In an `EngineTestHarness` test, get the injected `RenderingService`.
  -   Create a dummy `Camera`, `Mesh`, and `Texture`.
  -   Call `beginScene`, `submit`, and `endScene` and assert no exceptions are thrown.

### UI Renderer (`UIRendererService`)
- [ ] **Integration**: Should correctly set up an orthographic projection based on window size.
  -   In an `EngineTestHarness` test, get the injected `UIRendererService`.
  -   Check the service's internal camera projection matrix after initialization and after a resize event to ensure it's an ortho matrix matching the window dimensions.
- [ ] **Integration**: Should render a UI element without OpenGL errors.
  -   Create an entity with `UITransformComponent` and `UIImageComponent`.
  -   Call the `uiRenderer.submit()` method within a `begin()`/`end()` block.
  -   Assert that no exceptions are thrown.

---
## Game Systems

### Audio System
- [ ] **Integration**: Should play, pause, and stop a looping music track.
  -   Extend `EngineTestHarness`. Create an entity with a `MusicComponent`.
  -   Load a real (short) OGG file from test resources.
  -   Call the `AudioSystem.update()` method multiple times.
  -   Check the `isPlaying` and `isPaused` flags on the component and the underlying `AudioSource` to verify state transitions.
- [ ] **Integration**: Should play a one-shot sound effect and remove the component after it finishes.
  -   Create an entity with a `SoundEffectComponent`.
  -   After several updates, assert that the `SoundEffectComponent` has been removed from the entity.

### Movement & Collision
- [ ] **Integration**: An entity should move correctly based on its `ControllableComponent` state.
  -   Extend `EngineTestHarness`. Create an entity with `TransformComponent`, `ControllableComponent`, and `MovementStatsComponent`.
  -   Set `wantsToMoveUp = true` on the component.
  -   Instantiate and run the `MovementSystem`.
  -   Assert that the entity's Y position has increased.
- [ ] **Integration**: Diagonal movement should be normalized to prevent a speed increase.
  -   Set `wantsToMoveUp` and `wantsToMoveRight` to true.
  -   Run the `MovementSystem`.
  -   Check the magnitude of the movement vector to ensure it's not greater than the entity's speed.
- [ ] **Integration**: Should detect an overlap and revert the position of a movable entity when it collides with a wall.
  -   Create two entities: a "player" and a "wall", both with `TransformComponent` and `ColliderComponent`. Position them to overlap.
  -   Run the `CollisionSystem`.
  -   Assert that the player's position has been reverted to its `previousPosition`.

### AI & Player Input
- [ ] **Integration**: An enemy entity should move horizontally over time.
  -   Create an entity with `EnemyComponent` and `TransformComponent`.
  -   Instantiate and run the `EnemyAISystem` over several frames (mocking `SystemTimeService` to control time).
  -   Assert that the enemy's X position changes in an oscillating pattern.
- [ ] **Integration**: Player input should correctly update the `ControllableComponent`.
  -   Use a mock `InputMappingService` to simulate player input.
  -   Create an entity with a `ControllableComponent`.
  -   Run the `PlayerInputSystem`.
  -   Assert that the `wantsToMove...` flags on the component are set correctly based on the mock input.

### UI System (`UISystem`)
- [ ] **Unit**: Should correctly calculate the screen bounds of a UI element based on its anchor, pivot, and size.
  -   Create a `UITransformComponent` with known values.
  -   Instantiate `UISystem` with mock dependencies.
  -   Call the private `calculateLayout` method (or a public test wrapper).
  -   Assert that the `screenBounds` array in the component contains the correct pixel coordinates.
- [ ] **Integration**: A `UIButtonComponent` should change its state to `HOVERED` when the mouse is over it.
  -   Create an entity with `UITransformComponent` and `UIButtonComponent`.
  -   Use a mock `InputService` to set the mouse position directly over the button's calculated bounds.
  -   Run the `UISystem`.
  -   Assert that the button's `currentState` is `HOVERED`.
- [ ] **Integration**: A `UIButtonComponent` should fire its `actionEvent` when clicked.
  -   Set up the button as above.
  -   Use a mock `EventPublisherService` to capture events.
  -   Simulate a mouse press and release over the button using the mock `InputService`.
  -   Run the `UISystem` for multiple frames to capture the state changes (hover -> pressed -> hover).
  -   Verify that the mock `EventPublisherService` captured the correct `actionEvent` string.
