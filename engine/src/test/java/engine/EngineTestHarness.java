package engine;

import engine.services.state.GameState;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.TestInstance;

/**
 * @see <a href="docs/engine-test-harness.md">Engine Test Harness</a>
 */
@MicronautTest(packages = "engine")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class EngineTestHarness {

  @Inject
  protected Engine engine;

  @Slf4j
  @Factory
  static class TestConfiguration {

    @Primary
    @Singleton
    @Named("initial")
    static class TestGameState implements GameState {
      @Override
      public void onEnter() {
        log.info("Entering test harness game state. Asset loading would happen here.");
      }

      @Override
      public void onUpdate(float deltaTime) {
        /* No-op for harness */
      }

      @Override
      public void onExit() {
        /* No-op for harness */
      }
    }

    @Primary
    @Singleton
    public ApplicationLoopPolicy testLoopPolicy() {
      return ApplicationLoopPolicy.skip();
    }
  }
}