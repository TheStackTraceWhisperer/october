package engine.services.window;

import engine.services.glfw.GlfwContextService;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@MicronautTest(packages = "engine")
class WindowServiceTest {

    @Inject
    private WindowService windowService;

    @Inject
    private GlfwContextService glfwContextService;

    @BeforeEach
    void setUp() {
        glfwContextService.start();
    }

    @AfterEach
    void tearDown() {
        glfwContextService.stop();
    }

    @Test
    void testWindowLifecycle() {
        windowService.start();
        assertNotEquals(0L, windowService.getHandle(), "Window handle should be created.");

        windowService.stop();
        assertEquals(0L, windowService.getHandle(), "Window handle should be released.");
    }
}
