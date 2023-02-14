package team.waitingcatch.app.event.service.event;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.dto.event.CreateEventControllerRequest;
import team.waitingcatch.app.event.dto.event.CreateEventServiceRequest;
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
	public String createAdminEvent(CreateEventControllerRequest createEventControllerRequest) {
		Event event = new Event(createEventControllerRequest);
		eventServiceRepository.save(event);
		return "어드민 이벤트 생성 완료";
	}

	//레스토랑 이벤트를 생성한다.
	@Override
	public String createSellerEvent(CreateEventServiceRequest createEventServiceRequest) {

		Restaurant restaurants = restaurantRepository.findById(createEventServiceRequest.getRestaurantId()).orElseThrow(
			() -> new IllegalArgumentException("잘못된 레스토랑 Id 입니다.")
		);

		Event event = new Event(createEventServiceRequest, restaurants);
		eventServiceRepository.save(event);
		return "레스토랑 이벤트 생성 완료";
	}

	//광역 이벤트를 수정한다.
	@Override
	public String updateAdminEvent(UpdateEventServiceRequest updateEventServiceRequest) {
		Event events = _getEventFindById(updateEventServiceRequest.getEventId());
		events.updateAdminEvent(updateEventServiceRequest);
		return "관리자 이벤트 수정 완료";
	}

	//레스토랑 이벤트를 수정한다.
	@Override
	public String updateSellerEvent(UpdateSellerEventServiceRequest updateSellerEventServiceRequest) {
		Restaurant restaurant = _getRestaurantFindByUsername(updateSellerEventServiceRequest.getUsername());
		Event ev = _getEventFindById(updateSellerEventServiceRequest.getEventId());

		Event events = eventServiceRepository.findByIdAndRestaurant(updateSellerEventServiceRequest.getEventId(),
			restaurant).orElseThrow(
			() -> new IllegalArgumentException("매장에 해당 이벤트가 존재하지 않습니다.")
		);

		events.updateSellerEvent(updateSellerEventServiceRequest);
		return "레스토랑 이벤트 수정 완료";
	}

	//광역 이벤트를 삭제한다.
	@Override
	public String deleteAdminEvent(Long eventId) {
		Event events = _getEventFindById(eventId);
		events.isDeleted();
		return "광역 이벤트가 삭제완료";
	}

	//레스토랑 이벤트를 삭제한다.
	@Override
	public String deleteSellerEvent(DeleteEventServiceRequest deleteEventServiceRequest) {
		Restaurant restaurant = _getRestaurantFindByUsername(deleteEventServiceRequest.getUsername());
		Event ev = _getEventFindById(deleteEventServiceRequest.getEventId());

		Event events = eventServiceRepository.findByIdAndRestaurant(deleteEventServiceRequest.getEventId(),
			restaurant).orElseThrow(
			() -> new IllegalArgumentException("매장에 해당 이벤트가 존재하지 않습니다.")
		);
		events.isDeleted();
		return "레스토랑 이벤트가 삭제완료";
	}

	//광역 이벤트를 조회한다.
	@Override
	public List<GetEventsResponse> getGlobalEvents() {
		//이벤트중 restaurant이 null인것만 조회
		List<Event> events = eventServiceRepository.findByRestaurantIsNull();
		List<GetEventsResponse> GetEventsResponse = new ArrayList<>();
		for (Event event : events) {
			GetEventsResponse.add(new GetEventsResponse(event));
		}
		return GetEventsResponse;

	}

	//레스토랑 이벤트를 조회한다.
	@Override
	public List<GetEventsResponse> getRestaurantEvents(Long restaurantId) {
		//레스토랑 아이디로 레스토랑 객체를 찾아야함
		Restaurant restaurant = _getRestaurantFindById(restaurantId);
		//찾은 객체로 이벤트 검색
		List<Event> events = eventServiceRepository.findByRestaurant(restaurant);
		List<GetEventsResponse> GetEventsResponse = new ArrayList<>();
		for (Event event : events) {
			GetEventsResponse.add(new GetEventsResponse(event));
		}
		return GetEventsResponse;
	}

	@Override
	public Event _getEventFindById(Long id) {
		Event events = eventServiceRepository.findById(id).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 이벤트 입니다.")
		);
		return events;
	}

	@Override
	public Restaurant _getRestaurantFindByUsername(String name) {
		Restaurant restaurant = restaurantRepository.findByUsername(name)
			.orElseThrow(
				() -> new IllegalArgumentException("레스토랑을 찾을수 없습니다.")
			);
		return restaurant;
	}

	@Override
	public Restaurant _getRestaurantFindById(Long id) {
		Restaurant restaurant = restaurantRepository.findById(id)
			.orElseThrow(
				() -> new IllegalArgumentException("레스토랑을 찾을수 없습니다.")
			);
		return restaurant;
	}
}
