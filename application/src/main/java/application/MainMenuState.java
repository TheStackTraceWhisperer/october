package application;

import engine.services.scene.SceneService;
import engine.services.state.ApplicationState;
import engine.services.state.ApplicationStateService;
import engine.services.world.WorldService;
import engine.services.world.components.UIButtonComponent;
import engine.services.world.components.UIImageComponent;
import engine.services.world.components.UITransformComponent;
import engine.services.world.systems.UISystem;
import io.micronaut.context.annotation.Prototype;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Prototype
@Named("initial")
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class MainMenuState implements ApplicationState {

  private final SceneService sceneService;
  private final WorldService worldService;
  private final ApplicationStateService applicationStateService;
  private final UISystem uiSystem;

  @Override
  public void onEnter() {
    Map<String, Class<?>> registry = new HashMap<>();
    registry.put("UITransformComponent", UITransformComponent.class);
    registry.put("UIImageComponent", UIImageComponent.class);
    registry.put("UIButtonComponent", UIButtonComponent.class);

    sceneService.initialize(registry);
    sceneService.load("/scenes/main_menu.json");

    worldService.addSystem(uiSystem);
  }

  @EventListener
  public void onStartGame(String event) {
    if ("START_NEW_GAME".equals(event)) {
      applicationStateService.changeState(PlayingState.class);
    }
  }

  @Override
  public void onUpdate(float deltaTime) {
    // The UISystem is now handling updates and rendering
  }

  @Override
  public void onExit() {
    worldService.clearSystems();
  }
}