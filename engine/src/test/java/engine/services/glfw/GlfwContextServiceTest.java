package engine.services.glfw;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class GlfwContextServiceTest {

    @Test
    void testStartAndStop() {
        GlfwContextService contextService = new GlfwContextService();
        assertDoesNotThrow(contextService::start, "Starting the service should not throw an exception.");
        assertDoesNotThrow(contextService::stop, "Stopping the service should not throw an exception.");
    }
}
