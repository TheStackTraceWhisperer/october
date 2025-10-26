# GitHub Copilot Instructions for Professional Java Development

## Core Principles

* **Quality First:** Prioritize correctness, robustness, and maintainability over raw speed of implementation.
* **Test-Driven Mindset:** Every functional change or addition requires appropriate test coverage.
* **Readability & Clarity:** Code must be easily understandable by other developers.
* **Security & Performance:** Be mindful of potential security vulnerabilities and performance implications.

## General Java Standards

1.  **Code Formatting:** Adhere strictly to Google Java Format or the project's `.editorconfig` if present. Ensure proper indentation, bracing, and line length.
2.  **Naming Conventions:**
    * Classes/Interfaces: `PascalCase` (e.g., `UserService`, `OrderRepository`).
    * Methods/Variables: `camelCase` (e.g., `calculateTotal`, `userId`).
    * Constants: `SCREAMING_SNAKE_CASE` (e.g., `MAX_RETRIES`).
    * Packages: `lowercase.dotted.notation` (e.g., `com.example.app.service`).
3.  **Immutability:** Favor immutable objects where possible, especially for data transfer objects (DTOs) and configuration.
4.  **Error Handling:** Use specific checked exceptions for recoverable errors and unchecked exceptions for programming errors. Always catch and handle exceptions gracefully; avoid catching `Exception` directly unless re-throwing or logging with extreme care.
5.  **Logging:** Use SLF4J and a chosen logging implementation (e.g., Logback, Log4j2). Log at appropriate levels (`DEBUG`, `INFO`, `WARN`, `ERROR`). Never log sensitive information.
6.  **Dependency Injection:** Utilize a dependency injection framework (e.g., Spring, Guice) for managing component lifecycles and dependencies. Avoid direct instantiation of services within other services.

## Testing Requirements

* **Mandatory Test Coverage:**
    * All new public methods, services, and API endpoints MUST have corresponding **unit tests**.
    * Any interaction between major components (e.g., service layer to repository, controller to service) MUST have **integration tests**.
    * New features or significant bug fixes should include **acceptance/end-to-end tests** where applicable (e.g., using Spring Boot's test capabilities, WireMock for external services).
* **Test Frameworks:** Use JUnit 5 for unit and integration tests. Use Mockito for mocking dependencies. AssertJ is preferred for assertions over Hamcrest.
* **Test Location:** Tests for `src/main/java/com/example/package/ClassName.java` must be located at `src/test/java/com/example/package/ClassNameTest.java` or `ClassNameIntegrationTest.java`.
* **Test Isolation:** Unit tests must be independent and not rely on external services (databases, network calls). Use mocks or in-memory equivalents.
* **Clear Test Names:** Test method names should clearly describe the behavior being tested (e.g., `shouldReturnUserWhenIdIsValid`, `throwsNotFoundExceptionWhenUserDoesNotExist`).
* **Test Data:** Use dedicated test data that is representative but isolated from production data. Avoid hardcoding IDs or external state.

## Documentation Requirements

* **Javadoc:**
    * All public classes, interfaces, enums, constructors, and methods MUST have comprehensive Javadoc comments.
    * Explain the purpose, parameters (`@param`), return value (`@return`), and potential exceptions (`@throws`).
    * **Example:**
        ```java
        /**
         * Fetches a user by their unique identifier.
         *
         * @param userId The unique ID of the user to fetch. Must not be null.
         * @return The User object if found.
         * @throws UserNotFoundException If no user with the given ID exists.
         * @throws IllegalArgumentException If the userId is null or invalid.
         */
        public User getUserById(String userId) throws UserNotFoundException { ... }
        ```
* **External Library References:**
    * When utilizing a complex or non-standard function/API from an external library, include a comment directly above the code block with a link to its official documentation.
    * **Example:**
        ```java
        // Using Apache Commons Collections4: ListUtils.union()
        // See: [https://commons.apache.org/proper/commons-collections/javadocs/api-4.4/org/apache/commons/collections4/ListUtils.html#union-java.util.List-java.util.List-](https://commons.apache.org/proper/commons-collections/javadocs/api-4.4/org/apache/commons/collections4/ListUtils.html#union-java.util.List-java.util.List-)
        List<String> combinedList = ListUtils.union(listA, listB);
        ```
* **Internal API/Service Usage:** For calls to complex internal APIs or services, add a brief inline comment explaining the purpose of the call, especially if it's not immediately obvious from the method name.

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

* **Maven/Gradle:** Assume Maven or Gradle as the build tool.
* **Build Command:** Use `mvn clean install` (Maven) or `./gradlew build` (Gradle) for a full build.
* **Test Command:** Use `mvn test` (Maven) or `./gradlew test` (Gradle) for running tests.
* **Linter/Formatter:** If `maven-checkstyle-plugin` or `spotless-maven-plugin` (or their Gradle equivalents) are configured, ensure code adheres to their rules.

---