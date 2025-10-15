package engine.services.rendering;

import engine.IService;
import engine.services.resources.AssetCacheService;
import engine.services.window.WindowService;
import jakarta.inject.Singleton;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class UIRendererService implements IService {

  private final AssetCacheService assetCacheService;
  private final WindowService windowService;

  @Getter
  private UIRenderer renderer;

  @Override
  public int priority() {
    return 31;
  }

  @Override
  public void start() {
    this.renderer = new UIRenderer(assetCacheService, windowService);
    this.renderer.start();
  }
}
