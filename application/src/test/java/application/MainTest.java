package application;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;

class MainTest {

    @Test
    void testApplicationMethods() {
        // This is a simple unit test that doesn't use the Micronaut context.
        // It just verifies that the methods on the Main class can be called without throwing an exception.
        Main main = new Main();
        assertThatCode(() -> {
            main.start();
            main.run();
            main.stop();
        }).doesNotThrowAnyException();
    }
}
