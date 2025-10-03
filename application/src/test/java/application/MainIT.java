package application;

import engine.Engine;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import plugin.ExamplePlugin;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class MainIT {

  @Inject
  private Engine engine;

  @Inject
  private ExamplePlugin examplePlugin;

  @Test
  void testContextStartsAndBeansAreInjected() {
    // This is an integration test that starts the full Micronaut context.
    // It verifies that the dependency injection container is working correctly
    // and can inject beans from different modules.
    assertThat(engine).isNotNull();
    assertThat(examplePlugin).isNotNull();
  }
}
