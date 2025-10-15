package application;

import engine.ecs.ISystem;
import engine.ecs.IWorld;
import engine.services.world.components.ControllableComponent;
import lombok.RequiredArgsConstructor;

/**
 * This system reads from the InputMappingService and updates the state of all
 * ControllableComponent instances based on the player's 2D input.
 */
@RequiredArgsConstructor
public class PlayerInputSystem implements ISystem {

  private final InputMappingService mappingService;

  @Override
  public void update(IWorld world, float deltaTime) {
    for (int entityId : world.getEntitiesWith(ControllableComponent.class)) {
      ControllableComponent control = world.getComponent(entityId, ControllableComponent.class);
      int playerId = control.playerId;

      // Update the component's state based on the abstract 2D actions
      control.wantsToMoveUp = mappingService.isActionActive(playerId, GameAction.MOVE_UP);
      control.wantsToMoveDown = mappingService.isActionActive(playerId, GameAction.MOVE_DOWN);
      control.wantsToMoveLeft = mappingService.isActionActive(playerId, GameAction.MOVE_LEFT);
      control.wantsToMoveRight = mappingService.isActionActive(playerId, GameAction.MOVE_RIGHT);
      control.wantsToAttack = mappingService.isActionActive(playerId, GameAction.ATTACK);
    }
  }
}
