package team.waitingcatch.app.event.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.dto.GenericResponse;
import team.waitingcatch.app.event.dto.couponcreator.CreateAdminCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.CreateCouponCreatorControllerRequest;
import team.waitingcatch.app.event.dto.couponcreator.CreateSellerCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateAdminCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateCouponCreatorControllerRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateSellerCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.event.CreateEventControllerRequest;
import team.waitingcatch.app.event.dto.event.DeleteEventControllerRequest;
import team.waitingcatch.app.event.dto.event.DeleteEventServiceRequest;
import team.waitingcatch.app.event.dto.event.GetCouponCreatorResponse;
import team.waitingcatch.app.event.dto.event.GetEventsResponse;
import team.waitingcatch.app.event.dto.event.UpdateEventControllerRequest;
import team.waitingcatch.app.event.dto.event.UpdateEventServiceRequest;
import team.waitingcatch.app.event.dto.event.UpdateSellerEventServiceRequest;
import team.waitingcatch.app.event.dto.usercoupon.CreateUserCouponServiceRequest;
import team.waitingcatch.app.event.dto.usercoupon.GetUserCouponResponse;
import team.waitingcatch.app.event.service.couponcreator.CouponCreatorService;
import team.waitingcatch.app.event.service.event.EventService;
import team.waitingcatch.app.event.service.usercoupon.UserCouponService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class CouponController {

	private final EventService eventService;
	private final CouponCreatorService couponCreatorService;
	private final UserCouponService userCouponService;


	/*  어드민  */

	//새로운 광역 이벤트를 생성한다.
	@PostMapping("/admin/events")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createAdminEvent(@Validated @RequestBody CreateEventControllerRequest createEventControllerRequest) {
		eventService.createAdminEvent(createEventControllerRequest);
	}

	// 광역 이벤트 목록을 조회한다.
	@GetMapping("/admin/events")
	public GenericResponse<Page<GetEventsResponse>> getAdminEvents(
		@PageableDefault(size = 10, page = 0) Pageable pageable) {
		return new GenericResponse<>(eventService.getGlobalEvents(pageable));
	}

	//광역 이벤트를 수정한다.
	@PutMapping("/admin/events/{eventId}")
	public void updateAdminEvent(@Validated @RequestBody UpdateEventControllerRequest updateEventControllerRequest,
		@PathVariable Long eventId) {
		UpdateEventServiceRequest updateEventServiceRequest = new UpdateEventServiceRequest(
			updateEventControllerRequest, eventId);
		eventService.updateAdminEvent(updateEventServiceRequest);
	}

	//광역 이벤트를 삭제한다.
	@DeleteMapping("/admin/events/{eventId}")
	public void deleteAdminEvent(@PathVariable Long eventId) {
		DeleteEventControllerRequest deleteEventControllerRequest = new DeleteEventControllerRequest(eventId);
		eventService.deleteAdminEvent(deleteEventControllerRequest);
	}

	/*  레스토랑  */

	//새로운 레스토랑 이벤트를 생성한다.
	// @PostMapping("/seller/restaurants/{restaurant_id}/events")
	// @ResponseStatus(value = HttpStatus.CREATED)
	// public void createSellerEvent(@Validated @RequestBody CreateEventControllerRequest createEventControllerRequest,
	// 	@PathVariable Long restaurant_id) {
	// 	CreateEventServiceRequest createEventServiceRequest = new CreateEventServiceRequest(
	// 		createEventControllerRequest, restaurant_id);
	// 	eventService.createSellerEvent(createEventServiceRequest);
	// }

	//레스토랑 이벤트를 수정한다.
	@PutMapping("/seller/events/{eventId}")
	public void updateSellerEvent(@RequestBody UpdateEventControllerRequest updateEventControllerRequest,
		@PathVariable Long eventId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		UpdateSellerEventServiceRequest updateSellerEventServiceRequest = new UpdateSellerEventServiceRequest(
			updateEventControllerRequest, eventId, userDetails.getId());
		eventService.updateSellerEvent(updateSellerEventServiceRequest);
	}

	//레스토랑 이벤트를 삭제한다.
	@DeleteMapping("/seller/events/{eventId}")
	public void deleteSellerEvent(@PathVariable Long eventId, @AuthenticationPrincipal UserDetailsImpl userDetails) {

		DeleteEventServiceRequest deleteEventServiceRequest = new DeleteEventServiceRequest(eventId,
			userDetails.getId());
		eventService.deleteSellerEvent(deleteEventServiceRequest);
	}

	/*  유저  */
	//광역 이벤트 목록 출력 + 해당 이벤트의 쿠폰생성자 출력
	@GetMapping("/customer/events")
	public GenericResponse<Page<GetEventsResponse>> getEvents(@PageableDefault(size = 10, page = 0) Pageable pageable) {
		return new GenericResponse<>(eventService.getGlobalEvents(pageable));
	}

	//레스토랑 이벤트 목록 출력 + 해당 이벤트의 쿠폰생성자 출력
	@GetMapping("/customer/restaurants/{restaurantId}/events")
	public GenericResponse<Page<GetEventsResponse>> getRestaurantEvents(
		@PageableDefault(size = 10, page = 0) Pageable pageable,
		@PathVariable Long restaurantId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return new GenericResponse<>(eventService.getRestaurantEventsByRestaurantId(restaurantId, pageable));
	}
	/*  쿠폰 생성자  */

	//광역 이벤트 쿠폰 생성자 생성
	@PostMapping("/admin/events/{eventId}/creator")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createAdminCouponCreator(
		@Validated @RequestBody CreateCouponCreatorControllerRequest createCouponCreatorControllerRequest,
		@PathVariable Long eventId) {
		CreateAdminCouponCreatorServiceRequest createAdminCouponCreatorServiceRequest = new CreateAdminCouponCreatorServiceRequest(
			createCouponCreatorControllerRequest, eventId);

		couponCreatorService.createAdminCouponCreator(createAdminCouponCreatorServiceRequest);

	}

	//레스토랑 이벤트 쿠폰 생성자 생성
	@PostMapping("/seller/events/{eventId}/creator")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createSellerCouponCreator(
		@Validated @RequestBody CreateCouponCreatorControllerRequest createCouponCreatorControllerRequest,
		@PathVariable Long eventId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		CreateSellerCouponCreatorServiceRequest createSellerCouponCreatorServiceRequest = new CreateSellerCouponCreatorServiceRequest(
			createCouponCreatorControllerRequest, eventId, userDetails.getId());

		couponCreatorService.createSellerCouponCreator(createSellerCouponCreatorServiceRequest);
	}

	// 광역 이벤트 쿠폰 생성자 조회
	@GetMapping("/admin/events/{eventId}/creator")
	public GenericResponse<List<GetCouponCreatorResponse>> getAdminCouponCreator(@PathVariable Long eventId) {
		return new GenericResponse<>(couponCreatorService.getAdminCouponCreator(eventId));
	}

	//광역 이벤트 쿠폰 생성자 수정
	@PutMapping("/admin/events/{eventId}/creator/{creatorId}")
	public void updateAdminCouponCreator(
		@Validated @RequestBody UpdateCouponCreatorControllerRequest updateCouponCreatorControllerRequest,
		@PathVariable Long eventId, @PathVariable Long creatorId) {
		UpdateAdminCouponCreatorServiceRequest updateAdminCouponCreatorServiceRequest = new UpdateAdminCouponCreatorServiceRequest(
			updateCouponCreatorControllerRequest, eventId, creatorId);
		couponCreatorService.updateAdminCouponCreator(updateAdminCouponCreatorServiceRequest);
	}

	//레스토랑 이벤트 쿠폰 생성자 수정
	@PutMapping("/seller/events/{eventId}/creator/{creatorId}")
	public void updateSellerCouponCreator(
		@RequestBody UpdateCouponCreatorControllerRequest controllerRequest,
		@PathVariable Long eventId, @PathVariable Long creatorId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		UpdateSellerCouponCreatorServiceRequest serviceRequest = new UpdateSellerCouponCreatorServiceRequest(
			controllerRequest, eventId, creatorId, userDetails.getId());
		couponCreatorService.updateSellerCouponCreator(serviceRequest);
	}


	/*  유저 쿠폰  */

	//광역 쿠폰 생성
	@PostMapping("/admin/coupons/creators/{creatorId}")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createAdminCoupon(@Validated @PathVariable Long creatorId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		CreateUserCouponServiceRequest createUserCouponserviceRequest = new CreateUserCouponServiceRequest(creatorId,
			userDetails.getUsername());

		userCouponService.createUserCoupon(createUserCouponserviceRequest);
	}

	//셀러 쿠폰 생성
	@PostMapping("/seller/coupons/creators/{creatorId}")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createSellerCoupon(@Validated @PathVariable Long creatorId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		CreateUserCouponServiceRequest createUserCouponserviceRequest = new CreateUserCouponServiceRequest(creatorId,
			userDetails.getUsername());

		userCouponService.createUserCoupon(createUserCouponserviceRequest);
	}

	//유저 쿠폰 조회
	@GetMapping("/customer/coupons")
	public GenericResponse<List<GetUserCouponResponse>> getUserCoupon(
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return new GenericResponse<>(userCouponService.getUserCoupons(userDetails.getUser()));
	}

	//유저 쿠폰 사용
	@PutMapping("/customer/coupons/{couponId}")
	public void useCoupon(@PathVariable Long couponId) {
		userCouponService.useCoupon(couponId);
	}

	//유저 쿠폰 다운로드
	@PostMapping("/customer/coupons/creators/{creatorid}")
	public void downloadCoupon(@PathVariable Long creatorid, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		CreateUserCouponServiceRequest createUserCouponserviceRequest = new CreateUserCouponServiceRequest(creatorid,
			userDetails.getUsername());
		userCouponService.createUserCoupon(createUserCouponserviceRequest);
	}
}