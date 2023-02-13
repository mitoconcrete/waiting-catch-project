package team.waitingcatch.app.event.service.event;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.dto.event.CreateEventControllerRequest;
import team.waitingcatch.app.event.dto.event.DeleteEventControllerRequest;
import team.waitingcatch.app.event.dto.event.GetEventServiceResponse;
import team.waitingcatch.app.event.dto.event.GetGlobalEventsServiceResponse;
import team.waitingcatch.app.event.dto.event.GetRestaurantEventControllerRequest;
import team.waitingcatch.app.event.dto.event.GetRestaurantEventsServiceResponse;
import team.waitingcatch.app.event.dto.event.PutEventControllerRequest;
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

		Optional<Event> events = eventServiceRepository.findByName(createEventControllerRequest.getName());
		if (events.isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 이벤트입니다.");
		}

		Event event = new Event(createEventControllerRequest);
		return "어드민 이벤트 생성 완료";
	}

	@Override
	public String createSellerEvent(CreateEventControllerRequest createEventControllerRequest, Long restaurant_id) {

		Optional<Event> events = eventServiceRepository.findByName(createEventControllerRequest.getName());
		if (events.isPresent()) {
			throw new IllegalArgumentException("이미 존재하는 이벤트입니다.");
		}
		Restaurant restaurants = restaurantRepository.findById(restaurant_id).orElseThrow(
			() -> new IllegalArgumentException("잘못된 레스토랑 Id 입니다.")
		);

		Event event = new Event(createEventControllerRequest, restaurants);
		return "레스토랑 이벤트 생성 완료";
	}

	@Override
	public String putEvent(PutEventControllerRequest putEventControllerRequest) {
		return "";
	}

	@Override
	public String deleteEvent(DeleteEventControllerRequest deleteEventControllerRequest) {
		return "";
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
}
