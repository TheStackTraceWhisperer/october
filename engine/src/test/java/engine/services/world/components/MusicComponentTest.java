package engine.services.world.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MusicComponentTest {

    private MusicComponent music;

    @BeforeEach
    void setUp() {
        music = new MusicComponent("test_music");
    }

    @Test
    void constructor_withHandle_shouldSetDefaults() {
        assertEquals("test_music", music.musicBufferHandle);
        assertEquals(1.0f, music.baseVolume);
        assertEquals(1.0f, music.currentVolume);
        assertTrue(music.looping);
        assertTrue(music.autoPlay);
        assertEquals(2.0f, music.fadeDuration);
    }

    @Test
    void constructor_withHandleAndVolume_shouldSetCorrectly() {
        music = new MusicComponent("test_music", 0.5f);
        assertEquals(0.5f, music.baseVolume);
        assertEquals(0.5f, music.currentVolume);
    }

    @Test
    void constructor_withHandleVolumeAndLooping_shouldSetCorrectly() {
        music = new MusicComponent("test_music", 0.7f, false);
        assertEquals(0.7f, music.baseVolume);
        assertEquals(0.7f, music.currentVolume);
        assertFalse(music.looping);
    }

    @Test
    void constructor_withAllParameters_shouldSetCorrectly() {
        music = new MusicComponent("test_music", 0.9f, false, 5.0f);
        assertEquals(0.9f, music.baseVolume);
        assertEquals(0.9f, music.currentVolume);
        assertFalse(music.looping);
        assertEquals(5.0f, music.fadeDuration);
    }

    @Test
    void startFadeIn_shouldSetFadingFlagsAndResetVolume() {
        music.currentVolume = 0.5f; // Simulate a different state
        music.startFadeIn();

        assertTrue(music.fadingIn);
        assertFalse(music.fadingOut);
        assertEquals(0.0f, music.fadeTimer);
        assertEquals(0.0f, music.currentVolume);
    }

    @Test
    void startFadeOut_shouldSetFadingFlags() {
        music.fadingIn = true; // Simulate a different state
        music.startFadeOut();

        assertFalse(music.fadingIn);
        assertTrue(music.fadingOut);
        assertEquals(0.0f, music.fadeTimer);
    }

    @Test
    void stopFade_shouldResetAllFadingAndSetVolumeToBase() {
        music.baseVolume = 0.8f;
        music.currentVolume = 0.2f;
        music.fadingIn = true;
        music.fadeTimer = 1.0f;

        music.stopFade();

        assertFalse(music.fadingIn);
        assertFalse(music.fadingOut);
        assertEquals(0.0f, music.fadeTimer);
        assertEquals(0.8f, music.currentVolume);
    }
}
