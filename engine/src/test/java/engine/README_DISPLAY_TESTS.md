# Display-Dependent Test Infrastructure

This directory contains test infrastructure for handling integration tests that require a display/graphics environment (GLFW/OpenGL).

## Problem

Some integration tests require GLFW initialization, which fails in headless environments (environments without a display server). This causes build failures on developer machines without Xvfb or in misconfigured CI environments.

## Solution

We use custom JUnit 5 conditional test execution to automatically skip display-dependent tests in headless environments while allowing them to run when a display is available.

## Components

### `@EnabledIfDisplayAvailable` Annotation

A custom JUnit 5 annotation that marks tests (or test classes) as requiring a display/graphics environment.

**Usage:**
```java
@EnabledIfDisplayAvailable
public class MyGraphicsTest {
    // Tests requiring GLFW/OpenGL
}
```

Or on individual test methods:
```java
public class MyTest {
    @Test
    @EnabledIfDisplayAvailable
    void testGraphicsFeature() {
        // Test requiring GLFW/OpenGL
    }
}
```

### `DisplayAvailableCondition` Class

The JUnit 5 `ExecutionCondition` that determines whether a display is available by attempting to initialize GLFW.

**Behavior:**
- Attempts to initialize GLFW once per test run
- If successful, tests are enabled
- If initialization fails, tests are skipped with a descriptive message
- GLFW is immediately terminated after the check to avoid holding resources

### `EngineTestHarness` Base Class

All integration tests that require a live engine (with GLFW/OpenGL context) should extend this class. The class is annotated with `@EnabledIfDisplayAvailable`, so all tests extending it are automatically skipped in headless environments.

**Example:**
```java
public class MyEngineIT extends EngineTestHarness {
    @Test
    void testEngineFeature() {
        // This test automatically skips in headless environments
        engine.init();
        // ... test logic
    }
}
```

## Test Results

### With Display Available
- All integration tests run normally
- Example: Local development on Linux/Mac/Windows with X11/Wayland/native display

### Without Display (Headless)
- Tests requiring display are skipped
- Clear messages explain why tests were skipped
- Build succeeds instead of failing
- Example: Developer machine without Xvfb, partial CI setup

### CI Environment
- Docker container with Xvfb provides virtual display
- All integration tests execute normally
- Full test coverage is maintained

## Benefits

1. **Stable Builds**: No more build failures due to missing display
2. **Better Developer Experience**: Developers can run builds locally without complex display setup
3. **Flexible Testing**: Tests run when possible, skip gracefully when not
4. **Clear Communication**: Test reports clearly indicate skipped tests and reasons
5. **No Code Changes Needed**: Existing tests just need to extend `EngineTestHarness`

## Troubleshooting

### All integration tests are being skipped

**Cause**: GLFW cannot initialize, indicating no display is available.

**Solutions**:
- On Linux: Install and configure Xvfb, then run tests with `xvfb-run mvn verify`
- On Mac/Windows: Ensure you have a display session running
- In CI: Verify Docker container has Xvfb configured and running

### Tests fail with "Unable to initialize GLFW"

**Cause**: Test is not using the conditional execution infrastructure.

**Solution**: Ensure the test class extends `EngineTestHarness` or is annotated with `@EnabledIfDisplayAvailable`.

### Want to force tests to run in headless mode

Currently, there's no override mechanism. Integration tests requiring GLFW must have a display available. Consider whether the test truly requires GLFW initialization or could be refactored as a unit test with mocked dependencies.
