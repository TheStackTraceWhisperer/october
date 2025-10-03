package api.events;

import io.micronaut.context.ApplicationContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class PingIT {

  @Inject
  private ApplicationContext context;

  @Test
  void testContextStarts() {
    // This integration test simply verifies that a Micronaut context can be started
    // for this module without any errors, confirming the test setup is correct.
    assertThat(context).isNotNull();
    assertThat(context.isRunning()).isTrue();
  }
}
