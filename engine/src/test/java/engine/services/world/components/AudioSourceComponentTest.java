package engine.services.world.components;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AudioSourceComponentTest {

    @Test
    void constructor_withHandle_shouldSetDefaults() {
        AudioSourceComponent audio = new AudioSourceComponent("test_sound");

        assertEquals("test_sound", audio.audioBufferHandle);
        assertEquals(1.0f, audio.volume);
        assertEquals(1.0f, audio.pitch);
        assertFalse(audio.looping);
        assertFalse(audio.autoPlay);
        assertFalse(audio.isPlaying);
    }

    @Test
    void constructor_withHandleAndVolume_shouldSetCorrectly() {
        AudioSourceComponent audio = new AudioSourceComponent("test_sound", 0.5f);

        assertEquals("test_sound", audio.audioBufferHandle);
        assertEquals(0.5f, audio.volume);
        assertEquals(1.0f, audio.pitch);
        assertFalse(audio.looping);
        assertFalse(audio.autoPlay);
    }

    @Test
    void constructor_withHandleVolumeAndLooping_shouldSetCorrectly() {
        AudioSourceComponent audio = new AudioSourceComponent("test_sound", 0.7f, true);

        assertEquals("test_sound", audio.audioBufferHandle);
        assertEquals(0.7f, audio.volume);
        assertTrue(audio.looping);
        assertFalse(audio.autoPlay);
    }

    @Test
    void constructor_withAllParameters_shouldSetCorrectly() {
        AudioSourceComponent audio = new AudioSourceComponent("test_sound", 0.9f, true, true);

        assertEquals("test_sound", audio.audioBufferHandle);
        assertEquals(0.9f, audio.volume);
        assertTrue(audio.looping);
        assertTrue(audio.autoPlay);
    }
}
