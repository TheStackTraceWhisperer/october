# Proposed Test Utilities & Fixtures

This document outlines a collection of recommended test fixtures and helper methods. The goal is to abstract common test setup and logic, reduce boilerplate code, and improve the clarity and maintainability of our unit and integration tests.

---
## 1. Entity & World Fixtures

- [ ] **Entity Factory Methods:** Create fully-formed entities with a single method call.
  - [ ] `createControllablePlayer(world, position)`: Creates an entity with `TransformComponent`, `ControllableComponent`, `MovementStatsComponent`, and `ColliderComponent`.
  - [ ] `createStaticWall(world, position, size)`: Creates an entity with `TransformComponent` and `ColliderComponent`.
  - [ ] `createBasicEnemy(world, position)`: Creates an entity with `TransformComponent`, `EnemyComponent`, and `MovementStatsComponent`.
  - [ ] `createCamera(world, position, isPrimary)`: Creates a camera entity with `TransformComponent` and `CameraComponent`.

- [ ] **Component Factory Methods:** Create components with sensible default values.
  - [ ] `defaultTransform()`: Returns a `TransformComponent` at the origin `(0,0,0)`.
  - [ ] `kinematicCollider(size)`: Returns a `ColliderComponent` configured for a movable object.
  - [ ] `staticCollider(size)`: Returns a `ColliderComponent` configured for a static, immovable object.

- [ ] **Custom World Assertions:** Make tests more readable by providing domain-specific assertion methods.
  - [ ] `assertEntityExists(world, entityId)`: Asserts that an entity with the given ID is present in the world.
  - [ ] `assertHasComponents(world, entityId, Class<? extends IComponent>... components)`: Asserts that an entity has all the specified component types.
  - [ ] `assertEntityPosition(world, entityId, Vector2f expectedPosition)`: Asserts that an entity's `TransformComponent` is at a specific position.
  - [ ] `assertEntityCount(world, expectedCount, Class<? extends IComponent> component)`: Asserts that a specific number of entities with a given component exist.

---
## 2. UI & Input Fixtures

- [ ] **UI Entity Factory Methods:**
  - [ ] `createButton(world, anchor, pivot, text, actionEvent)`: Creates a UI entity with `UITransformComponent` and `UIButtonComponent`.
  - [ ] `createImage(world, anchor, pivot, textureHandle)`: Creates a UI entity with `UITransformComponent` and `UIImageComponent`.

- [ ] **Input Simulation Helpers:** These methods wrap a mocked `InputService` to simulate user actions.
  - [ ] `simulateMousePosition(mockInputService, x, y)`: Configures the mock to return specific mouse coordinates.
  - [ ] `simulateMouseButtonPress(mockInputService, button)`: Configures the mock to report that a mouse button is currently pressed.
  - [ ] `simulateMouseButtonRelease(mockInputService, button)`: Configures the mock to report that a mouse button is not pressed.
  - [ ] `simulateMouseClick(mockInputService, button, x, y)`: A convenience method that combines the above to simulate a full click at a position.

- [ ] **Custom UI Assertions:**
  - [ ] `assertButtonState(world, buttonEntityId, expectedState)`: Asserts that a `UIButtonComponent` is in the expected state (e.g., `HOVERED`, `PRESSED`).
  - [ ] `assertEventPublished(mockEventPublisher, expectedEvent)`: Verifies that a specific event string was published by the `EventPublisherService`.

---
## 3. System & Time Fixtures

- [ ] **System Execution Helpers:**
  - [ ] `runSystem(system, world, deltaTime)`: A simple wrapper to call the `update` method of a single system.
  - [ ] `runSystemForFrames(system, world, frameCount, deltaTime)`: Runs a system's update method in a loop for a specified number of frames.

- [ ] **Time Control Helpers:** For use with a mocked `SystemTimeService`.
  - [ ] `advanceTime(mockTimeService, duration)`: Advances the mocked game time by a specific amount, allowing for predictable testing of time-based logic.

---
## 4. Rendering & Asset Fixtures

- [ ] **Dummy Asset Creators:** These methods should load minimal, valid assets from the `test/resources` directory.
  - [ ] `createTestMesh(assetCache, handle)`: Loads or creates a simple procedural mesh (e.g., a quad) and registers it with the asset cache.
  - [ ] `createTestTexture(assetCache, handle)`: Loads a simple, valid texture file (e.g., a 1x1 white pixel PNG) and registers it.
  - [ ] `createTestShader(assetCache, handle)`: Loads a basic, valid vertex/fragment shader pair.

---
## 5. Centralized Mock Configuration

- [ ] **Modify `EngineTestHarness`:** The `@Factory` inside `EngineTestHarness.java` should be modified to provide default, mock implementations for cross-cutting services.
  - [ ] Provide a `@Singleton @Primary` mock for `InputService`.
  - [ ] Provide a `@Singleton @Primary` mock for `EventPublisherService`.
  - [ ] Provide a `@Singleton @Primary` mock for `SystemTimeService`.

- [ ] **Clarification on Mocking Strategy:** This approach **does not** prevent testing of the real components. It provides convenience for the majority of tests while allowing for targeted testing of the real implementations when needed.
  - **Default Behavior:** For most integration tests (e.g., testing UI, game logic), the `@Primary` mock is automatically injected. This allows tests to easily simulate inputs and verify outputs without depending on real hardware.
  - **Testing the Real Component:** To test a real implementation (e.g., `GlfwInputService`), create a new, dedicated test class that extends `EngineTestHarness`.
  - **Overriding the Mock:** Inside that specific test class, create a nested `@Factory` class. Within that factory, define a `@Singleton` bean method that is annotated with `@Replaces(TheServiceToReplace.class)`. This tells Micronaut to use your real implementation instead of the primary mock for the scope of that one test class.
