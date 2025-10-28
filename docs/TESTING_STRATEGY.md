# Project Testing Strategy

This document outlines the official testing strategy for the October engine and application. Adhering to this strategy is crucial for maintaining code quality, reliability, and maintainability.

## Guiding Principle: The Testing Pyramid

Our methodology is based on the classic Testing Pyramid model. The goal is to have many fast, isolated unit tests at the base, a smaller number of integration tests to verify component collaboration, and very few (if any) full end-to-end tests.

```
      / \\
     /   \\   <-- (Few) End-to-End Tests
    /-----\\\
   /       \\  <-- (More) Integration Tests
  /---------\\\
 /           \\ <-- (Lots) Unit Tests
/-------------\\\
```

This structure provides the best balance of feedback speed, test reliability, and confidence in the codebase.

---

## 1. Unit Tests (The Foundation)

Unit tests form the base of our pyramid. They are the first line of defense against bugs.

*   **Scope:** A single class or a single method (`unit`). All external dependencies (other services, file system, network, OpenGL context, etc.) **must** be mocked or stubbed.
*   **Goal:** To verify the correctness of a specific piece of logic in complete isolation.
*   **What to Test:**
    *   **Execution Branches:** Every `if`, `else`, `for`, `while`, and `switch` path.
    *   **Negative Cases:** How the unit behaves with invalid input (e.g., `null` arguments, empty lists, negative numbers).
    *   **Edge Cases:** Boundary conditions (e.g., zero, `Integer.MAX_VALUE`, empty strings).
    *   **Error Handling:** Correct exception throwing and `try-catch` logic.
*   **Speed:** Must be very fast. The entire unit test suite should run in seconds.
*   **Location:** `src/test/java`
*   **Execution:** Run via `mvn test` (using the Maven Surefire plugin).

---

## 2. Integration Tests (The Middle Layer)

Integration tests verify that different parts of the system work together as intended.

*   **Scope:** A feature slice or a complete use case involving multiple, real components working together. This can include a running Micronaut context, interaction between services, or systems running in a live engine context.
*   **Goal:** To verify that independently developed components collaborate correctly to produce the desired outcome.
*   **What to Test:**
    *   **Happy Paths:** The expected, successful flow of a feature from start to finish (e.g., player presses a button -> a state transition occurs -> a new scene is loaded).
    *   **Failure Paths:** How the integrated system behaves when one component fails or returns an error (e.g., a texture file is missing, a service returns an unexpected value).
    *   **Data Flow:** Correctness of data passing between services, systems, and components.
*   **Speed:** Slower than unit tests. It's acceptable for these to take longer as they may involve starting an application context or a live engine via the `EngineTestHarness`.
*   **Location:** `src/test/java`, typically using a `*IT.java` naming convention (e.g., `SceneLoadingIT.java`).
*   **Execution:** Run via `mvn verify` (using the Maven Failsafe plugin).

### Headless Environment Handling

Some integration tests require a display/graphics environment (e.g., tests that initialize GLFW or OpenGL). These tests will fail in headless environments (such as CI builds without Xvfb).

To handle this:

*   **Use `@EnabledIfDisplayAvailable` annotation**: Tests that extend `EngineTestHarness` are automatically marked with this annotation and will be skipped in headless environments.
*   **Behavior in headless mode**: Tests requiring a display are automatically detected and skipped with a clear message indicating the reason.
*   **Behavior with display**: Tests run normally when a display server is available (local development, Docker with Xvfb, CI with virtual display).
*   **Example**: When running `mvn verify` on a developer's machine without Xvfb, integration tests requiring GLFW will be skipped rather than failing.

**Note**: The CI environment is configured to run tests in Docker with Xvfb, ensuring all integration tests execute in the CI pipeline.

---

## 3. Distinguishing Unit from Integration Test Contexts

It's crucial to understand that not every class can or should be subjected to strict unit testing (where *all* external dependencies are mocked). Some components, by their very nature, are designed to interact directly with external systems that are difficult or impossible to mock in isolation (e.g., native libraries like GLFW, file systems, network sockets).

For such components:

*   **If a class's primary responsibility is to act as a direct interface to an unmockable external system**, it is often more appropriate to test its functionality at the **integration test level**. In these cases, the "external dependency" is the real system it interacts with, and the test verifies the correct interaction with that real system.
*   **The goal shifts from isolating logic to verifying the correct bridge/interaction with the external dependency.** This means that while the component itself might be a "unit" in terms of its codebase, its *test context* must be an integration one to provide meaningful validation.
*   **Avoid over-mocking:** Do not attempt to mock static native calls or complex external system behaviors if it leads to brittle, unrealistic, or overly complicated unit tests. Instead, embrace the integration test level for these specific components.

This clarification ensures that we apply the most effective testing strategy for each component, balancing isolation with realistic validation of system interactions.

---

This strategy ensures we have a robust, maintainable, and well-tested application.