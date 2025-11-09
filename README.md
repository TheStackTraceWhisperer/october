# October

A Java-based 2D game engine with entity-component-system architecture.

## Project Structure

- **engine** - Core systems: rendering, audio, input, collision, entity management
- **application** - Game implementation and demo

## Requirements

- Java 21
- Maven 3.x
- OpenGL 4.6 compatible graphics card
- OpenAL audio library

## Build and Run

Build the project:
```bash
mvn clean verify
```

Run the application:
```bash
mvn exec:java -pl application
```

Run tests:
```bash
mvn test              # Unit tests
mvn verify            # All tests including integration
```

## Code Formatting

Check code formatting:
```bash
mvn spotless:check    # Report violations only
mvn spotless:apply    # Apply formatting fixes
```

## Technologies

- **LWJGL** - OpenGL and OpenAL bindings
- **Micronaut** - Dependency injection framework
- **JUnit 5** - Testing framework
- **Lombok** - Code generation

## Documentation

See the [docs/](docs/) directory for detailed documentation:
- Architecture and design patterns
- Testing strategy
- Sprite sheets and animations
- Tilemap system
- Development guides
