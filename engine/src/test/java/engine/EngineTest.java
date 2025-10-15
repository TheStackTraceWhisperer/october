package engine;

import engine.services.state.ApplicationStateService;
import engine.services.time.SystemTimeService;
import engine.services.window.WindowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
  private IService service1;
  @Mock
  private IService service2;

  private Engine engine;

  @BeforeEach
  void setUp() {
    // The Engine requires a mutable list to sort, so we must provide an ArrayList.
    List<IService> mutableServices = new ArrayList<>(List.of(service1, service2));
    engine = new Engine(loopPolicy, applicationStateService, mutableServices, windowService, systemTimeService);
  }

  @Test
  void init_whenCalledMultipleTimes_onlyInitializesServicesOnce() {
    // Act
    engine.init();
    engine.init(); // Second call should be ignored

    // Assert
    // Verify that the start method on each service was called exactly once.
    verify(service1, times(1)).start();
    verify(service2, times(1)).start();
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
    verify(service1, times(1)).stop();
    verify(service2, times(1)).stop();
  }

  @Test
  void tick_whenEngineNotInitialized_throwsIllegalStateException() {
    // Assert
    // Verify that calling tick() on a new engine throws the correct exception.
    assertThrows(IllegalStateException.class, () -> engine.tick());
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
    verify(service1, times(1)).start();
    verify(service1, times(1)).stop();
  }
}
