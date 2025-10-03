package engine.services.window;

import engine.ApplicationLoopPolicy;
import jakarta.inject.Singleton;

@Singleton
public class WindowDefaults {

  public int width() {
    return 1024;
  }

  public int height() {
    return 768;
  }

  public String title() {
    return "Micronaut September Engine";
  }

  public ApplicationLoopPolicy loopPolicy() {
    return ApplicationLoopPolicy.standard();
  }
}
