package team.waitingcatch.app.event.service.event;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.dto.event.CreateEventControllerRequest;
import team.waitingcatch.app.event.dto.event.CreateEventRequest;
import team.waitingcatch.app.event.dto.event.CreateEventServiceRequest;
import team.waitingcatch.app.event.dto.event.DeleteEventControllerRequest;
import team.waitingcatch.app.event.dto.event.DeleteEventServiceRequest;
import team.waitingcatch.app.event.dto.event.GetEventsResponse;
import team.waitingcatch.app.event.dto.event.UpdateEventServiceRequest;
import team.waitingcatch.app.event.dto.event.UpdateSellerEventServiceRequest;
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

	//광역 이벤트를 생성한다.
	@Override
	public void createAdminEvent(CreateEventControllerRequest createEventControllerRequest) {
		Event event = new Event(createEventControllerRequest);
		eventServiceRepository.save(event);
	}

	//레스토랑 이벤트를 생성한다.
	@Override
	public void createSellerEvent(CreateEventServiceRequest createEventServiceRequest) {

		Restaurant restaurant = restaurantRepository.findById(createEventServiceRequest.getRestaurantId())
			.orElseThrow(() -> new IllegalArgumentException("잘못된 레스토랑 Id 입니다."));
		CreateEventRequest createEventRequest = new CreateEventRequest(createEventServiceRequest, restaurant);
		Event event = new Event(createEventRequest);
		eventServiceRepository.save(event);
	}

	//광역 이벤트를 수정한다.
	@Override
	public void updateAdminEvent(UpdateEventServiceRequest updateEventServiceRequest) {
		Event event = _getEventById(updateEventServiceRequest.getEventId());
		event.updateAdminEvent(updateEventServiceRequest);
	}

	//레스토랑 이벤트를 수정한다.
	@Override
	public void updateSellerEvent(UpdateSellerEventServiceRequest updateSellerEventServiceRequest) {
		Restaurant restaurant = _getRestaurantByUsername(updateSellerEventServiceRequest.getUsername());
		Event event = eventServiceRepository.findByIdAndIsDeletedFalseAndRestaurantIsDeletedFalse(
				updateSellerEventServiceRequest.getEventId(), restaurant)
			.orElseThrow(() -> new IllegalArgumentException("매장에 해당 이벤트가 존재하지 않습니다."));
		event.updateSellerEvent(updateSellerEventServiceRequest);
	}

	//광역 이벤트를 삭제한다.
	@Override
	public void deleteAdminEvent(DeleteEventControllerRequest deleteEventControllerRequest) {
		Event event = _getEventById(deleteEventControllerRequest.getEventId());
		event.deleteEvent();
	}

	//레스토랑 이벤트를 삭제한다.
	@Override
	public void deleteSellerEvent(DeleteEventServiceRequest deleteEventServiceRequest) {
		Restaurant restaurant = _getRestaurantByUsername(deleteEventServiceRequest.getUsername());
		Event event = eventServiceRepository.findByIdAndIsDeletedFalseAndRestaurantIsDeletedFalse(
				deleteEventServiceRequest.getEventId(), restaurant)
			.orElseThrow(() -> new IllegalArgumentException("매장에 해당 이벤트가 존재하지 않습니다."));
		event.deleteEvent();
	}

	//광역 이벤트를 조회한다.
	@Override
	@Transactional(readOnly = true)
	public List<GetEventsResponse> getGlobalEvents() {
		//이벤트중 restaurant이 null인것만 조회
		List<Event> events = eventServiceRepository.findByRestaurantIsNullAndIsDeletedFalse();
		List<GetEventsResponse> GetEventsResponse = new ArrayList<>();
		for (Event event : events) {
			GetEventsResponse.add(new GetEventsResponse(event));
		}
		return GetEventsResponse;

	}

	//레스토랑 이벤트를 조회한다.
	@Override
	@Transactional(readOnly = true)
	public List<GetEventsResponse> getRestaurantEvents(Long restaurantId) {
		//레스토랑 아이디로 레스토랑 객체를 찾아야함
		Restaurant restaurant = _getRestaurantById(restaurantId);
		//찾은 객체로 이벤트 검색
		List<Event> events = eventServiceRepository.findByRestaurantAndIsDeletedFalse(restaurant);
		List<GetEventsResponse> GetEventsResponse = new ArrayList<>();
		for (Event event : events) {
			GetEventsResponse.add(new GetEventsResponse(event));
		}
		return GetEventsResponse;
	}

	@Override
	@Transactional(readOnly = true)
	public Event _getEventById(Long id) {
		Event event = eventServiceRepository.findByIdAndIsDeletedFalse(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이벤트 입니다."));
		return event;
	}

	@Override
	@Transactional(readOnly = true)
	public Restaurant _getRestaurantByUsername(String name) {
		Restaurant restaurant = restaurantRepository.findByUsernameAndIsDeletedFalse(name)
			.orElseThrow(() -> new IllegalArgumentException("레스토랑을 찾을수 없습니다."));
		return restaurant;
	}

	@Override
	@Transactional(readOnly = true)
	public Restaurant _getRestaurantById(Long id) {
		Restaurant restaurant = restaurantRepository.findByIdAndIsDeletedFalse(id)
			.orElseThrow(() -> new IllegalArgumentException("레스토랑을 찾을수 없습니다."));
		return restaurant;
	}
}
