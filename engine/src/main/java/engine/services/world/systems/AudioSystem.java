package engine.services.world.systems;

import api.ecs.ISystem;
import api.ecs.IWorld;
import engine.services.audio.AudioBuffer;
import engine.services.audio.AudioService;
import engine.services.audio.AudioSource;
import engine.services.resources.AssetCacheService;
import engine.services.world.WorldService;
import engine.services.world.components.AudioSourceComponent;
import engine.services.world.components.MusicComponent;
import engine.services.world.components.SoundEffectComponent;
import engine.services.world.components.TransformComponent;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The system responsible for managing all audio playback in the engine.
 * <p>
 * This system handles:
 * - Positioned 3D audio sources (AudioSourceComponent)
 * - Background music with fading effects (MusicComponent)
 * - One-shot sound effects (SoundEffectComponent)
 * <p>
 * The system creates and manages OpenAL audio sources for each audio component,
 * applies volume and fade effects, and cleans up finished audio automatically.
 */
@RequiredArgsConstructor
public class AudioSystem implements ISystem {

  //private final IWorld world;
  //private final WorldService worldService;
  private final AudioService audioManager;
  private final AssetCacheService resourceManager;

  // Maps entity IDs to their associated AudioSource instances
  private final Map<Integer, AudioSource> audioSourceMap = new HashMap<>();
  private final Map<Integer, AudioSource> musicSourceMap = new HashMap<>();
  private final Map<Integer, AudioSource> soundEffectSourceMap = new HashMap<>();

  @Override
  public void update(IWorld world, float deltaTime) {
    updateAudioSources(world, deltaTime);
    updateMusic(world, deltaTime);
    updateSoundEffects(world, deltaTime);
  }

  /**
   * Updates all entities with AudioSourceComponent.
   */
  private void updateAudioSources(IWorld world, float deltaTime) {
    var entities = world.getEntitiesWith(AudioSourceComponent.class);

    for (int entityId : entities) {
      AudioSourceComponent audioComp = world.getComponent(entityId, AudioSourceComponent.class);
      AudioSource audioSource = audioSourceMap.get(entityId);

      // Create audio source if it doesn't exist
      if (audioSource == null) {
        audioSource = audioManager.createSource();
        audioSourceMap.put(entityId, audioSource);

        // Set initial properties
        audioSource.setVolume(audioComp.volume);
        audioSource.setPitch(audioComp.pitch);
        audioSource.setLooping(audioComp.looping);

        // Start playing if auto-play is enabled
        if (audioComp.autoPlay && !audioComp.isPlaying) {
          AudioBuffer buffer = resourceManager.resolveAudioBufferHandle(audioComp.audioBufferHandle);
          audioSource.play(buffer);
          audioComp.isPlaying = true;
        }
      }

      // Update position if entity has a transform
      TransformComponent transform = world.getComponent(entityId, TransformComponent.class);
      if (transform != null) {
        audioSource.setPosition(transform.position);
      }

      // Update audio source properties
      audioSource.setVolume(audioComp.volume);
      audioSource.setPitch(audioComp.pitch);
      audioSource.setLooping(audioComp.looping);

      // Update playing state
      audioComp.isPlaying = audioSource.isPlaying();

      // Clean up finished non-looping sounds
      if (!audioComp.looping && audioSource.isStopped() && audioComp.isPlaying) {
        audioComp.isPlaying = false;
      }
    }

    // Clean up audio sources for entities that no longer have AudioSourceComponent
    cleanupAudioSources(entities, audioSourceMap);
  }

  /**
   * Updates all entities with MusicComponent.
   */
  private void updateMusic(IWorld world, float deltaTime) {
    var entities = world.getEntitiesWith(MusicComponent.class);

    for (int entityId : entities) {
      MusicComponent musicComp = world.getComponent(entityId, MusicComponent.class);
      AudioSource musicSource = musicSourceMap.get(entityId);

      // Create music source if it doesn't exist
      if (musicSource == null) {
        musicSource = audioManager.createSource();
        musicSourceMap.put(entityId, musicSource);

        // Music sources are not positioned in 3D space
        musicSource.setPosition(0.0f, 0.0f, 0.0f);
        musicSource.setLooping(musicComp.looping);

        // Start playing if auto-play is enabled
        if (musicComp.autoPlay && !musicComp.isPlaying) {
          AudioBuffer buffer = resourceManager.resolveAudioBufferHandle(musicComp.musicBufferHandle);
          musicSource.play(buffer);
          musicComp.isPlaying = true;

          // Start with fade-in if not already fading
          if (!musicComp.fadingIn && !musicComp.fadingOut) {
            musicComp.startFadeIn();
          }
        }
      }

      // Handle fade effects
      updateMusicFades(musicComp, musicSource, deltaTime);

      // Update music source properties
      musicSource.setVolume(musicComp.currentVolume);
      musicSource.setLooping(musicComp.looping);

      // Update playing state
      musicComp.isPlaying = musicSource.isPlaying();
      musicComp.isPaused = musicSource.isPaused();

      // Clean up finished non-looping music
      if (!musicComp.looping && musicSource.isStopped() && musicComp.isPlaying) {
        musicComp.isPlaying = false;
      }
    }

    // Clean up music sources for entities that no longer have MusicComponent
    cleanupAudioSources(entities, musicSourceMap);
  }

  /**
   * Updates fade effects for a music component.
   */
  private void updateMusicFades(MusicComponent musicComp, AudioSource musicSource, float deltaTime) {
    if (musicComp.fadingIn) {
      musicComp.fadeTimer += deltaTime;
      float fadeProgress = Math.min(musicComp.fadeTimer / musicComp.fadeDuration, 1.0f);
      musicComp.currentVolume = musicComp.baseVolume * fadeProgress;

      if (fadeProgress >= 1.0f) {
        musicComp.fadingIn = false;
        musicComp.fadeTimer = 0.0f;
        musicComp.currentVolume = musicComp.baseVolume;
      }
    } else if (musicComp.fadingOut) {
      musicComp.fadeTimer += deltaTime;
      float fadeProgress = Math.min(musicComp.fadeTimer / musicComp.fadeDuration, 1.0f);
      musicComp.currentVolume = musicComp.baseVolume * (1.0f - fadeProgress);

      if (fadeProgress >= 1.0f) {
        musicComp.fadingOut = false;
        musicComp.fadeTimer = 0.0f;
        musicComp.currentVolume = 0.0f;
        musicSource.stop();
        musicComp.isPlaying = false;
      }
    }
  }

  /**
   * Updates all entities with SoundEffectComponent.
   */
  private void updateSoundEffects(IWorld world, float deltaTime) {
    List<Integer> entitiesToCleanup = new ArrayList<>();
    var entities = world.getEntitiesWith(SoundEffectComponent.class);

    for (int entityId : entities) {
      SoundEffectComponent soundComp = world.getComponent(entityId, SoundEffectComponent.class);
      AudioSource soundSource = soundEffectSourceMap.get(entityId);

      if (soundSource == null && !soundComp.hasBeenTriggered) {
        soundSource = audioManager.createSource();
        soundEffectSourceMap.put(entityId, soundSource);

        soundSource.setVolume(soundComp.volume);
        soundSource.setPitch(soundComp.pitch);
        soundSource.setLooping(false);

        if (soundComp.autoPlay) {
          AudioBuffer buffer = resourceManager.resolveAudioBufferHandle(soundComp.soundBufferHandle);
          soundSource.play(buffer);
          soundComp.hasBeenTriggered = true;
        }
      }

      if (soundSource != null && soundSource.isStopped() && soundComp.hasBeenTriggered) {
        if (soundComp.removeAfterPlay) {
          entitiesToCleanup.add(entityId);
        }
      }
    }

    // Perform cleanup after iteration to avoid concurrent modification issues
    for (int entityId : entitiesToCleanup) {
      AudioSource soundSource = soundEffectSourceMap.remove(entityId);
      if (soundSource != null) {
        soundSource.close();
      }
      world.removeComponent(entityId, SoundEffectComponent.class);
    }

    // Final cleanup pass for any sources whose entities were removed by other means
    cleanupAudioSources(world.getEntitiesWith(SoundEffectComponent.class), soundEffectSourceMap);
  }


  /**
   * Cleans up audio sources for entities that no longer have the corresponding audio component.
   */
  private void cleanupAudioSources(List<Integer> currentEntities, Map<Integer, AudioSource> sourceMap) {
    var currentEntitySet = new java.util.HashSet<>(currentEntities);
    sourceMap.entrySet().removeIf(entry -> {
      if (!currentEntitySet.contains(entry.getKey())) {
        entry.getValue().close();
        return true;
      }
      return false;
    });
  }

  /**
   * Plays a sound effect on the specified entity.
   * This is a convenience method for triggering sound effects programmatically.
   *
   * @param entityId     The entity to play the sound on
   * @param bufferHandle The audio buffer handle
   * @param soundType    The type of sound effect
   * @param volume       The volume level
   */
  public void playSoundEffect(IWorld world, int entityId, String bufferHandle,
                              SoundEffectComponent.SoundEffectType soundType, float volume) {
    SoundEffectComponent soundComp = new SoundEffectComponent(bufferHandle, soundType, volume);
    world.addComponent(entityId, soundComp);
  }

  /**
   * Starts fade-out for all music components.
   */
  public void fadeOutAllMusic(IWorld world) {
    var entities = world.getEntitiesWith(MusicComponent.class);
    for (int entityId : entities) {
      MusicComponent musicComp = world.getComponent(entityId, MusicComponent.class);
      musicComp.startFadeOut();
    }
  }

  /**
   * Pauses all music components.
   */
  public void pauseAllMusic() {
    for (AudioSource musicSource : musicSourceMap.values()) {
      if (musicSource.isPlaying()) {
        musicSource.pause();
      }
    }
  }

  /**
   * Resumes all paused music components.
   */
  public void resumeAllMusic() {
    for (AudioSource musicSource : musicSourceMap.values()) {
      if (musicSource.isPaused()) {
        musicSource.resume();
      }
    }
  }

  /**
   * Stops all audio playback and cleans up resources.
   */
  public void stopAll() {
    audioSourceMap.values().forEach(AudioSource::close);
    audioSourceMap.clear();

    musicSourceMap.values().forEach(AudioSource::close);
    musicSourceMap.clear();

    soundEffectSourceMap.values().forEach(AudioSource::close);
    soundEffectSourceMap.clear();
  }
}
