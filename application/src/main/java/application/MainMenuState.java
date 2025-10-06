package application;

import api.ecs.IComponent;
import engine.services.rendering.Camera;
import engine.services.rendering.RenderingService;
import engine.services.resources.AssetCacheService;
import engine.services.scene.SceneService;
import engine.services.state.ApplicationState;
import engine.services.world.WorldService;
import engine.services.world.components.ColliderComponent;
import engine.services.world.components.ControllableComponent;
import engine.services.world.components.MovementStatsComponent;
import engine.services.world.components.SpriteComponent;
import engine.services.world.components.TransformComponent;
import engine.services.world.systems.RenderSystem;
import io.micronaut.context.annotation.Prototype;
import jakarta.inject.Named;

import lombok.RequiredArgsConstructor;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

@Prototype
@Named("initial")
@RequiredArgsConstructor
public class MainMenuState implements ApplicationState {

  private final SceneService sceneService;
  private final WorldService worldService;
  private final AssetCacheService assetCacheService;
  private final RenderingService renderingService;

//  private final PlayingState playingState;
//  private final GameStateManager gameStateManager;
//  private final SystemFactory systemFactory;
//  private final UISystemFactory uiSystemFactory;
//  private EngineServices services;
//
//  @Inject
//  public MainMenuState(
//      PlayingState playingState,
//      GameStateManager gameStateManager,
//      SystemFactory systemFactory,
//      UISystemFactory uiSystemFactory) {
//    this.playingState = playingState;
//    this.gameStateManager = gameStateManager;
//    this.systemFactory = systemFactory;
//    this.uiSystemFactory = uiSystemFactory;
//  }
//
//  /**
//   * Handles UI button click events using Micronaut's @EventListener annotation.
//   * This demonstrates the Micronaut event system.
//   */
//  @EventListener
//  public void onButtonClicked(UIButtonClickedEvent event) {
//    if ("START_NEW_GAME".equals(event.actionEvent())) {
//      gameStateManager.changeState(playingState, services);
//    }
//  }

  public Map<String, Class<? extends IComponent>> getComponentRegistry() {
    Map<String, Class<? extends IComponent>> registry = new HashMap<>();
    // Engine Components
    registry.put("TransformComponent", TransformComponent.class);
    registry.put("SpriteComponent", SpriteComponent.class);
    registry.put("ControllableComponent", ControllableComponent.class);
    registry.put("MovementStatsComponent", MovementStatsComponent.class);
    registry.put("ColliderComponent", ColliderComponent.class);
    // Game Components
    registry.put("PlayerComponent", PlayerComponent.class);
    registry.put("EnemyComponent", EnemyComponent.class);
    registry.put("HealthComponent", HealthComponent.class);

//    registry.put("UITransformComponent", UITransformComponent.class);
//    registry.put("UIImageComponent", UIImageComponent.class);
//    registry.put("UIButtonComponent", UIButtonComponent.class);

    return registry;
  }

  @Override
  public void onEnter() {
    // Load the scene for the main menu.
    sceneService.initialize(getComponentRegistry());
    sceneService.load("/scenes/playing-scene.json");

    Camera camera = new Camera(); // TODO: this would actually come from the scene
    camera.setPosition(new Vector3f(0.0f, 0.0f, 5.0f));

    worldService.addSystem(new RenderSystem(renderingService, assetCacheService, camera));
    //systemManager.register(systemFactory.createRenderSystem(services.world()));
    //systemManager.register(uiSystemFactory.createUISystem(services.world()));
    //systemManager.register(systemFactory.createUIRenderSystem(services.world()));
  }

  @Override
  public void onUpdate(float deltaTime) {

  }

  @Override
  public void onExit() {
    worldService.clearSystems();
  }
}
