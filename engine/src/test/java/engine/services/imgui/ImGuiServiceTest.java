package engine.services.imgui;

import engine.services.window.WindowService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for the ImGuiService.
 * Tests basic service structure without requiring OpenGL context.
 */
public class ImGuiServiceTest {

  @Test
  void testServiceHasCorrectExecutionOrder() {
    WindowService mockWindowService = mock(WindowService.class);
    ImGuiService service = new ImGuiService(mockWindowService);

    assertEquals(32, service.executionOrder(),
      "ImGuiService should execute after rendering services (order 32)");
  }

  @Test
  void testServiceCanBeInstantiated() {
    WindowService mockWindowService = mock(WindowService.class);
    ImGuiService service = new ImGuiService(mockWindowService);

    assertNotNull(service, "ImGuiService should be instantiable");
  }
}
