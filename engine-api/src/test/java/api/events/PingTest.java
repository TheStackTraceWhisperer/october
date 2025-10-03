package api.events;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PingTest {

  @Test
  void testPingRecord() {
    // This is a simple unit test for a data record.
    // It verifies that the object is constructed correctly and the accessor method returns the expected value.
    Ping ping = new Ping("test message");
    assertThat(ping.message()).isEqualTo("test message");
  }
}
