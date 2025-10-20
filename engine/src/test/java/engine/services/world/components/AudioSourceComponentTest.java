package engine.services.world.components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AudioSourceComponentTest {

    @Test
    void constructor_shouldSetHandleAndDefaults() {
        // Given: The component is created with the only valid constructor
        AudioSourceComponent audio = new AudioSourceComponent("test_sound");

        // Then: The handle should be set and all other fields should have their correct default values
        assertEquals("test_sound", audio.audioBufferHandle);
        assertEquals(1.0f, audio.volume);
        assertEquals(1.0f, audio.pitch);
        assertFalse(audio.looping);
        assertFalse(audio.autoPlay);
        assertFalse(audio.isPlaying);
    }
}
