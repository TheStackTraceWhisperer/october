package engine.services.world.components;

import engine.services.world.IComponent;
import lombok.Getter;
import lombok.Setter;

/**
 * A stateful "bookmark" component added to an entity to signify that a sequence is currently running.
 * The SequenceSystem acts upon entities that have this component.
 */
@Getter
@Setter
public class ActiveSequenceComponent implements IComponent {
  /**
   * The ID of the Sequence (from the current Zone) to execute.
   */
  private String sequenceId;

  /**
   * The index of the current GameEvent being processed.
   */
  private int currentIndex;

  /**
   * A countdown timer used for the "WAIT" command.
   */
  private float waitTimer;

  /**
   * A flag set to true when the sequence is waiting for another system 
   * (e.g., MovementSystem) to complete a long-running task.
   */
  private boolean isBlocked;

  public ActiveSequenceComponent(String sequenceId) {
    this.sequenceId = sequenceId;
    this.currentIndex = 0;
    this.waitTimer = 0.0f;
    this.isBlocked = false;
  }
}
