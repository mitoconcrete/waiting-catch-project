package team.waitingcatch.app.event.service.event;

import static team.waitingcatch.app.exception.ErrorCode.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.entity.Event;
import team.waitingcatch.app.event.repository.CouponCreatorRepository;
import team.waitingcatch.app.event.repository.EventRepository;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.service.restaurant.InternalRestaurantService;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService, InternalEventService {
	private final EventRepository eventRepository;
	private final CouponCreatorRepository couponCreatorRepository;

	private final InternalRestaurantService internalRestaurantService;

	// 광역 이벤트를 생성한다.
	@Override
	public void createAdminEvent(CreateEventControllerRequest createEventControllerRequest) {
		Event event = new Event(createEventControllerRequest);
		eventRepository.save(event);
	}

	// 레스토랑 이벤트를 생성한다.
	@Override
	public void createSellerEvent(CreateEventServiceRequest createEventServiceRequest) {
		Restaurant restaurant = internalRestaurantService._getRestaurantByUserId(
			createEventServiceRequest.getSellerId());
		CreateEventRequest createEventRequest = new CreateEventRequest(createEventServiceRequest, restaurant);
		Event event = new Event(createEventRequest);
		eventRepository.save(event);
	}

	// 광역 이벤트를 수정한다.
	@Override
	public void updateAdminEvent(UpdateEventServiceRequest updateEventServiceRequest) {
		Event event = _getEventById(updateEventServiceRequest.getEventId());
		event.updateAdminEvent(updateEventServiceRequest);
	}

	// 레스토랑 이벤트를 수정한다.
	@Override
	public void updateSellerEvent(UpdateSellerEventServiceRequest updateSellerEventServiceRequest) {
		Restaurant restaurant = internalRestaurantService._getRestaurantByUserId(
			updateSellerEventServiceRequest.getUserId());
		Event event = eventRepository.findByIdAndRestaurantAndIsDeletedFalse(
				updateSellerEventServiceRequest.getEventId(), restaurant)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_EVENT.getMessage()));
		event.updateSellerEvent(updateSellerEventServiceRequest);
	}

	// 광역 이벤트를 삭제한다.
	@Override
	public void deleteAdminEvent(DeleteEventControllerRequest deleteEventControllerRequest) {
		Event event = _getEventById(deleteEventControllerRequest.getEventId());
		event.deleteEvent();
	}

	// 레스토랑 이벤트를 삭제한다.
	@Override
	public void deleteSellerEvent(DeleteEventServiceRequest deleteEventServiceRequest) {
		Restaurant restaurant = internalRestaurantService._getRestaurantByUserId(deleteEventServiceRequest.getUserId());
		Event event = eventRepository.findByIdAndRestaurantAndIsDeletedFalse(
				deleteEventServiceRequest.getEventId(), restaurant)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_EVENT.getMessage()));
		event.deleteEvent();
	}

	// 레스토랑 이벤트를 조회한다.
	@Override
	@Transactional(readOnly = true)
	public Page<GetEventsResponse> getRestaurantEvents(Long userId, Pageable pageable) {
		Restaurant restaurant = internalRestaurantService._getRestaurantByUserId(userId);
		Page<Event> events = eventRepository.findByRestaurantAndIsDeletedFalse(restaurant, pageable);
		return _getEventsResponse(events, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<GetEventsResponse> getGlobalEvents(Pageable pageable) {
		Page<Event> events = eventRepository.findByRestaurantIsNullAndIsDeletedFalse(pageable);
		return _getEventsResponse(events, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Event _getEventById(Long id) {
		return eventRepository.findByIdAndIsDeletedFalse(id)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_EVENT.getMessage()));
	}

	// 이벤트 목록 + 쿠폰생성자를 DTO형태로 리턴
	@Override
	public Page<GetEventsResponse> _getEventsResponse(Page<Event> events, Pageable pageable) {
		List<CouponCreator> couponCreators = couponCreatorRepository.findByEvent(events.getContent());
		List<GetEventsResponse> getEventsResponses = events.get()
			.map(event -> new GetEventsResponse(event, couponCreators))
			.collect(
				Collectors.toList());
		return new PageImpl<>(getEventsResponses, pageable, events.getTotalElements());
	}

	@Override
	public void _bulkSoftDeleteByRestaurantId(Long restaurantId) {
		eventRepository.softDeleteByRestaurantId(restaurantId);
	}
}