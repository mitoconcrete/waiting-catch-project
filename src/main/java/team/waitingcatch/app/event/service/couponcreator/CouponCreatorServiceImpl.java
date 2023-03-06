package team.waitingcatch.app.event.service.couponcreator;

import static team.waitingcatch.app.exception.ErrorCode.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.dto.couponcreator.CreateAdminCouponCreatorRequest;
import team.waitingcatch.app.event.dto.couponcreator.CreateAdminCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.CreateSellerCouponCreatorRequest;
import team.waitingcatch.app.event.dto.couponcreator.CreateSellerCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateAdminCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateSellerCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.event.GetCouponCreatorResponse;
import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.entity.Event;
import team.waitingcatch.app.event.repository.CouponCreatorRepository;
import team.waitingcatch.app.event.repository.EventRepository;
import team.waitingcatch.app.event.service.event.InternalEventService;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.service.restaurant.InternalRestaurantService;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponCreatorServiceImpl implements CouponCreatorService, InternalCouponCreatorService {
	private final EventRepository eventRepository;
	private final CouponCreatorRepository couponCreatorRepository;

	private final InternalRestaurantService internalRestaurantService;
	private final InternalEventService internalEventService;

	// 광역 이벤트 쿠폰 생성자를 생성한다.
	@Override
	public void createAdminCouponCreator(
		CreateAdminCouponCreatorServiceRequest createAdminCouponCreatorServiceRequest) {
		Event events = internalEventService._getEventById(createAdminCouponCreatorServiceRequest.getEventId());
		CreateAdminCouponCreatorRequest createAdminCouponCreatorRequest = new CreateAdminCouponCreatorRequest(
			createAdminCouponCreatorServiceRequest, events);
		CouponCreator couponCreator = new CouponCreator(createAdminCouponCreatorRequest);
		couponCreatorRepository.save(couponCreator);
	}

	@Override
	@Transactional(readOnly = true)
	public List<GetCouponCreatorResponse> getAdminCouponCreator(Long eventId) {
		return couponCreatorRepository.findAllByEventId(eventId).stream()
			.map(GetCouponCreatorResponse::new)
			.collect(Collectors.toList());
	}

	// 레스토랑 이벤트 쿠폰 생성자를 생성한다
	@Override
	public void createSellerCouponCreator(
		CreateSellerCouponCreatorServiceRequest serviceRequest) {
		Restaurant restaurant = internalRestaurantService._getRestaurantByUserId(serviceRequest.getUserId());

		Event event = eventRepository.findByIdAndRestaurantAndIsDeletedFalse(serviceRequest.getEventId(), restaurant)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_EVENT.getMessage()));

		CreateSellerCouponCreatorRequest createAdminCouponCreatorRequest = new CreateSellerCouponCreatorRequest(
			serviceRequest, event);
		CouponCreator couponCreator = new CouponCreator(createAdminCouponCreatorRequest);
		couponCreatorRepository.save(couponCreator);
	}

	//광역 이벤트 쿠폰생성자를 수정한다.
	@Override
	public void updateAdminCouponCreator(UpdateAdminCouponCreatorServiceRequest serviceRequest) {
		internalEventService._getEventById(serviceRequest.getEventId());
		CouponCreator couponCreators = _getCouponCreatorById(serviceRequest.getCreatorId());

		couponCreators.updateAdminCouponCreator(serviceRequest);
	}

	//레스토랑 이벤트 쿠폰생성자를 수정한다.
	@Override
	public void updateSellerCouponCreator(UpdateSellerCouponCreatorServiceRequest serviceRequest) {
		Restaurant restaurant = internalRestaurantService._getRestaurantByUserId(serviceRequest.getUserId());
		System.out.println(serviceRequest.getEventId() + "  " + restaurant + " 검증");
		eventRepository.findByIdAndRestaurantAndIsDeletedFalse(
				serviceRequest.getEventId(), restaurant)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_EVENT.getMessage()));

		CouponCreator couponCreators = _getCouponCreatorById(
			serviceRequest.getCreatorId());

		couponCreators.updateSellerCouponCreator(serviceRequest);
	}

	@Override
	@Transactional(readOnly = true)
	public CouponCreator _getCouponCreatorById(Long id) {
		return couponCreatorRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_COUPON_CRETOR.getMessage()));
	}
}