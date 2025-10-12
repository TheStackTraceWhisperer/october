package engine.services.audio;

import engine.EngineTestHarness;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AudioServiceIT extends EngineTestHarness {

  @Inject AudioService audio;

  @Test
  void audioServiceInitializesOrGracefullyDisables() {
    // On machines without an audio device, AudioService leaves initialized=false and methods should throw.
    if (audio.isInitialized()) {
      audio.setMasterVolume(0.5f);
      assertThat(audio.getMasterVolume()).isGreaterThanOrEqualTo(0.0f);
    } else {
      assertThatThrownBy(() -> audio.setMasterVolume(0.5f)).isInstanceOf(IllegalStateException.class);
    }
  }
}
