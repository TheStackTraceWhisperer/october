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

## Key Technologies

- LWJGL for OpenGL and OpenAL bindings
- Micronaut for dependency injection
- JUnit 5 for testing
- Lombok for code generation
