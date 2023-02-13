package team.waitingcatch.app.event.service.event;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.dto.event.CreateEventControllerRequest;
import team.waitingcatch.app.event.dto.event.CreateEventServiceRequest;
import team.waitingcatch.app.event.dto.event.GetEventServiceResponse;
import team.waitingcatch.app.event.dto.event.GetGlobalEventsServiceResponse;
import team.waitingcatch.app.event.dto.event.GetRestaurantEventControllerRequest;
import team.waitingcatch.app.event.dto.event.GetRestaurantEventsServiceResponse;
import team.waitingcatch.app.event.dto.event.UpdateEventServiceRequest;
import team.waitingcatch.app.event.entity.Event;
import team.waitingcatch.app.event.repository.EventServiceRepository;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService, InternalEventService {

	private final EventServiceRepository eventServiceRepository;
	private final RestaurantRepository restaurantRepository;

	@Override
	public String createAdminEvent(CreateEventControllerRequest createEventControllerRequest) {
		Event event = new Event(createEventControllerRequest);
		eventServiceRepository.save(event);
		return "어드민 이벤트 생성 완료";
	}

	@Override
	public String createSellerEvent(CreateEventServiceRequest createEventServiceRequest) {

		Restaurant restaurants = restaurantRepository.findById(createEventServiceRequest.getRestaurantId()).orElseThrow(
			() -> new IllegalArgumentException("잘못된 레스토랑 Id 입니다.")
		);

		Event event = new Event(createEventServiceRequest, restaurants);
		eventServiceRepository.save(event);
		return "레스토랑 이벤트 생성 완료";
	}

	//어드민은 모든 사람의 이벤트를 수정할수있다.
	@Override
	public String updateAdminEvent(UpdateEventServiceRequest updateEventServiceRequest) {
		Event events = _getEventFindById(updateEventServiceRequest.getEventId());
		events.updateEvent(updateEventServiceRequest);
		return "이벤트 수정 완료";
	}


	

	@Override
	public String deleteAdminEvent(Long eventId) {
		Event events = _getEventFindById(eventId);
		eventServiceRepository.deleteById(eventId);
		return "이벤트가 삭제되었습니다.";
	}

	@Override
	public GetGlobalEventsServiceResponse getGlobalEvents() {
		return null;
	}

	@Override
	public GetRestaurantEventsServiceResponse getRestaurantEvents(Long id) {
		return null;
	}

	@Override
	public GetEventServiceResponse getEvent(GetRestaurantEventControllerRequest getRestaurantEventControllerRequest) {
		return null;
	}

	@Override
	public Optional<Event> _getEventFindByName(String name) {
		Optional<Event> events = eventServiceRepository.findByName(name);
		if (events.isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 이벤트입니다.");
		}
		return events;
	}

	@Override
	public Event _getEventFindById(Long id) {
		Event events = eventServiceRepository.findById(id).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 이벤트 입니다.")
		);
		return events;
	}
}
