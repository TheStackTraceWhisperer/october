package plugin;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@MicronautTest
class ExamplePluginIT {

    @Inject
    private ExamplePlugin examplePlugin;

    @Test
    void testPluginIsCreatedInContext() {
        // This integration test verifies that the ExamplePlugin is correctly
        // created as a Singleton bean in the Micronaut context.
        assertThat(examplePlugin).isNotNull();
    }
}
