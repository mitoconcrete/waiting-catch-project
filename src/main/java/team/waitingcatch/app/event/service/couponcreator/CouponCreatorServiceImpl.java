package team.waitingcatch.app.event.service.couponcreator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.dto.couponcreator.CreateAdminCouponCreatorRequest;
import team.waitingcatch.app.event.dto.couponcreator.CreateAdminCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.CreateSellerCouponCreatorRequest;
import team.waitingcatch.app.event.dto.couponcreator.CreateSellerCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateAdminCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateSellerCouponCreatorServiceRequest;
import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.entity.Event;
import team.waitingcatch.app.event.repository.CouponCreatorRepository;
import team.waitingcatch.app.event.repository.EventRepository;
import team.waitingcatch.app.event.service.event.InternalEventService;
import team.waitingcatch.app.restaurant.entity.Restaurant;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponCreatorServiceImpl implements CouponCreatorService, InternalCouponCreatorService {

	private final EventRepository eventRepository;
	private final CouponCreatorRepository couponCreatorRepository;
	private final InternalEventService internalEventService;

	//광역 이벤트 쿠폰생성자를 생성한다.
	@Override
	public void createAdminCouponCreator(
		CreateAdminCouponCreatorServiceRequest createAdminCouponCreatorServiceRequest) {
		Event events = internalEventService._getEventById(createAdminCouponCreatorServiceRequest.getEventId());
		CreateAdminCouponCreatorRequest createAdminCouponCreatorRequest = new CreateAdminCouponCreatorRequest(
			createAdminCouponCreatorServiceRequest, events);
		CouponCreator couponCreator = new CouponCreator(createAdminCouponCreatorRequest);
		couponCreatorRepository.save(couponCreator);
	}

	//레스토랑 이벤트 쿠폰생성자를 생성한다
	@Override
	public void createSellerCouponCreator(
		CreateSellerCouponCreatorServiceRequest createSellerCouponCreatorServiceRequest) {
		Restaurant restaurant = internalEventService._getRestaurantById(
			createSellerCouponCreatorServiceRequest.getUserId());

		Event event = eventRepository.findByIdAndRestaurantAndIsDeletedFalse(
				createSellerCouponCreatorServiceRequest.getEventId(), restaurant)
			.orElseThrow(() -> new IllegalArgumentException("매장에 해당 이벤트가 존재하지 않습니다."));

		CreateSellerCouponCreatorRequest createAdminCouponCreatorRequest = new CreateSellerCouponCreatorRequest(
			createSellerCouponCreatorServiceRequest, event);
		CouponCreator couponCreator = new CouponCreator(createAdminCouponCreatorRequest);
		couponCreatorRepository.save(couponCreator);
	}

	//광역 이벤트 쿠폰생성자를 수정한다.
	@Override
	public void updateAdminCouponCreator(
		UpdateAdminCouponCreatorServiceRequest updateAdminCouponCreatorServiceRequest) {
		internalEventService._getEventById(updateAdminCouponCreatorServiceRequest.getEventId());
		CouponCreator couponCreators = _getCouponCreatorById(updateAdminCouponCreatorServiceRequest.getCreatorId());

		couponCreators.updateAdminCouponCreator(updateAdminCouponCreatorServiceRequest);
	}

	//레스토랑 이벤트 쿠폰생성자를 수정한다.
	@Override
	public void updateSellerCouponCreator(
		UpdateSellerCouponCreatorServiceRequest updateSellerCouponCreatorServiceRequest) {
		Restaurant restaurant = internalEventService._getRestaurantById(
			updateSellerCouponCreatorServiceRequest.getUserId());

		eventRepository.findByIdAndRestaurantAndIsDeletedFalse(
				updateSellerCouponCreatorServiceRequest.getEventId(), restaurant)
			.orElseThrow(() -> new IllegalArgumentException("매장에 해당 이벤트가 존재하지 않습니다."));

		CouponCreator couponCreators = _getCouponCreatorById(
			updateSellerCouponCreatorServiceRequest.getCreatorId());

		couponCreators.updateSellerCouponCreator(updateSellerCouponCreatorServiceRequest);
	}

	@Override
	@Transactional(readOnly = true)
	public CouponCreator _getCouponCreatorById(Long id) {
		CouponCreator couponCreator = couponCreatorRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("쿠폰 생성자를 찾을수 없습니다."));
		return couponCreator;
	}

}
