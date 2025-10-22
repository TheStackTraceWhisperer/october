package engine.services.world.systems;

import engine.services.world.ISystem;
import engine.services.world.World;
import engine.services.world.components.ActiveSequenceComponent;
import engine.services.zone.Zone;
import engine.services.zone.ZoneService;
import engine.services.zone.sequence.GameEvent;
import engine.services.zone.sequence.Sequence;
import io.micronaut.context.annotation.Prototype;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A stateless system that interprets ActiveSequence components and executes GameEvent commands.
 */
@Slf4j
@Prototype
@RequiredArgsConstructor
public class SequenceSystem implements ISystem {
  private final ZoneService zoneService;

  @Override
  public void update(World world, float deltaTime) {
    Zone currentZone = zoneService.getCurrentZone();
    if (currentZone == null) {
      return;
    }

    // Query the ECS for all entities with an ActiveSequence component
    Set<Integer> entities = world.getEntitiesWith(ActiveSequenceComponent.class);
    
    // Track entities to remove (to avoid concurrent modification)
    Set<Integer> entitiesToRemove = new HashSet<>();

    for (int entityId : entities) {
      ActiveSequenceComponent activeSequence = world.getComponent(entityId, ActiveSequenceComponent.class);
      
      // Update wait timer if waiting
      if (activeSequence.getWaitTimer() > 0) {
        activeSequence.setWaitTimer(activeSequence.getWaitTimer() - deltaTime);
        if (activeSequence.getWaitTimer() <= 0) {
          activeSequence.setWaitTimer(0);
          activeSequence.setCurrentIndex(activeSequence.getCurrentIndex() + 1);
        }
        continue;
      }

      // Skip if blocked by another system
      if (activeSequence.isBlocked()) {
        // TODO: Check if the block is resolved
        // For now, we just skip blocked sequences
        continue;
      }

      // Get the current sequence
      Sequence sequence = findSequenceById(currentZone, activeSequence.getSequenceId());
      if (sequence == null) {
        log.warn("Sequence not found: {}", activeSequence.getSequenceId());
        entitiesToRemove.add(entityId);
        continue;
      }

      List<GameEvent> events = sequence.getEvents();
      
      // Check if sequence is complete
      if (activeSequence.getCurrentIndex() >= events.size()) {
        log.debug("Sequence complete: {}", activeSequence.getSequenceId());
        entitiesToRemove.add(entityId);
        continue;
      }

      // Get and execute the current event
      GameEvent event = events.get(activeSequence.getCurrentIndex());
      executeEvent(world, entityId, activeSequence, event);
    }

    // Remove completed sequences
    for (int entityId : entitiesToRemove) {
      world.removeComponent(entityId, ActiveSequenceComponent.class);
      world.destroyEntity(entityId);
    }
  }

  /**
   * Finds a sequence by its ID in the current zone.
   */
  private Sequence findSequenceById(Zone zone, String sequenceId) {
    for (Sequence sequence : zone.getSequences()) {
      if (sequence.getId().equals(sequenceId)) {
        return sequence;
      }
    }
    return null;
  }

  /**
   * Executes a single GameEvent.
   */
  private void executeEvent(World world, int entityId, ActiveSequenceComponent activeSequence, GameEvent event) {
    String type = event.getType();
    
    switch (type) {
      case "WAIT" -> {
        // Set the wait timer
        float duration = ((Number) event.getProperties().getOrDefault("duration", 1.0)).floatValue();
        activeSequence.setWaitTimer(duration);
        log.debug("Waiting for {} seconds", duration);
      }
      case "PLAY_SOUND" -> {
        // Instant action - execute and move to next event
        String soundId = (String) event.getProperties().get("soundId");
        log.debug("Playing sound: {}", soundId);
        // TODO: Call audioService.playSound(soundId)
        activeSequence.setCurrentIndex(activeSequence.getCurrentIndex() + 1);
      }
      case "TELEPORT_ENTITY" -> {
        // Instant action - execute and move to next event
        String targetEntityId = (String) event.getProperties().get("entityId");
        log.debug("Teleporting entity: {}", targetEntityId);
        // TODO: Update the entity's TransformComponent position
        activeSequence.setCurrentIndex(activeSequence.getCurrentIndex() + 1);
      }
      case "MOVE_ENTITY" -> {
        // Blocking action - issue command and wait for completion
        String targetEntityId = (String) event.getProperties().get("entityId");
        log.debug("Moving entity: {}", targetEntityId);
        // TODO: Add MoveToTarget component to the target entity
        activeSequence.setBlocked(true);
        // The system will need to check later if the MoveToTarget component has been removed
      }
      case "FADE_SCREEN" -> {
        // Blocking action - issue command and wait for completion
        String fadeType = (String) event.getProperties().get("fadeType");
        log.debug("Fading screen: {}", fadeType);
        // TODO: Call renderService.startFade(fadeType)
        activeSequence.setBlocked(true);
      }
      default -> {
        log.warn("Unknown event type: {}", type);
        // Move to next event to avoid getting stuck
        activeSequence.setCurrentIndex(activeSequence.getCurrentIndex() + 1);
      }
    }
  }
}
