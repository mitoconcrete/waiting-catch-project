package team.waitingcatch.app.event.service.event;

import java.util.Optional;

import team.waitingcatch.app.event.entity.Event;

public interface InternalEventService {

	public Optional<Event> _getEventFindByName(String name);
}
