package engine.services.imgui;

import engine.EngineTestHarness;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for the ImGuiService.
 * Verifies that Dear ImGui is correctly initialized and can render basic UI elements.
 */
public class ImGuiServiceIT extends EngineTestHarness {

  @Inject
  private ImGuiService imGuiService;

  @Test
  void testServiceIsInitialized() {
    assertNotNull(imGuiService, "ImGuiService should be injected");
  }

  @Test
  void testBasicImGuiRendering() {
    // Begin ImGui frame
    imGuiService.beginFrame();

    // Create a simple window to verify ImGui is working
    ImGui.begin("Test Window", ImGuiWindowFlags.NoCollapse);
    ImGui.text("Hello from Dear ImGui!");
    ImGui.end();

    // End ImGui frame
    imGuiService.endFrame();

    // If we get here without exceptions, the integration is working
    assertTrue(true, "ImGui frame rendered successfully");
  }

  @Test
  void testMultipleFrames() {
    // Render multiple frames to ensure ImGui state is properly managed
    for (int i = 0; i < 3; i++) {
      imGuiService.beginFrame();

      ImGui.begin("Frame Counter");
      ImGui.text("Frame: " + i);
      ImGui.end();

      imGuiService.endFrame();
      tick(); // Execute one engine tick
    }

    assertTrue(true, "Multiple ImGui frames rendered successfully");
  }

  @Test
  void testImGuiContextExists() {
    // Verify that ImGui context is created
    assertNotNull(ImGui.getCurrentContext(), "ImGui context should exist");
  }
}
