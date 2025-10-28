package engine;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom JUnit 5 annotation to enable tests only when a display/graphics environment is available.
 * This is used for integration tests that require GLFW/OpenGL initialization.
 * 
 * Tests annotated with this will be skipped in headless environments (e.g., CI without Xvfb).
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(DisplayAvailableCondition.class)
public @interface EnabledIfDisplayAvailable {
    /**
     * Custom reason to explain why the test requires a display.
     */
    String value() default "Test requires a display/graphics environment (GLFW/OpenGL)";
}
