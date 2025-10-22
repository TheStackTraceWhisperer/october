package engine.services.world.systems;

import engine.services.world.World;
import engine.services.world.components.ActiveSequenceComponent;
import engine.services.zone.SimpleZone;
import engine.services.zone.Zone;
import engine.services.zone.ZoneLoadedEvent;
import engine.services.zone.ZoneService;
import engine.services.zone.sequence.GameEvent;
import engine.services.zone.sequence.SimpleGameEvent;
import engine.services.zone.sequence.SimpleTrigger;
import engine.services.zone.sequence.Trigger;
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
class TriggerSystemTest {

  @Mock
  private ZoneService zoneService;

  private World world;
  private TriggerSystem triggerSystem;

  @BeforeEach
  void setUp() {
    world = new World();
    triggerSystem = new TriggerSystem(zoneService, world);
  }

  @Test
  void testOnLoadTriggerWithNoDelay() {
    // Given a zone with an ON_LOAD trigger with no delay
    GameEvent startSequenceEvent = SimpleGameEvent.builder()
      .type("START_SEQUENCE")
      .properties(Map.of("sequenceId", "test_sequence"))
      .build();

    Trigger onLoadTrigger = SimpleTrigger.builder()
      .id("trigger1")
      .type("ON_LOAD")
      .events(List.of(startSequenceEvent))
      .properties(new HashMap<>())
      .build();

    Zone zone = SimpleZone.builder()
      .id("test_zone")
      .name("Test Zone")
      .triggers(List.of(onLoadTrigger))
      .sequences(List.of())
      .properties(new HashMap<>())
      .build();

    when(zoneService.getCurrentZone()).thenReturn(zone);

    // When we simulate a zone loaded event
    triggerSystem.onApplicationEvent(new ZoneLoadedEvent(zone));

    // And update the trigger system
    triggerSystem.update(world, 0.01f);

    // Then an ActiveSequence component should be created
    var entities = world.getEntitiesWith(ActiveSequenceComponent.class);
    assertThat(entities).hasSize(1);

    int entityId = entities.iterator().next();
    ActiveSequenceComponent component = world.getComponent(entityId, ActiveSequenceComponent.class);
    assertThat(component.getSequenceId()).isEqualTo("test_sequence");
  }

  @Test
  void testOnLoadTriggerWithDelay() {
    // Given a zone with an ON_LOAD trigger with a 2 second delay
    GameEvent startSequenceEvent = SimpleGameEvent.builder()
      .type("START_SEQUENCE")
      .properties(Map.of("sequenceId", "delayed_sequence"))
      .build();

    Map<String, Object> triggerProperties = new HashMap<>();
    triggerProperties.put("delay", 2.0);

    Trigger onLoadTrigger = SimpleTrigger.builder()
      .id("trigger2")
      .type("ON_LOAD")
      .events(List.of(startSequenceEvent))
      .properties(triggerProperties)
      .build();

    Zone zone = SimpleZone.builder()
      .id("test_zone")
      .name("Test Zone")
      .triggers(List.of(onLoadTrigger))
      .sequences(List.of())
      .properties(new HashMap<>())
      .build();

    when(zoneService.getCurrentZone()).thenReturn(zone);

    // When we simulate a zone loaded event
    triggerSystem.onApplicationEvent(new ZoneLoadedEvent(zone));

    // And update the trigger system with only 1 second elapsed
    triggerSystem.update(world, 1.0f);

    // Then no ActiveSequence component should be created yet
    var entities = world.getEntitiesWith(ActiveSequenceComponent.class);
    assertThat(entities).isEmpty();

    // When we update with another 1.5 seconds (total 2.5 seconds)
    triggerSystem.update(world, 1.5f);

    // Then an ActiveSequence component should be created
    entities = world.getEntitiesWith(ActiveSequenceComponent.class);
    assertThat(entities).hasSize(1);
  }

  @Test
  void testNonRepeatableTriggerFiresOnlyOnce() {
    // Given a zone with a non-repeatable ON_LOAD trigger
    GameEvent startSequenceEvent = SimpleGameEvent.builder()
      .type("START_SEQUENCE")
      .properties(Map.of("sequenceId", "once_sequence"))
      .build();

    Map<String, Object> triggerProperties = new HashMap<>();
    triggerProperties.put("isRepeatable", false);

    Trigger onLoadTrigger = SimpleTrigger.builder()
      .id("trigger3")
      .type("ON_LOAD")
      .events(List.of(startSequenceEvent))
      .properties(triggerProperties)
      .build();

    Zone zone = SimpleZone.builder()
      .id("test_zone")
      .name("Test Zone")
      .triggers(List.of(onLoadTrigger))
      .sequences(List.of())
      .properties(new HashMap<>())
      .build();

    when(zoneService.getCurrentZone()).thenReturn(zone);

    // When we simulate a zone loaded event and update twice
    triggerSystem.onApplicationEvent(new ZoneLoadedEvent(zone));
    triggerSystem.update(world, 0.01f);
    triggerSystem.update(world, 0.01f);

    // Then only one ActiveSequence component should be created
    var entities = world.getEntitiesWith(ActiveSequenceComponent.class);
    assertThat(entities).hasSize(1);
  }

  @Test
  void testUpdateWithNoZoneDoesNothing() {
    // Given no zone is loaded
    when(zoneService.getCurrentZone()).thenReturn(null);

    // When we update the trigger system
    triggerSystem.update(world, 0.01f);

    // Then no entities should be created
    var entities = world.getEntitiesWith(ActiveSequenceComponent.class);
    assertThat(entities).isEmpty();
  }
}
