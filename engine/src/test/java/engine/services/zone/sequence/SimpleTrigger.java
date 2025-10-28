package engine.services.zone.sequence;

import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

/** Simple implementation of Trigger for testing purposes. */
@Getter
@Builder
public class SimpleTrigger implements Trigger {
  private final String id;
  private final String type;
  private final List<GameEvent> events;
  private final Map<String, Object> properties;
}
