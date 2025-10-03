package engine.services.time;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SystemTimeServiceTest {

    @Test
    void testTimeService() throws InterruptedException {
        SystemTimeService timeService = new SystemTimeService();
        timeService.start();

        // First update
        Thread.sleep(100);
        timeService.update();
        float deltaTime1 = timeService.getDeltaTimeSeconds();
        double totalTime1 = timeService.getTotalTimeSeconds();

        assertTrue(deltaTime1 > 0.09 && deltaTime1 < 0.11, "Delta time should be around 0.1 seconds");
        assertTrue(totalTime1 > 0.09 && totalTime1 < 0.11, "Total time should be around 0.1 seconds");

        // Second update
        Thread.sleep(50);
        timeService.update();
        float deltaTime2 = timeService.getDeltaTimeSeconds();
        double totalTime2 = timeService.getTotalTimeSeconds();

        assertTrue(deltaTime2 > 0.04 && deltaTime2 < 0.06, "Delta time should be around 0.05 seconds");
        assertTrue(totalTime2 > 0.14 && totalTime2 < 0.16, "Total time should be around 0.15 seconds");
    }
}
