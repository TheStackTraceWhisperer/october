package engine;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.lwjgl.glfw.GLFW;

/**
 * JUnit 5 ExecutionCondition that checks whether a display/graphics environment is available.
 * 
 * This condition attempts to initialize GLFW to determine if the environment supports
 * graphics operations. If GLFW initialization fails (typically in headless environments),
 * the test is disabled.
 */
@Slf4j
public class DisplayAvailableCondition implements ExecutionCondition {
    
    private static Boolean displayAvailable = null;

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        if (displayAvailable == null) {
            displayAvailable = checkDisplayAvailable();
        }

        if (displayAvailable) {
            return ConditionEvaluationResult.enabled("Display/graphics environment is available");
        } else {
            String reason = "Display/graphics environment is NOT available (headless environment). " +
                           "Test requires GLFW/OpenGL which needs a display server (X11/Xvfb).";
            log.info("Skipping test {}: {}", 
                    context.getDisplayName(), 
                    reason);
            return ConditionEvaluationResult.disabled(reason);
        }
    }

    /**
     * Checks if a display is available by attempting to initialize GLFW.
     * This is a safe check that doesn't throw exceptions.
     * 
     * @return true if GLFW can be initialized, false otherwise
     */
    private boolean checkDisplayAvailable() {
        try {
            // Attempt to initialize GLFW
            boolean initialized = GLFW.glfwInit();
            if (initialized) {
                // Clean up immediately - we don't want to hold the GLFW context
                GLFW.glfwTerminate();
                log.info("Display check: GLFW initialization successful - display is available");
                return true;
            } else {
                log.info("Display check: GLFW initialization failed - no display available");
                return false;
            }
        } catch (Exception e) {
            // Any exception during initialization means display is not available
            log.info("Display check: Exception during GLFW initialization - no display available: {}", 
                    e.getMessage());
            return false;
        }
    }
}
