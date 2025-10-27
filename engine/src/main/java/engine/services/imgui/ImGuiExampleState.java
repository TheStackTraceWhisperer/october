package engine.services.imgui;

import engine.services.state.ApplicationState;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImBoolean;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

/**
 * Example application state demonstrating Dear ImGui usage.
 * This class shows how to create various UI elements using ImGui in the game engine.
 * 
 * Note: This is an example class for demonstration purposes.
 * To use it, inject it as an ApplicationState in your game.
 */
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ImGuiExampleState implements ApplicationState {

  private final ImGuiService imGuiService;
  
  private final ImBoolean showDemoWindow = new ImBoolean(false);
  private float[] floatValue = new float[]{0.5f};
  private int counter = 0;

  @Override
  public void onEnter() {
    // Initialization code if needed
  }

  @Override
  public void onUpdate(float deltaTime) {
    // Begin ImGui frame
    imGuiService.beginFrame();
    
    // Create main demo window
    createMainWindow(deltaTime);
    
    // Show ImGui's built-in demo window if enabled
    if (showDemoWindow.get()) {
      ImGui.showDemoWindow();
    }
    
    // End ImGui frame and render
    imGuiService.endFrame();
  }

  private void createMainWindow(float deltaTime) {
    // Set initial window position and size (only on first frame)
    ImGui.setNextWindowPos(50, 50, ImGuiCond.FirstUseEver);
    ImGui.setNextWindowSize(400, 300, ImGuiCond.FirstUseEver);
    
    // Create window
    ImGui.begin("ImGui Example Window", ImGuiWindowFlags.MenuBar);
    
    // Menu bar
    if (ImGui.beginMenuBar()) {
      if (ImGui.beginMenu("File")) {
        if (ImGui.menuItem("Exit")) {
          // Handle exit
        }
        ImGui.endMenu();
      }
      if (ImGui.beginMenu("View")) {
        ImGui.menuItem("Show Demo Window", "", showDemoWindow);
        ImGui.endMenu();
      }
      ImGui.endMenuBar();
    }
    
    // Display FPS
    ImGui.text(String.format("Frame Time: %.3f ms (%.1f FPS)", 
      deltaTime * 1000.0f, 1.0f / deltaTime));
    
    ImGui.separator();
    
    // Interactive elements
    ImGui.text("Interactive Controls:");
    
    if (ImGui.button("Click Me!")) {
      counter++;
    }
    ImGui.sameLine();
    ImGui.text("Counter: " + counter);
    
    ImGui.sliderFloat("Float Slider", floatValue, 0.0f, 1.0f);
    
    ImGui.checkbox("Show Demo Window", showDemoWindow);
    
    ImGui.separator();
    
    // Collapsing headers
    if (ImGui.collapsingHeader("System Information")) {
      ImGui.text("Java Version: " + System.getProperty("java.version"));
      ImGui.text("OS: " + System.getProperty("os.name"));
      ImGui.text("ImGui Version: " + ImGui.getVersion());
    }
    
    if (ImGui.collapsingHeader("Usage Tips")) {
      ImGui.bulletText("Use ImGui for debug overlays and tools");
      ImGui.bulletText("Windows can be docked by dragging");
      ImGui.bulletText("Use keyboard navigation with Tab/Arrow keys");
    }
    
    ImGui.end();
  }

  @Override
  public void onExit() {
    // Cleanup code if needed
  }
}
