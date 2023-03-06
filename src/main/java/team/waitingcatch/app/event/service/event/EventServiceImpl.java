package team.waitingcatch.app.event.service.event;

import static team.waitingcatch.app.exception.ErrorCode.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.dto.couponcreator.GetCouponCreatorResponse;
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
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;

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
		Restaurant restaurant = internalRestaurantService._getRestaurantById(
			createEventServiceRequest.getRestaurantId());
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

	// 광역 이벤트를 조회한다.
	@Override
	@Transactional(readOnly = true)
	public List<GetEventsResponse> getGlobalEvents() {
		// 이벤트 중 restaurant이 null인 것만 조회
		List<Event> events = eventRepository.findByRestaurantIsNullAndIsDeletedFalse();
		return _getEventsResponse(events);

	}

	// 레스토랑 이벤트를 조회한다.
	@Override
	@Transactional(readOnly = true)
	public List<GetEventsResponse> getRestaurantEvents(Long restaurantId) {
		Restaurant restaurant = internalRestaurantService._getRestaurantById(restaurantId);
		List<Event> events = eventRepository.findByRestaurantAndIsDeletedFalse(restaurant);
		return _getEventsResponse(events);


		// //레스토랑 아이디로 레스토랑 객체를 찾아야함
		// Restaurant restaurant = restaurantService._getRestaurantByUserId(id);
		//
		// Page<Event> events = eventRepository.findByRestaurantAndIsDeletedFalse(restaurant, pageable);
		// return _getEventsResponse(events, restaurant, pageable);
		//

	}

	@Override
	@Transactional(readOnly = true)
	public Event _getEventById(Long id) {
		Event event = eventRepository.findByIdAndIsDeletedFalse(id)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_EVENT.getMessage()));
		return event;
	}

	// 이벤트 목록 + 쿠폰생성자를 DTO형태로 리턴
	@Override
	public Page<GetEventsResponse> _getEventsResponse(Page<Event> events, Restaurant restaurant, Pageable pageable) {

		List<GetEventsResponse> getEventsResponse = new ArrayList<>();
		for (Event event : events) {
			//List<CouponCreator> couponCreators = couponCreatorRepository.findByEventAndIsDeletedFalse(event);
			List<CouponCreator> couponCreators = couponCreatorRepository.findByEventWithEvent(event);
			List<GetCouponCreatorResponse> getCouponCreatorResponses = new ArrayList<>();
			for (CouponCreator couponCreator : couponCreators) {
				GetCouponCreatorResponse getCouponCreatorResponse = new GetCouponCreatorResponse(couponCreator);
				getCouponCreatorResponses.add(getCouponCreatorResponse);
			}
			getEventsResponse.add(new GetEventsResponse(event, getCouponCreatorResponses));
		}

		return new PageImpl<>(getEventsResponse, pageable, events.getTotalElements());
	}

	public List<GetEventsResponse> _getGlobalEventsResponse(List<Event> events) {

		List<GetEventsResponse> getEventsResponse = new ArrayList<>();
		for (Event event : events) {
			List<CouponCreator> couponCreators = couponCreatorRepository.findByEventWithEvent(event);
			List<GetCouponCreatorResponse> getCouponCreatorResponses = new ArrayList<>();
			for (CouponCreator couponCreator : couponCreators) {
				GetCouponCreatorResponse getCouponCreatorResponse = new GetCouponCreatorResponse(couponCreator);
				getCouponCreatorResponses.add(getCouponCreatorResponse);
			}
			getEventsResponse.add(new GetEventsResponse(event, getCouponCreatorResponses));
		}

		return getEventsResponse;
	}

	@Override
	public void _bulkSoftDeleteByRestaurantId(Long restaurantId) {
		eventRepository.softDeleteByRestaurantId(restaurantId);
	}
}