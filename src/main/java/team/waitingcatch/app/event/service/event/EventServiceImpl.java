package team.waitingcatch.app.event.service.event;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.dto.event.createEventControllerRequest;
import team.waitingcatch.app.event.dto.event.deleteEventControllerRequest;
import team.waitingcatch.app.event.dto.event.getEventServiceResponse;
import team.waitingcatch.app.event.dto.event.getGlobalEventsServiceResponse;
import team.waitingcatch.app.event.dto.event.getRestaurantEventControllerRequest;
import team.waitingcatch.app.event.dto.event.getRestaurantEventsServiceResponse;
import team.waitingcatch.app.event.dto.event.putEventControllerRequest;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService, InternalEventService {
	@Override
	public void createEvent(createEventControllerRequest createEventControllerRequest) {

	}

	@Override
	public void putEvent(putEventControllerRequest putEventControllerRequest) {

	}

	@Override
	public void deleteEvent(deleteEventControllerRequest deleteEventControllerRequest) {

	}

	@Override
	public getGlobalEventsServiceResponse getGlobalEvents() {
		return null;
	}

	@Override
	public getRestaurantEventsServiceResponse getRestaurantEvents(Long id) {
		return null;
	}

	@Override
	public getEventServiceResponse getEvent(getRestaurantEventControllerRequest getRestaurantEventControllerRequest) {
		return null;
	}
}
