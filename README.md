# October

A Java-based game engine built with LWJGL, Micronaut, and OpenGL.

## Structure

The project consists of two modules:

- **engine** - Core engine systems including rendering, audio, input, and game logic
- **application** - Game implementation using the engine

## Requirements

- Java 21
- Maven 3.x
- OpenGL 4.6 compatible graphics
- OpenAL audio library

## Building

```bash
mvn clean verify
```

## Running

```bash
mvn exec:java -pl application
```

## Testing

The project follows a testing pyramid strategy with unit tests and integration tests.

- Unit tests: `mvn test`
- Integration tests: `mvn verify`

## Code Style (Spotless)

Spotless is configured in informational mode and is not bound to the Maven lifecycle. It will not fail regular builds unless invoked explicitly.

- Check formatting (reports violations, does not modify files):
  ```bash
  mvn spotless:check
  ```
- Apply formatting fixes:
  ```bash
  mvn spotless:apply
  ```

The current configuration performs minimal, non-invasive checks (trim trailing whitespace, ensure newline at EOF) for Java sources and POM files.

## Key Technologies

- LWJGL for OpenGL and OpenAL bindings
- Micronaut for dependency injection
- JUnit 5 for testing
- Lombok for code generation
