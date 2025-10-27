# Architectural Rules and Testing

This document describes the architectural rules enforced in the October engine using ArchUnit. These rules ensure code quality, maintainability, and adherence to best practices.

## ArchUnit Overview

ArchUnit is a testing library that allows us to check architectural characteristics of our Java codebase. It runs as part of the normal test suite, ensuring that architectural violations are caught early in the development process.

## Enforced Rules

### 1. Service-System Access Model

**Rule:** Service classes must NOT depend on system classes.

**Location:** `engine.services..**` (excluding `engine.services.world.systems..**`)  
**Constraint:** Cannot depend on `engine.services.world.systems..**`

**Rationale:**
- Services are higher-level components that provide infrastructure and functionality to the engine
- Systems (ECS systems in `engine.services.world.systems`) are game logic components that operate on entities
- Systems should depend on services, not vice versa
- This maintains a clear dependency hierarchy and prevents circular dependencies

**Example:**
```java
// ✅ GOOD: System depends on Service
@Prototype
public class AudioSystem implements ISystem {
    private final AudioService audioService;  // System can depend on service
    // ...
}

// ❌ BAD: Service depends on System
@Singleton
public class AudioService {
    private final AudioSystem audioSystem;  // Service should NOT depend on system
    // ...
}
```

### 2. No Java Util Logging

**Rule:** Classes should not use `java.util.logging`.

**Rationale:**
- The project uses SLF4J with Logback for logging
- Using multiple logging frameworks can lead to configuration issues and inconsistent behavior
- SLF4J provides a better API and more flexible configuration

**Example:**
```java
// ✅ GOOD: Using SLF4J with Lombok
@Slf4j
public class MyService {
    public void doSomething() {
        log.info("Doing something");
    }
}

// ❌ BAD: Using java.util.logging
public class MyService {
    private static final Logger logger = Logger.getLogger(MyService.class.getName());
    public void doSomething() {
        logger.info("Doing something");
    }
}
```

### 3. No Access to Standard Streams

**Rule:** Classes should not access `System.out` or `System.err`.

**Rationale:**
- Standard streams bypass the logging framework
- Logging through SLF4J provides better control, formatting, and output management
- Standard stream output is difficult to test and cannot be controlled by configuration

**Example:**
```java
// ✅ GOOD: Using logger
@Slf4j
public class MyService {
    public void processData() {
        log.debug("Processing data");
    }
}

// ❌ BAD: Using System.out
public class MyService {
    public void processData() {
        System.out.println("Processing data");
    }
}
```

### 4. No Field Injection

**Rule:** Classes should not use field injection.

**Rationale:**
- Constructor injection is preferred as it makes dependencies explicit
- Constructor injection enables immutability
- Constructor injection makes testing easier (no need for reflection to set fields)
- Constructor injection prevents `NullPointerException` from uninitialized fields

**Example:**
```java
// ✅ GOOD: Constructor injection with Lombok
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class MyService {
    private final DependencyA dependencyA;
    private final DependencyB dependencyB;
}

// ❌ BAD: Field injection
@Singleton
public class MyService {
    @Inject
    private DependencyA dependencyA;
    
    @Inject
    private DependencyB dependencyB;
}
```

### 5. Service Classes Must Use DI Annotations

**Rule:** Classes in `engine.services..**` ending with `Service` must be annotated with `@Singleton` or `@Prototype`.

**Rationale:**
- Ensures all service classes are properly managed by the Micronaut DI container
- Makes service lifecycle explicit
- Prevents accidental manual instantiation of services

**Example:**
```java
// ✅ GOOD: Service with DI annotation
@Singleton
public class AudioService {
    // ...
}

// ❌ BAD: Service without DI annotation
public class AudioService {
    // ...
}
```

### 6. System Classes Must Be Prototypes

**Rule:** Classes in `engine.services.world.systems..**` ending with `System` must be annotated with `@Prototype`.

**Rationale:**
- ECS systems need to be instantiated per world instance
- Singleton systems would be shared across all worlds, causing state issues
- Prototype scope ensures each world gets its own system instances

**Example:**
```java
// ✅ GOOD: System with Prototype annotation
@Prototype
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class MovementSystem implements ISystem {
    // ...
}

// ❌ BAD: System with Singleton annotation
@Singleton
public class MovementSystem implements ISystem {
    // ...
}
```

## Running the Tests

Architectural tests run as part of the normal test suite:

```bash
# Run all tests including architectural tests
mvn test

# Run only architectural tests
mvn test -Dtest=ArchitectureTest
```

## Adding New Rules

When adding new architectural rules:

1. Add the rule to `engine/src/test/java/engine/ArchitectureTest.java`
2. Document the rule in this file
3. Ensure the rule includes a clear `because()` clause explaining the rationale
4. Run the test suite to verify existing code complies
5. If violations exist, decide whether to:
   - Fix the violations (preferred)
   - Exclude specific cases if they have legitimate exceptions
   - Remove the rule if it's not appropriate for the codebase

## References

- [ArchUnit Documentation](https://www.archunit.org/userguide/html/000_Index.html)
- [ArchUnit GitHub](https://github.com/TNG/ArchUnit)
