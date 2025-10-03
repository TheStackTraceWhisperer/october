package engine;

import engine.services.glfw.GlfwContextService;
import engine.services.window.WindowService;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(packages = "engine")
class ApplicationLoopPolicyTest {

    @Inject
    private GlfwContextService glfwContextService;

    @Inject
    private WindowService windowService;

    private long windowHandle;

    @BeforeEach
    void setUp() {
        glfwContextService.start();
        windowService.start();
        windowHandle = windowService.getHandle();
    }

    @AfterEach
    void tearDown() {
        windowService.stop();
        glfwContextService.stop();
    }

    @Test
    void testFramesPolicy() {
        ApplicationLoopPolicy policy = ApplicationLoopPolicy.frames(3);
        assertTrue(policy.continueRunning(0, windowHandle), "Frame 0 should continue");
        assertTrue(policy.continueRunning(1, windowHandle), "Frame 1 should continue");
        assertTrue(policy.continueRunning(2, windowHandle), "Frame 2 should continue");
        assertFalse(policy.continueRunning(3, windowHandle), "Frame 3 should not continue");
    }

    @Test
    void testSkipPolicy() {
        ApplicationLoopPolicy policy = ApplicationLoopPolicy.skip();
        assertFalse(policy.continueRunning(0, windowHandle), "Skip policy should not continue");
    }

    @Test
    void testTimedPolicy() throws InterruptedException {
        ApplicationLoopPolicy policy = ApplicationLoopPolicy.timed(Duration.ofMillis(100));
        assertTrue(policy.continueRunning(0, windowHandle), "Timed policy should continue initially");
        Thread.sleep(110);
        assertFalse(policy.continueRunning(1, windowHandle), "Timed policy should not continue after duration");
    }

    @Test
    void testAllPolicy() {
        ApplicationLoopPolicy policy1 = ApplicationLoopPolicy.frames(2);
        ApplicationLoopPolicy policy2 = (f, h) -> f < 3;
        ApplicationLoopPolicy all = ApplicationLoopPolicy.all(policy1, policy2);

        assertTrue(all.continueRunning(0, windowHandle));
        assertTrue(all.continueRunning(1, windowHandle));
        assertFalse(all.continueRunning(2, windowHandle));
    }

    @Test
    void testAnyPolicy() {
        ApplicationLoopPolicy policy1 = ApplicationLoopPolicy.frames(1);
        ApplicationLoopPolicy policy2 = (f, h) -> f > 2;
        ApplicationLoopPolicy any = ApplicationLoopPolicy.any(policy1, policy2);

        assertTrue(any.continueRunning(0, windowHandle));
        assertFalse(any.continueRunning(1, windowHandle));
        assertFalse(any.continueRunning(2, windowHandle));
        assertTrue(any.continueRunning(3, windowHandle));
    }
}
