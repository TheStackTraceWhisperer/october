package engine.services.world.systems;

import engine.EngineTestHarness;
import engine.services.audio.AudioSource;
import engine.services.resources.AssetCacheService;
import engine.services.world.WorldService;
import engine.services.world.components.MusicComponent;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class AudioSystemIT extends EngineTestHarness {

    @Inject
    private WorldService worldService;
    @Inject
    private AssetCacheService assetCacheService;
    @Inject
    private AudioSystem audioSystem;

    @BeforeEach
    void setUp() {
        worldService.addSystem(audioSystem);
        assetCacheService.loadAudioBuffer("test-music", "audio/test-music.ogg");
    }

    @AfterEach
    void tearDown() {
        audioSystem.stopAll();
        worldService.clearSystems();
        worldService.getEntitiesWith().forEach(worldService::destroyEntity);
    }

    @Test
    void testMusicComponent_autoPlayAndLooping() {
        // Given
        int entity = worldService.createEntity();
        // Correctly use the Lombok-generated constructor
        MusicComponent music = new MusicComponent("test-music");
        // Set other properties on the instance
        music.looping = true;
        music.autoPlay = true;
        worldService.addComponent(entity, music);

        // When
        tick();

        // Then
        assertTrue(music.isPlaying, "MusicComponent should be playing after one tick.");

        // When we tick again, it should still be playing because it's looping
        tick();
        assertTrue(music.isPlaying, "Looping music should still be playing after a second tick.");
    }

    @Test
    void testSystem_cleansUpSourceWhenComponentIsRemoved() throws Exception {
        // Given: An entity with music that is playing
        int entity = worldService.createEntity();
        worldService.addComponent(entity, new MusicComponent("test-music"));
        tick(); // Tick once to create and play the internal AudioSource

        // And: We can see the internal source was created
        Map<Integer, AudioSource> musicSourceMap = getInternalMusicSourceMap();
        assertFalse(musicSourceMap.isEmpty(), "Internal music source should be created.");
        AudioSource source = musicSourceMap.get(entity);
        assertNotNull(source, "AudioSource should exist in the system's map.");
        assertFalse(source.isClosed(), "AudioSource should be open.");

        // When: The component is removed from the entity
        worldService.removeComponent(entity, MusicComponent.class);
        tick(); // Tick again to allow the system's cleanup logic to run

        // Then: The internal map should be empty and the source should be closed
        assertTrue(musicSourceMap.isEmpty(), "Internal music source should be removed after component is removed.");
        assertTrue(source.isClosed(), "AudioSource should be closed after component is removed.");
    }

    @Test
    void testPauseAndResumeAllMusic() throws Exception {
        // Given: An entity with music that is playing
        int entity = worldService.createEntity();
        worldService.addComponent(entity, new MusicComponent("test-music"));
        tick(); // Start playback

        Map<Integer, AudioSource> musicSourceMap = getInternalMusicSourceMap();
        AudioSource source = musicSourceMap.get(entity);
        assertTrue(source.isPlaying(), "Source should be playing initially.");

        // When: We pause all music
        audioSystem.pauseAllMusic();

        // Then: The underlying source should be paused
        assertTrue(source.isPaused(), "Source should be paused.");

        // When: We resume all music
        audioSystem.resumeAllMusic();

        // Then: The underlying source should be playing again
        assertTrue(source.isPlaying(), "Source should be playing again after resume.");
    }

    /**
     * Uses reflection to access the private source map for verification.
     * This is an acceptable testing practice for verifying internal state management.
     */
    @SuppressWarnings("unchecked")
    private Map<Integer, AudioSource> getInternalMusicSourceMap() throws NoSuchFieldException, IllegalAccessException {
        Field field = AudioSystem.class.getDeclaredField("musicSourceMap");
        field.setAccessible(true);
        return (Map<Integer, AudioSource>) field.get(audioSystem);
    }
}
