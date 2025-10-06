//package engine.services.scene;
//
//import api.ecs.IComponent;
//import api.ecs.IWorld;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import engine.services.resources.AssetCacheService;
//import jakarta.inject.Singleton;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Map;
//
//@Slf4j
//@Singleton
//@RequiredArgsConstructor
//public class SceneManager {
//  private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new CustomJomlModule());
//
//  private final AssetCacheService resourceManager;
//  private Map<String, Class<? extends IComponent>> componentRegistry;
//
//  /**
//   * Initializes the scene manager with the component registry.
//   * This must be called before loading any scenes.
//   */
//  public void initialize(Map<String, Class<? extends IComponent>> componentRegistry) {
//    this.componentRegistry = componentRegistry;
//  }
//
//  public void load(String path, IWorld world) {
//    if (componentRegistry == null) {
//      throw new IllegalStateException("SceneManager not initialized. Call initialize() with component registry first.");
//    }
//
//    log.info("Loading scene: {}", path);
//    world.getEntitiesWith().forEach(world::destroyEntity);
//
//    try (InputStream sceneStream = SceneManager.class.getResourceAsStream(path)) {
//      if (sceneStream == null) {
//        throw new IOException("Scene file not found: " + path);
//      }
//      Scene scene = MAPPER.readValue(sceneStream, Scene.class);
//      log.info("Successfully parsed scene: {}", scene.name());
//
//      // Step 1: Load all assets from the manifest
//      loadAssets(scene.manifest());
//
//      // Step 2: Create all entities from the templates
//      for (EntityTemplate template : scene.entities()) {
//        int entity = world.createEntity();
//        log.info("creating entity {} with id {}", template.name(), entity);
//
//        for (Map.Entry<String, Object> componentEntity : template.components().entrySet()) {
//          String componentName = componentEntity.getKey();
//          Class<? extends IComponent> componentClass = componentRegistry.get(componentName);
//
//          if (componentClass != null) {
//            IComponent component = MAPPER.convertValue(componentEntity.getValue(), componentClass);
//            world.addComponent(entity, component);
//          } else {
//            log.warn("Unknown component type '{}' for entity '{}'", componentName, template.name());
//          }
//        }
//      }
//    } catch (Exception e) {
//      log.error("Failed to load scene {}", path, e);
//    }
//  }
//
//  private void loadAssets(AssetManifest manifest) {
//    if (manifest == null) {
//      return;
//    }
//
//    log.info("Loading assets from scene manifest...");
//    if (manifest.textures() != null) {
//      for (TextureDefinition textureDef : manifest.textures()) {
//        resourceManager.loadTexture(textureDef.handle(), textureDef.path());
//      }
//    }
//
//    if (manifest.meshes() != null) {
//      for (MeshDefinition meshDef : manifest.meshes()) {
//        resourceManager.loadProceduralMesh(meshDef.handle(), meshDef.vertices(), meshDef.indices());
//      }
//    }
//  }
//}
