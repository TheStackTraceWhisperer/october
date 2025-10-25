package engine.services.state;

import io.micronaut.context.ApplicationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import engine.services.world.WorldService;

import jakarta.inject.Provider;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationStateServiceTest {

    @Mock
    private ApplicationContext applicationContext;
    @Mock
    private Provider<ApplicationState> initialStateProvider;
    @Mock
    private ApplicationState initialState;
    @Mock
    private ApplicationState newState;
    @Mock
    private WorldService worldService;

    private ApplicationStateService stateService;

    @BeforeEach
    void setUp() {
        // Leniently stub the supplier to avoid issues in tests that don't call start()
        lenient().when(initialStateProvider.get()).thenReturn(initialState);
        stateService = new ApplicationStateService(applicationContext, worldService, initialStateProvider);
    }

    @Test
    void start_pushesAndEntersInitialState() {
        // When
        stateService.start();

        // Then
        verify(initialStateProvider).get();
        verify(initialState).onEnter();
    }

    @Test
    void pushState_entersNewState() {
        // When
        stateService.pushState(newState);

        // Then
        verify(newState).onEnter();
    }

    @Test
    void popState_exitsCurrentState() {
        // Given
        stateService.pushState(initialState);

        // When
        stateService.popState();

        // Then
        verify(initialState).onExit();
    }

    @Test
    void changeState_exitsOldStateAndEntersNewState() {
        // Given
        stateService.pushState(initialState);

        // When
        stateService.changeState(newState);

        // Then
        InOrder inOrder = inOrder(initialState, newState);
        inOrder.verify(initialState).onExit();
        inOrder.verify(newState).onEnter();
    }

    @Test
    void update_callsUpdateOnActiveState() {
        // Given
        stateService.pushState(initialState);
        float deltaTime = 0.16f;

        // When
        stateService.update(deltaTime);

        // Then
        verify(initialState).onUpdate(deltaTime);
    }

    @Test
    void stop_exitsAllStatesInStack() {
        // Given
        stateService.pushState(initialState);
        stateService.pushState(newState);

        // When
        stateService.stop();

        // Then
        InOrder inOrder = inOrder(newState, initialState);
        inOrder.verify(newState).onExit();
        inOrder.verify(initialState).onExit();
    }
}
