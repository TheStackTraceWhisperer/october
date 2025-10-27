# Dear ImGui Integration

This project integrates [Dear ImGui](https://github.com/ocornut/imgui) using the [imgui-java](https://github.com/SpaiR/imgui-java) binding by SpaiR.

## Overview

Dear ImGui is a bloat-free graphical user interface library for C++ which has been ported to Java. It provides immediate mode GUI functionality that's perfect for debugging tools, in-game developer consoles, and editor interfaces.

## Dependencies

The following dependencies have been added to enable Dear ImGui:

- `imgui-java-binding` (version 1.89.0) - Core ImGui Java bindings
- `imgui-java-lwjgl3` (version 1.89.0) - LWJGL3 integration layer
- Platform-specific native libraries for Windows, Linux, and macOS

## Service

The `ImGuiService` class manages the Dear ImGui lifecycle:

- **Initialization**: Sets up the ImGui context with GLFW and OpenGL bindings
- **Frame Management**: Provides `beginFrame()` and `endFrame()` methods
- **Cleanup**: Properly disposes resources on shutdown

## Usage Example

Here's a basic example of how to use ImGui in an application state:

```java
import engine.services.imgui.ImGuiService;
import engine.services.state.ApplicationState;
import imgui.ImGui;
import jakarta.inject.Inject;

public class MyGameState implements ApplicationState {
  
  private final ImGuiService imGuiService;
  
  @Inject
  public MyGameState(ImGuiService imGuiService) {
    this.imGuiService = imGuiService;
  }
  
  @Override
  public void onUpdate(float deltaTime) {
    // Begin ImGui frame
    imGuiService.beginFrame();
    
    // Create your UI
    ImGui.begin("Debug Window");
    ImGui.text("FPS: " + (1.0f / deltaTime));
    ImGui.text("Delta Time: " + deltaTime + "s");
    
    if (ImGui.button("Click Me!")) {
      System.out.println("Button clicked!");
    }
    
    ImGui.end();
    
    // End ImGui frame (this renders the UI)
    imGuiService.endFrame();
  }
}
```

## Key Features

- **Docking Support**: The ImGui context is configured with `ImGuiConfigFlags.DockingEnable`
- **Keyboard Navigation**: Enabled via `ImGuiConfigFlags.NavEnableKeyboard`
- **OpenGL 4.6**: Uses GLSL version 460 core for shader compatibility

## Testing

A basic unit test (`ImGuiServiceTest`) verifies that the service can be instantiated and has the correct execution order.

Integration tests requiring an OpenGL context should extend `EngineTestHarness` but may not run in headless CI environments.

## Common Use Cases

- Debug overlays showing performance metrics
- In-game developer consoles
- Level editor interfaces
- Diagnostic tools and profilers
- Settings and configuration panels
