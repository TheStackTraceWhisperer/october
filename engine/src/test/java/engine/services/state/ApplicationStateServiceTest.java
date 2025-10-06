package engine.services.state;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(packages = "engine")
class ApplicationStateServiceTest {

    @Inject
    private ApplicationStateService applicationStateService;

    private TestState1 testState1;
    private TestState2 testState2;

    @BeforeEach
    void setUp() {
        // Ensure the state service is clean before each test
        applicationStateService.stop();
        testState1 = new TestState1();
        testState2 = new TestState2();
    }

    @Test
    void testStartAndStop() {
        assertTrue(applicationStateService.isEmpty());
        applicationStateService.start();
        assertFalse(applicationStateService.isEmpty());
        assertEquals("TestGameState", applicationStateService.peek().getClass().getSimpleName());

        applicationStateService.stop();
        assertTrue(applicationStateService.isEmpty());
    }

    @Test
    void testPushAndPopState() {
        assertTrue(applicationStateService.isEmpty());
        applicationStateService.pushState(testState1);
        assertTrue(testState1.entered);
        assertFalse(testState1.exited);
        assertEquals(testState1, applicationStateService.peek());

        applicationStateService.pushState(testState2);
        assertTrue(testState2.entered);
        assertFalse(testState2.exited);
        assertEquals(testState2, applicationStateService.peek());

        applicationStateService.popState();
        assertTrue(testState2.exited);
        assertEquals(testState1, applicationStateService.peek());

        applicationStateService.popState();
        assertTrue(testState1.exited);
        assertTrue(applicationStateService.isEmpty());
    }

    @Test
    void testChangeState() {
        assertTrue(applicationStateService.isEmpty());
        applicationStateService.pushState(testState1);
        applicationStateService.changeState(testState2);

        assertTrue(testState1.exited);
        assertTrue(testState2.entered);
        assertFalse(testState2.exited);
        assertEquals(testState2, applicationStateService.peek());
    }

    @Test
    void testUpdate() {
        assertTrue(applicationStateService.isEmpty());
        applicationStateService.pushState(testState1);
        applicationStateService.update(0.1f);
        assertEquals(0.1f, testState1.lastDelta);
    }

    // Mock GameState implementations for testing

    static class TestState1 implements ApplicationState {
        boolean entered = false;
        boolean exited = false;
        float lastDelta = 0.0f;

        @Override
        public void onEnter() {
            entered = true;
        }

        @Override
        public void onUpdate(float deltaTime) {
            lastDelta = deltaTime;
        }

        @Override
        public void onExit() {
            exited = true;
        }
    }

    static class TestState2 implements ApplicationState {
        boolean entered = false;
        boolean exited = false;

        @Override
        public void onEnter() {
            entered = true;
        }

        @Override
        public void onUpdate(float deltaTime) {}

        @Override
        public void onExit() {
            exited = true;
        }
    }
}
