package engine.services.audio;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AudioBufferTest {

  @Test
  void loadFromOggFile_throwsForMissingResource() {
    RuntimeException ex =
        assertThrows(
            RuntimeException.class, () -> AudioBuffer.loadFromOggFile("audio/does-not-exist.ogg"));
    assertTrue(ex.getMessage().contains("Failed to load audio file"));
  }
}
