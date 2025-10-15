package engine.services.scene;

import com.fasterxml.jackson.databind.ObjectMapper;
import engine.IService;
import engine.services.resources.AssetCacheService;
import engine.services.world.WorldService;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class SceneService implements IService {
  private static final ObjectMapper MAPPER = new ObjectMapper().registerModule(new CustomJomlModule());

  private final WorldService worldService;
  private final AssetCacheService resourceManager;

  private Map<String, Class<?>> componentRegistry;

  @Override
  public int priority() {
    return 60;
  }

  public void initialize(Map<String, Class<?>> componentRegistry) {
    this.componentRegistry = componentRegistry;
  }

  public void load(String path) {
    if (componentRegistry == null) {
      throw new IllegalStateException("SceneManager not initialized. Call initialize() with component registry first.");
    }

    log.info("Loading scene: {}", path);
    worldService.getEntitiesWith().forEach(worldService::destroyEntity);

    try (InputStream sceneStream = SceneService.class.getResourceAsStream(path)) {
      if (sceneStream == null) {
        throw new IOException("Scene file not found: " + path);
      }
      Scene scene = MAPPER.readValue(sceneStream, Scene.class);
      log.info("Successfully parsed scene: {}", scene.name());

      loadAssets(scene.manifest());

      for (EntityTemplate template : scene.entities()) {
        int entity = worldService.createEntity();
        log.info("creating entity {} with id {}", template.name(), entity);

        for (Map.Entry<String, Object> componentEntity : template.components().entrySet()) {
          String componentName = componentEntity.getKey();
          Class<?> componentClass = componentRegistry.get(componentName);

          if (componentClass != null) {
            Object component = MAPPER.convertValue(componentEntity.getValue(), componentClass);
            worldService.addComponent(entity, component);
          } else {
            log.warn("Unknown component type \'{}\' for entity \'{}\'", componentName, template.name());
          }
        }
      }
    } catch (Exception e) {
      log.error("Failed to load scene {}", path, e);
    }
  }

  private void loadAssets(AssetManifest manifest) {
    if (manifest == null) {
      return;
    }

    log.info("Loading assets from scene manifest...");
    if (manifest.textures() != null) {
      for (TextureDefinition textureDef : manifest.textures()) {
        resourceManager.loadTexture(textureDef.handle(), textureDef.path());
      }
    }

    if (manifest.meshes() != null) {
      for (MeshDefinition meshDef : manifest.meshes()) {
        resourceManager.loadProceduralMesh(meshDef.handle(), meshDef.vertices(), meshDef.indices());
      }
    }
  }
}
