package engine;

import engine.services.state.ApplicationStateService;
import engine.services.time.SystemTimeService;
import engine.services.window.WindowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link Engine} class, focusing on its lifecycle and state management.
 * This test is isolated from the Micronaut context and uses mocks for all dependencies.
 */
@ExtendWith(MockitoExtension.class)
class EngineTest {

  @Mock
  private ApplicationLoopPolicy loopPolicy;
  @Mock
  private ApplicationStateService applicationStateService;
  @Mock
  private WindowService windowService;
  @Mock
  private SystemTimeService systemTimeService;
  @Mock
  private IService serviceHighPriority; // e.g., priority 1
  @Mock
  private IService serviceLowPriority;  // e.g., priority 10
  @Mock
  private IService serviceMediumPriority; // e.g., priority 5

  private Engine engine;
  private List<IService> mutableServices;

  @BeforeEach
  void setUp() {
    // Configure service priorities (leniently, as not all tests directly use these stubs)
    lenient().when(serviceHighPriority.priority()).thenReturn(1);
    lenient().when(serviceMediumPriority.priority()).thenReturn(5);
    lenient().when(serviceLowPriority.priority()).thenReturn(10);

    // The Engine requires a mutable list to sort, so we must provide an ArrayList.
    // Add them in a non-sorted order to ensure the engine's sorting logic is tested.
    mutableServices = new ArrayList<>(List.of(serviceLowPriority, serviceHighPriority, serviceMediumPriority));
    engine = new Engine(loopPolicy, applicationStateService, mutableServices, windowService, systemTimeService);

    // Default mock behavior for loop policy and application state for most tests.
    // Marked as lenient to avoid UnnecessaryStubbingException if not used in every test.
    lenient().when(loopPolicy.continueRunning(anyInt(), anyLong())).thenReturn(true);
    lenient().when(applicationStateService.isEmpty()).thenReturn(false);
  }

  @Test
  void init_whenCalledMultipleTimes_onlyInitializesServicesOnce() {
    // Act
    engine.init();
    engine.init(); // Second call should be ignored

    // Assert
    // Verify that the start method on each service was called exactly once.
    verify(serviceHighPriority, times(1)).start();
    verify(serviceMediumPriority, times(1)).start();
    verify(serviceLowPriority, times(1)).start();
  }

  @Test
  void init_startsServicesInPriorityOrder() {
    // Act
    engine.init();

    // Assert
    InOrder inOrder = inOrder(serviceHighPriority, serviceMediumPriority, serviceLowPriority);
    inOrder.verify(serviceHighPriority).start();
    inOrder.verify(serviceMediumPriority).start();
    inOrder.verify(serviceLowPriority).start();
  }

  @Test
  void shutdown_whenCalledMultipleTimes_onlyShutsDownServicesOnce() {
    // Arrange
    engine.init(); // Engine must be initialized to be shut down

    // Act
    engine.shutdown();
    engine.shutdown(); // Second call should be ignored

    // Assert
    // Verify that the stop method on each service was called exactly once.
    verify(serviceHighPriority, times(1)).stop();
    verify(serviceMediumPriority, times(1)).stop();
    verify(serviceLowPriority, times(1)).stop();
  }

  @Test
  void shutdown_stopsServicesInReversePriorityOrder() {
    // Arrange
    engine.init(); // Initialize first to set up the state

    // Act
    engine.shutdown();

    // Assert
    InOrder inOrder = inOrder(serviceLowPriority, serviceMediumPriority, serviceHighPriority);
    inOrder.verify(serviceLowPriority).stop();
    inOrder.verify(serviceMediumPriority).stop();
    inOrder.verify(serviceHighPriority).stop();
  }

  @Test
  void tick_whenEngineNotInitialized_throwsIllegalStateException() {
    // Assert
    // Verify that calling tick() on a new engine throws the correct exception.
    assertThrows(IllegalStateException.class, () -> engine.tick());
  }

  @Test
  void tick_whenEngineInitialized_performsExpectedActions() {
    // Arrange
    engine.init();
    float expectedDeltaTime = 0.016f; // Example delta time
    when(systemTimeService.getDeltaTimeSeconds()).thenReturn(expectedDeltaTime);

    // Act
    engine.tick();

    // Assert
    verify(windowService).pollEvents();
    verify(serviceHighPriority).update();
    verify(serviceMediumPriority).update();
    verify(serviceLowPriority).update();
    verify(serviceHighPriority).update(expectedDeltaTime);
    verify(serviceMediumPriority).update(expectedDeltaTime);
    verify(serviceLowPriority).update(expectedDeltaTime);
    verify(windowService).swapBuffers();
    // We can't easily verify the 'frames' counter directly without a getter,
    // but its effect on loopPolicy.continueRunning is tested in run_executesMainLoop.
  }

  @Test
  void run_whenLoopDoesNotRun_stillCallsInitAndShutdown() {
    // Arrange
    // Configure the loop policy to immediately exit the main loop.
    when(loopPolicy.continueRunning(anyInt(), anyLong())).thenReturn(false);

    // Act
    engine.run();

    // Assert
    // Verify that init() and shutdown() were both called once as part of the run() lifecycle.
    verify(serviceHighPriority, times(1)).start();
    verify(serviceHighPriority, times(1)).stop();
    verify(serviceMediumPriority, times(1)).start();
    verify(serviceMediumPriority, times(1)).stop();
    verify(serviceLowPriority, times(1)).start();
    verify(serviceLowPriority, times(1)).stop();
  }

  @Test
  void run_executesMainLoopUntilConditionFalse() {
    // Arrange
    // Configure loop to run 3 times, then stop
    when(loopPolicy.continueRunning(eq(0), anyLong())).thenReturn(true);
    when(loopPolicy.continueRunning(eq(1), anyLong())).thenReturn(true);
    when(loopPolicy.continueRunning(eq(2), anyLong())).thenReturn(true);
    when(loopPolicy.continueRunning(eq(3), anyLong())).thenReturn(false); // Stop after 3 ticks

    // Act
    engine.run();

    // Assert
    // Verify init and shutdown are called once
    verify(serviceHighPriority, times(1)).start();
    verify(serviceHighPriority, times(1)).stop();

    // Verify tick() was called 3 times (for frames 0, 1, 2)
    verify(windowService, times(3)).pollEvents();
    verify(windowService, times(3)).swapBuffers();
    verify(serviceHighPriority, times(3)).update();
    verify(serviceMediumPriority, times(3)).update();
    verify(serviceLowPriority, times(3)).update();
  }

  @Test
  void run_shutsDownIfInitFails() {
    // Arrange
    doThrow(new RuntimeException("Service failed to start")).when(serviceHighPriority).start();

    // Act & Assert
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> engine.run());
    // Verify that the exception from init is rethrown
    assert(thrown.getMessage().contains("Engine initialization failed"));

    // Verify that shutdown was called on services that might have started.
    // In this case, serviceHighPriority failed to start, but shutdown() is still called in finally.
    verify(serviceHighPriority, times(1)).start(); // Attempted to start
    verify(serviceHighPriority, times(1)).stop(); // stop() is called in finally block
    verify(serviceMediumPriority, never()).start(); // Never started
    verify(serviceLowPriority, never()).start(); // Never started

    // If serviceMediumPriority had started before serviceHighPriority failed, it would also be stopped.
    // For this specific test, we only expect serviceHighPriority to be affected.
  }
}
