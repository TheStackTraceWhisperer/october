# GitHub Copilot Instructions for Professional Java Development

## Core Principles

* **Quality First:** Prioritize correctness, robustness, and maintainability over raw speed of implementation.
* **Test-Driven Mindset:** Every functional change or addition requires appropriate test coverage.
* **Readability & Clarity:** Code must be easily understandable by other developers.
* **Security & Performance:** Be mindful of potential security vulnerabilities and performance implications.

## General Java Standards

1.  **Code Formatting:** Follow standard Java conventions with consistent indentation, bracing, and reasonable line length. The project does not currently enforce a specific formatter (like Google Java Format or Spotless), so maintain consistency with existing code style.
2.  **Naming Conventions:**
    * Classes/Interfaces: `PascalCase` (e.g., `UserService`, `OrderRepository`).
    * Methods/Variables: `camelCase` (e.g., `calculateTotal`, `userId`).
    * Constants: `SCREAMING_SNAKE_CASE` (e.g., `MAX_RETRIES`).
    * Packages: `lowercase.dotted.notation` (e.g., `engine.services.rendering`).
3.  **Immutability:** Favor immutable objects where possible. Use:
    * Java records for simple data carriers with fields (e.g., `record HealthComponent(int maxHealth, int currentHealth) implements IComponent`)
    * Empty records for marker components (e.g., `record EnemyComponent() implements IComponent`)
    * Lombok's `@Getter` with private final fields for components requiring additional behavior
    * Immutable collections (e.g., `List.copyOf()`, `Collections.emptyList()`) when returning collections from interfaces
4.  **Error Handling:** Use specific exceptions for recoverable errors. Use unchecked exceptions (`IllegalArgumentException`, `NullPointerException`) for programming errors. Always catch and handle exceptions gracefully; avoid catching `Exception` directly unless re-throwing or logging with extreme care.
5.  **Logging:** Use SLF4J with Logback. Use Lombok's `@Slf4j` annotation for logger instances. Log at appropriate levels (`DEBUG`, `INFO`, `WARN`, `ERROR`). Never log sensitive information.
6.  **Dependency Injection:** This project uses **Micronaut** for dependency injection. Use:
    * `@Singleton` for service classes
    * `@RequiredArgsConstructor` (Lombok) for constructor injection
    * `jakarta.inject.Singleton` and related annotations
    * Avoid direct instantiation of services; let Micronaut manage the lifecycle

## Testing Requirements

* **Mandatory Test Coverage:**
    * All new public methods, services, and major components MUST have corresponding **unit tests**.
    * Any interaction between major components (e.g., service-to-service, system integration) MUST have **integration tests**.
    * New features or significant bug fixes should include integration tests where applicable.
* **Test Frameworks:** 
    * Use JUnit 5 for all tests (unit and integration)
    * Use Mockito for mocking dependencies
    * Use both JUnit assertions (`assertEquals`, `assertTrue`, `assertThrows`, etc.) and AssertJ (`assertThat`) based on readability and test complexity
    * Use `@MicronautTest` for integration tests that require dependency injection
* **Test Naming:**
    * **Unit Tests:** Name test classes `ClassNameTest.java` (e.g., `CameraTest`, `HealthComponentTest`)
    * **Integration Tests:** Name test classes `ClassNameIT.java` (e.g., `ZoneServiceIT`, `PlayerMovementIT`)
    * **Test Methods:** Use descriptive names with underscores for readability (e.g., `constructor_shouldSetMaxHealthAndCurrentHealth`, `takeDamage_shouldReduceCurrentHealth`)
* **Test Location:** Tests for `src/main/java/package/ClassName.java` must be located at `src/test/java/package/ClassNameTest.java` or `ClassNameIT.java`.
* **Test Isolation:** Unit tests must be independent and not rely on external services (databases, file system, network calls, GLFW windows, OpenGL context). Use mocks or skip tests that require external resources.
* **Test Data:** Use dedicated test data that is representative but isolated from production data. Avoid hardcoding IDs or external state.

## Documentation Requirements

* **Javadoc:**
    * All public interfaces, classes, enums, and their public methods SHOULD have Javadoc comments explaining their purpose.
    * Simple marker interfaces (e.g., `IComponent`) and simple records without fields (e.g., `record EnemyComponent()`) may omit class-level Javadoc if self-explanatory.
    * For non-trivial public methods, include:
        * Purpose description
        * Parameters (`@param`) with constraints/expectations
        * Return value (`@return`) with description
        * Exceptions (`@throws`) if applicable
    * **Example:**
        ```java
        /**
         * Loads a zone from the given zone ID by attempting to read a JSON file from classpath.
         * On success, sets it as current and publishes ZoneLoadedEvent.
         * If the resource is not found or fails to parse, falls back to a minimal BasicZone.
         * 
         * @param zoneId The unique identifier for the zone to load
         */
        public void loadZone(String zoneId) { ... }
        ```
* **Inline Comments:**
    * Use inline comments sparingly for complex logic that isn't self-evident from the code
    * Prefer self-documenting code (clear variable/method names) over excessive comments
    * When using external libraries with complex APIs, a brief comment or link to documentation may be helpful but is not mandatory
* **Code Clarity:**
    * Write code that is self-documenting through clear naming and structure
    * Comments should explain "why" not "what" when the "what" is already clear from the code

## Handling Ambiguity and Missing Information (`NEEDS_INFO` Protocol)

If you encounter a situation where crucial information (documentation, specific requirements, internal source code) is missing or unclear, **DO NOT proceed with assumptions**. Instead, follow this protocol:

1.  **Insert `TODO: [NEEDS_INFO]` comments:**
    * **Missing Documentation:** If you cannot locate official documentation for an external library or a critical internal API, insert a `TODO` comment.
        * **Format:** `// TODO: [NEEDS_INFO] Cannot find documentation for [Library/Function/Service]. Please provide a link or clear guidance for its usage.`
    * **Missing Internal Source/Context:** If you need to interact with an internal component but its source code or behavior is not discoverable, insert a `TODO` comment.
        * **Format:** `// TODO: [NEEDS_INFO] Required internal component [ComponentName/Functionality] is missing or unclear. Please provide the source code link or detailed specifications.`
    * **Unclear Requirements:** If the user story or issue description is ambiguous for a specific part of the implementation, insert a `TODO` comment.
        * **Format:** `// TODO: [NEEDS_INFO] Requirements for [SpecificFeature/Behavior] are unclear. Please clarify the expected outcome or logic.`
2.  **Generate a Minimal, Safe Implementation:** If absolutely necessary to compile, generate a minimal, safe, and typically non-functional implementation (e.g., throwing `UnsupportedOperationException`, returning `Optional.empty()`, or empty collections) for the part with missing information, clearly marked with the `NEEDS_INFO` comment.
3.  **Do NOT Guess:** Under no circumstances should you invent logic or external dependencies if the information is genuinely missing. It is better to halt and request clarification via the `NEEDS_INFO` flag.

## Build and Test Execution

* **Build Tool:** This project uses Maven.
* **Build Command:** Use `mvn clean install` for a full build including tests.
* **Compile Command:** Use `mvn clean compile` for compilation without tests.
* **Unit Test Command:** Use `mvn test` for running unit tests only.
* **Integration Test Command:** Use `mvn verify` for running both unit and integration tests.
* **Code Coverage:** JaCoCo is configured to generate coverage reports. Reports are available in `target/site/jacoco/` after running `mvn verify`.
* **Linter/Formatter:** The project does not currently use checkstyle or spotless plugins. Follow the existing code style and conventions in the codebase.
* **Java Version:** Java 21 is required for compilation and execution.

---