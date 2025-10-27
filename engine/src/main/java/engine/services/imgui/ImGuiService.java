package engine.services.imgui;

import engine.IService;
import engine.services.window.WindowService;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for managing Dear ImGui lifecycle and rendering.
 * Integrates Dear ImGui with GLFW and OpenGL for immediate mode GUI rendering.
 */
@Slf4j
@Singleton
@RequiredArgsConstructor
public class ImGuiService implements IService {

  private final WindowService windowService;

  private ImGuiImplGlfw imGuiGlfw;
  private ImGuiImplGl3 imGuiGl3;

  @Override
  public int executionOrder() {
    return 32; // After rendering services
  }

  @Override
  public void start() {
    log.info("Initializing Dear ImGui");
    
    // Initialize Dear ImGui context
    ImGui.createContext();
    
    // Configure ImGui IO
    final ImGuiIO io = ImGui.getIO();
    io.addConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
    io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
    
    // Initialize platform bindings
    imGuiGlfw = new ImGuiImplGlfw();
    imGuiGlfw.init(windowService.getHandle(), true);
    
    imGuiGl3 = new ImGuiImplGl3();
    imGuiGl3.init("#version 460 core");
    
    log.info("Dear ImGui initialized successfully");
  }

  /**
   * Begins a new Dear ImGui frame.
   * Should be called before any ImGui rendering commands.
   */
  public void beginFrame() {
    if (imGuiGlfw != null) {
      imGuiGlfw.newFrame();
      ImGui.newFrame();
    }
  }

  /**
   * Ends the Dear ImGui frame and renders the draw data.
   * Should be called after all ImGui rendering commands.
   */
  public void endFrame() {
    if (imGuiGl3 != null) {
      ImGui.render();
      imGuiGl3.renderDrawData(ImGui.getDrawData());
    }
  }

  @Override
  public void stop() {
    log.info("Shutting down Dear ImGui");
    if (imGuiGl3 != null) {
      imGuiGl3.shutdown();
    }
    if (imGuiGlfw != null) {
      imGuiGlfw.shutdown();
    }
    ImGui.destroyContext();
    log.info("Dear ImGui shut down successfully");
  }
}
