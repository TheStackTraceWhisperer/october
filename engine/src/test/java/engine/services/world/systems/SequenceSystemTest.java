package engine.services.world.systems;

import engine.services.world.World;
import engine.services.world.components.ActiveSequenceComponent;
import engine.services.zone.SimpleZone;
import engine.services.zone.Zone;
import engine.services.zone.ZoneService;
import engine.services.zone.sequence.GameEvent;
import engine.services.zone.sequence.Sequence;
import engine.services.zone.sequence.SimpleGameEvent;
import engine.services.zone.sequence.SimpleSequence;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SequenceSystemTest {

  @Mock
  private ZoneService zoneService;

  private World world;
  private SequenceSystem sequenceSystem;

  @BeforeEach
  void setUp() {
    world = new World();
    sequenceSystem = new SequenceSystem(zoneService);
  }

  @Test
  void testWaitEventDecreasesTimer() {
    // Given a sequence with a WAIT event
    GameEvent waitEvent = SimpleGameEvent.builder()
      .type("WAIT")
      .properties(Map.of("duration", 2.0))
      .build();

    Sequence sequence = SimpleSequence.builder()
      .id("wait_sequence")
      .events(List.of(waitEvent))
      .build();

    Zone zone = SimpleZone.builder()
      .id("test_zone")
      .name("Test Zone")
      .sequences(List.of(sequence))
      .triggers(List.of())
      .properties(new HashMap<>())
      .build();

    when(zoneService.getCurrentZone()).thenReturn(zone);

    // When we create an entity with ActiveSequenceComponent
    int entityId = world.createEntity();
    ActiveSequenceComponent component = new ActiveSequenceComponent("wait_sequence");
    world.addComponent(entityId, component);

    // And update the sequence system once to process the WAIT event
    sequenceSystem.update(world, 0.01f);

    // Then the wait timer should be set
    assertThat(component.getWaitTimer()).isGreaterThan(1.9f);
    assertThat(component.getCurrentIndex()).isEqualTo(0);

    // When we update with 1.5 seconds
    sequenceSystem.update(world, 1.5f);

    // Then the timer should decrease
    assertThat(component.getWaitTimer()).isLessThan(1.0f);
    assertThat(component.getCurrentIndex()).isEqualTo(0);

    // When we update with another 1 second
    sequenceSystem.update(world, 1.0f);

    // Then the timer should reach 0 and index should advance
    assertThat(component.getWaitTimer()).isEqualTo(0.0f);
    assertThat(component.getCurrentIndex()).isEqualTo(1);
  }

  @Test
  void testInstantActionAdvancesIndex() {
    // Given a sequence with a PLAY_SOUND event
    GameEvent playSoundEvent = SimpleGameEvent.builder()
      .type("PLAY_SOUND")
      .properties(Map.of("soundId", "test_sound"))
      .build();

    Sequence sequence = SimpleSequence.builder()
      .id("sound_sequence")
      .events(List.of(playSoundEvent))
      .build();

    Zone zone = SimpleZone.builder()
      .id("test_zone")
      .name("Test Zone")
      .sequences(List.of(sequence))
      .triggers(List.of())
      .properties(new HashMap<>())
      .build();

    when(zoneService.getCurrentZone()).thenReturn(zone);

    // When we create an entity with ActiveSequenceComponent
    int entityId = world.createEntity();
    ActiveSequenceComponent component = new ActiveSequenceComponent("sound_sequence");
    world.addComponent(entityId, component);

    // And update the sequence system
    sequenceSystem.update(world, 0.01f);

    // Then the index should advance immediately
    assertThat(component.getCurrentIndex()).isEqualTo(1);
  }

  @Test
  void testSequenceCompletionRemovesComponent() {
    // Given a sequence with a single instant event
    GameEvent playSoundEvent = SimpleGameEvent.builder()
      .type("PLAY_SOUND")
      .properties(Map.of("soundId", "test_sound"))
      .build();

    Sequence sequence = SimpleSequence.builder()
      .id("complete_sequence")
      .events(List.of(playSoundEvent))
      .build();

    Zone zone = SimpleZone.builder()
      .id("test_zone")
      .name("Test Zone")
      .sequences(List.of(sequence))
      .triggers(List.of())
      .properties(new HashMap<>())
      .build();

    when(zoneService.getCurrentZone()).thenReturn(zone);

    // When we create an entity with ActiveSequenceComponent
    int entityId = world.createEntity();
    world.addComponent(entityId, new ActiveSequenceComponent("complete_sequence"));

    // And update the sequence system to execute the event
    sequenceSystem.update(world, 0.01f);

    // Then the component should advance to index 1
    var component = world.getComponent(entityId, ActiveSequenceComponent.class);
    assertThat(component.getCurrentIndex()).isEqualTo(1);

    // When we update again
    sequenceSystem.update(world, 0.01f);

    // Then the entity should be destroyed (sequence complete)
    var entities = world.getEntitiesWith(ActiveSequenceComponent.class);
    assertThat(entities).isEmpty();
  }

  @Test
  void testBlockedSequenceDoesNotAdvance() {
    // Given a sequence with a MOVE_ENTITY event
    GameEvent moveEvent = SimpleGameEvent.builder()
      .type("MOVE_ENTITY")
      .properties(Map.of("entityId", "player"))
      .build();

    Sequence sequence = SimpleSequence.builder()
      .id("move_sequence")
      .events(List.of(moveEvent))
      .build();

    Zone zone = SimpleZone.builder()
      .id("test_zone")
      .name("Test Zone")
      .sequences(List.of(sequence))
      .triggers(List.of())
      .properties(new HashMap<>())
      .build();

    when(zoneService.getCurrentZone()).thenReturn(zone);

    // When we create an entity with ActiveSequenceComponent
    int entityId = world.createEntity();
    ActiveSequenceComponent component = new ActiveSequenceComponent("move_sequence");
    world.addComponent(entityId, component);

    // And update the sequence system
    sequenceSystem.update(world, 0.01f);

    // Then the component should be blocked
    assertThat(component.isBlocked()).isTrue();
    assertThat(component.getCurrentIndex()).isEqualTo(0);

    // When we update again while still blocked
    sequenceSystem.update(world, 0.01f);

    // Then the index should not advance
    assertThat(component.getCurrentIndex()).isEqualTo(0);
  }

  @Test
  void testUpdateWithNoZoneDoesNothing() {
    // Given no zone is loaded
    when(zoneService.getCurrentZone()).thenReturn(null);

    // When we create an entity with ActiveSequenceComponent
    int entityId = world.createEntity();
    world.addComponent(entityId, new ActiveSequenceComponent("test_sequence"));

    // And update the sequence system
    sequenceSystem.update(world, 0.01f);

    // Then the entity should still exist (nothing happened)
    var entities = world.getEntitiesWith(ActiveSequenceComponent.class);
    assertThat(entities).hasSize(1);
  }

  @Test
  void testUnknownEventTypeAdvancesIndex() {
    // Given a sequence with an unknown event type
    GameEvent unknownEvent = SimpleGameEvent.builder()
      .type("UNKNOWN_TYPE")
      .properties(new HashMap<>())
      .build();

    Sequence sequence = SimpleSequence.builder()
      .id("unknown_sequence")
      .events(List.of(unknownEvent))
      .build();

    Zone zone = SimpleZone.builder()
      .id("test_zone")
      .name("Test Zone")
      .sequences(List.of(sequence))
      .triggers(List.of())
      .properties(new HashMap<>())
      .build();

    when(zoneService.getCurrentZone()).thenReturn(zone);

    // When we create an entity with ActiveSequenceComponent
    int entityId = world.createEntity();
    ActiveSequenceComponent component = new ActiveSequenceComponent("unknown_sequence");
    world.addComponent(entityId, component);

    // And update the sequence system
    sequenceSystem.update(world, 0.01f);

    // Then the index should advance (to avoid getting stuck)
    assertThat(component.getCurrentIndex()).isEqualTo(1);
  }
}
