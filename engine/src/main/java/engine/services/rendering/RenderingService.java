package engine.services.rendering;

import engine.IService;
import engine.services.resources.AssetCacheService;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class RenderingService implements IService {

  private final AssetCacheService assetCacheService;

  @Getter
  private Renderer renderer;

  @Override
  public int priority() {
    return 30;
  }

  @Override
  public void start() {
    this.renderer = new Renderer(assetCacheService);
    this.renderer.start();
  }
}
