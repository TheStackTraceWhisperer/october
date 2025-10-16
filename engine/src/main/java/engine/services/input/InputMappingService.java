package engine.services.input;

import engine.game.GameAction;

public interface InputMappingService {
  boolean isActionActive(int playerId, GameAction action);
}
