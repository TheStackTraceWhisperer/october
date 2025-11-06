package engine.services.world.components;

import engine.services.world.IComponent;
import io.micronaut.core.annotation.Introspected;
import lombok.Getter;
import lombok.Setter;

/**
 * Marker component that indicates an entity should trigger an action when its sequence completes.
 * When attached to an entity with ActiveSequenceComponent, it signals that sequence completion
 * should trigger a specific behavior (e.g., state transition).
 */
@Introspected
@Getter
@Setter
public class IdleTimeoutComponent implements IComponent {
  /**
   * The action to perform when the timeout completes.
   * For example: "PUSH_INTRO_CUTSCENE", "POP_STATE"
   */
  private String action;

  public IdleTimeoutComponent(String action) {
    this.action = action;
  }
}
