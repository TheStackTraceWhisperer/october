# Engine Test Harness

The `EngineTestHarness` is an abstract base class designed to bootstrap a live, fully-functional game engine within a JUnit 5 test environment. Tests that require interaction with engine components that depend on a live OpenGL context (e.g., shaders, textures, windowing) should extend this class.

## Purpose

The primary goal of the harness is to provide a consistent and efficient testing environment that mirrors the running application without executing the main game loop. It handles the complexities of:
- Initializing the Micronaut dependency injection container.
- Creating and managing a valid OpenGL context and window.
- Providing test-specific configurations to control engine behavior.
- Ensuring clean shutdown of all resources after tests are complete.

## How It Works

The harness leverages a combination of JUnit 5 and Micronaut Test features to create a high-performance testing foundation.

### Micronaut Context Management

```java
@MicronautTest(packages = "engine")
```

This annotation instructs the Micronaut Test framework to start a full `ApplicationContext` before any tests run. By specifying `packages = "engine"`, we ensure that all beans and configurations within the `engine` module are discovered and wired correctly.

### Test Instance Lifecycle

```java
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
```

This crucial JUnit 5 annotation changes the default test lifecycle. Instead of creating a new instance of the test class for every test method, JUnit will create only a **single instance** and reuse it. This allows us to use non-static `@BeforeAll` and `@AfterAll` methods, which can operate on fields injected by Micronaut.

### Engine Lifecycle Management

```java
@BeforeAll
void setUp() {
  engine.init();
}

@AfterAll
void tearDown() {
  engine.shutdown();
}
```

By using class-level lifecycle annotations, the expensive operations of initializing the engine (creating the window and OpenGL context) and shutting it down happen only **once per test class**, not once per test method. This dramatically improves test execution speed.

### Test-Specific Configuration

The nested `TestConfiguration` factory uses Micronaut's `@Primary` annotation to override production beans with test-specific doubles:

1.  **`ApplicationLoopPolicy`**: It provides an `ApplicationLoopPolicy.skip()` bean. This allows the engine to complete its entire `init()` sequence but prevents it from entering the `while` loop in the `run()` method, so tests can execute immediately without getting stuck.

2.  **`GameState`**: It provides a `TestGameState` bean as the `@Primary @Named("initial")` state. This overrides the application's production initial state, giving tests a predictable and controlled entry point. This is the ideal place to load test-specific assets (e.g., textures, models, sounds) that are required for the tests to run.

## How to Use

1.  Create a new test class and make it `extend EngineTestHarness`.
2.  Inject any required engine services (like `Engine`, `WindowService`, etc.) using `@Inject`. They will be fully initialized and ready to use.
3.  Write your `@Test` methods.

### Example

```java
class MyServiceIT extends EngineTestHarness {

  @Inject
  private WindowService windowService;

  @Test
  void windowShouldBeActive() {
    // The 'engine' field is inherited and initialized from the harness
    assertThat(engine).isNotNull();
    assertThat(windowService.getHandle()).isNotZero();
  }
}
```