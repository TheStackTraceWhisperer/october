//package engine.services.world.systems;
//
//import api.ecs.ISystem;
//import api.ecs.IWorld;
//import engine.services.input.InputService;
//import engine.services.window.WindowService;
//import io.micronaut.context.event.ApplicationEventPublisher;
//import lombok.RequiredArgsConstructor;
//import org.joml.Vector2f;
//import org.lwjgl.glfw.GLFW;
//
///** Manages the layout, interaction, and event handling for all UI entities. */
//@RequiredArgsConstructor
//public class UISystem implements ISystem {
//
////  private final IWorld world;
//  private final WindowService window;
//  private final InputService inputService;
//  private final ApplicationEventPublisher<UIButtonClickedEvent> buttonClickedEvent;
////
////  public UISystem(
////      IWorld world, WindowContext window, GlfwInputService inputService, ApplicationEventPublisher<UIButtonClickedEvent> buttonClickedEvent) {
////    this.world = world;
////    this.window = window;
////    this.inputService = inputService;
////    this.buttonClickedEvent = buttonClickedEvent;
////  }
//
//  @Override
//  public int priority() {
//    return 0;
//  }
//
//  @Override
//  public void update(IWorld world, float deltaTime) {
//    var uiEntities = world.getEntitiesWith(UITransformComponent.class);
//
//    int width = window.getWidth();
//    int height = window.getHeight();
//
//    calculateLayout(uiEntities, width, height);
//    handleButtonInteractions(uiEntities, height);
//  }
//
//  private void calculateLayout(Iterable<Integer> entities, int windowWidth, int windowHeight) {
//    Vector2f parentSize = new Vector2f(windowWidth, windowHeight);
//
//    for (int entityId : entities) {
//      var transform = world.getComponent(entityId, UITransformComponent.class);
//
//      float actualWidth, actualHeight;
//      if (transform.relativeSize) {
//        actualWidth = parentSize.x * transform.size.x;
//        actualHeight = parentSize.y * transform.size.y;
//      } else {
//        actualWidth = transform.size.x;
//        actualHeight = transform.size.y;
//      }
//
//      float anchorPosX = parentSize.x * transform.anchor.x;
//      float anchorPosY = parentSize.y * transform.anchor.y;
//      float pivotPosX = actualWidth * transform.pivot.x;
//      float pivotPosY = actualHeight * transform.pivot.y;
//
//      float minX = anchorPosX - pivotPosX + transform.offset.x;
//      float minY = anchorPosY - pivotPosY + transform.offset.y;
//      float maxX = minX + actualWidth;
//      float maxY = minY + actualHeight;
//
//      transform.screenBounds[0] = minX;
//      transform.screenBounds[1] = minY;
//      transform.screenBounds[2] = maxX;
//      transform.screenBounds[3] = maxY;
//    }
//  }
//
//  private void handleButtonInteractions(Iterable<Integer> entities, int windowHeight) {
//    double mouseX = inputService.getMouseX();
//    // CORRECTED: Flip the Y-coordinate to match OpenGL's bottom-left origin.
//    double mouseY = windowHeight - inputService.getMouseY();
//    boolean isMouseDown = inputService.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT);
//
//    for (int entityId : entities) {
//      if (!world.hasComponent(entityId, UIButtonComponent.class)
//          || !world.hasComponent(entityId, UIImageComponent.class)) {
//        continue;
//      }
//
//      var transform = world.getComponent(entityId, UITransformComponent.class);
//      var button = world.getComponent(entityId, UIButtonComponent.class);
//      var image = world.getComponent(entityId, UIImageComponent.class);
//
//      float[] bounds = transform.screenBounds;
//      boolean isHovered =
//          mouseX >= bounds[0]
//              && mouseX <= bounds[2]
//              && mouseY >= bounds[1]
//              && mouseY <= bounds[3];
//
//      // Determine the new state
//      UIButtonComponent.ButtonState previousState = button.currentState;
//      if (isHovered) {
//        if (isMouseDown) {
//          button.currentState = UIButtonComponent.ButtonState.PRESSED;
//        } else {
//          if (previousState == UIButtonComponent.ButtonState.PRESSED) {
//            buttonClickedEvent.publishEvent(new UIButtonClickedEvent(button.actionEvent));
//          }
//          button.currentState = UIButtonComponent.ButtonState.HOVERED;
//        }
//      } else {
//        button.currentState = UIButtonComponent.ButtonState.NORMAL;
//      }
//
//      // Update the image based on the new state
//      if (button.currentState != previousState) {
//        switch (button.currentState) {
//          case HOVERED -> image.textureHandle = button.hoveredTexture;
//          case PRESSED -> image.textureHandle = button.pressedTexture;
//          default -> // NORMAL
//          image.textureHandle = button.normalTexture;
//        }
//      }
//    }
//  }
//}
