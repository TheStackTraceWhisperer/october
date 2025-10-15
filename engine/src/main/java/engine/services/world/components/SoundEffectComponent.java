package engine.services.world.components;

import engine.ecs.IComponent;

public class SoundEffectComponent implements IComponent {

  public interface SoundEffectType {
  }

  public String soundBufferHandle;

  public SoundEffectType soundType;

  public float volume = 1.0f;

  public float pitch = 1.0f;

  public boolean autoPlay = true;

  public boolean removeAfterPlay = true;

  public transient boolean hasBeenTriggered = false;

  public SoundEffectComponent(String soundBufferHandle, SoundEffectType soundType) {
    this.soundBufferHandle = soundBufferHandle;
    this.soundType = soundType;
  }

  public SoundEffectComponent(String soundBufferHandle, SoundEffectType soundType, float volume) {
    this.soundBufferHandle = soundBufferHandle;
    this.soundType = soundType;
    this.volume = volume;
  }

  public SoundEffectComponent(String soundBufferHandle, SoundEffectType soundType, float volume, float pitch) {
    this.soundBufferHandle = soundBufferHandle;
    this.soundType = soundType;
    this.volume = volume;
    this.pitch = pitch;
  }
}