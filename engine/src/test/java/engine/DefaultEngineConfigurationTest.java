package engine;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@MicronautTest(packages = "engine")
class DefaultEngineConfigurationTest {

    @Inject
    private ApplicationLoopPolicy applicationLoopPolicy;

    @Test
    void testDefaultLoopPolicy() {
        assertNotNull(applicationLoopPolicy, "ApplicationLoopPolicy should be injected");
    }
}
