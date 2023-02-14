package team.waitingcatch.app.event.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.dto.couponcreator.CreateAdminCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.CreateCouponCreatorControllerRequest;
import team.waitingcatch.app.event.dto.couponcreator.CreateSellerCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateAdminCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateCouponCreatorControllerRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateSellerCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.event.CreateEventControllerRequest;
import team.waitingcatch.app.event.dto.event.CreateEventServiceRequest;
import team.waitingcatch.app.event.dto.event.DeleteEventServiceRequest;
import team.waitingcatch.app.event.dto.event.GetEventsResponse;
import team.waitingcatch.app.event.dto.event.UpdateEventControllerRequest;
import team.waitingcatch.app.event.dto.event.UpdateEventServiceRequest;
import team.waitingcatch.app.event.dto.event.UpdateSellerEventServiceRequest;
import team.waitingcatch.app.event.service.couponcreator.CouponCreatorService;
import team.waitingcatch.app.event.service.event.EventService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@RequiredArgsConstructor
@RestController
public class CouponController {

	private final EventService eventService;
	private final CouponCreatorService couponCreatorService;


	/*  어드민  */

	//새로운 어드민 이벤트를 생성한다.
	@PostMapping("/admin/events")
	public ResponseEntity<String> createAdminEvent(
		@RequestBody CreateEventControllerRequest createEventControllerRequest) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(eventService.createAdminEvent(createEventControllerRequest));
	}

	@PutMapping("/admin/events/{eventId}")
	public ResponseEntity<String> updateAdminEvent(
		@RequestBody UpdateEventControllerRequest updateEventControllerRequest, @PathVariable Long eventId) {
		UpdateEventServiceRequest updateEventServiceRequest = new UpdateEventServiceRequest(
			updateEventControllerRequest, eventId);
		return ResponseEntity.status(HttpStatus.OK)
			.body(eventService.updateAdminEvent(updateEventServiceRequest));
	}

	@DeleteMapping("/admin/events/{eventId}")
	public ResponseEntity<String> deleteAdminEvent(@PathVariable Long eventId) {
		return ResponseEntity.status(HttpStatus.OK)
			.body(eventService.deleteAdminEvent(eventId));
	}

	/*  레스토랑  */

	//새로운 레스토랑 이벤트를 생성한다.
	@PostMapping("/seller/restaurants/{restaurant_id}/events")
	public ResponseEntity<String> createSellerEvent(
		@RequestBody CreateEventControllerRequest createEventControllerRequest, @PathVariable Long restaurant_id) {
		CreateEventServiceRequest createEventServiceRequest = new CreateEventServiceRequest(
			createEventControllerRequest, restaurant_id);
		return ResponseEntity.status(HttpStatus.OK)
			.body(eventService.createSellerEvent(createEventServiceRequest));
	}

	@PutMapping("/seller/events/{eventId}")
	public ResponseEntity<String> updateSellerEvent(
		@RequestBody UpdateEventControllerRequest updateEventControllerRequest, @PathVariable Long eventId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		UpdateSellerEventServiceRequest updateSellerEventServiceRequest = new UpdateSellerEventServiceRequest(
			updateEventControllerRequest, eventId, userDetails.getUsername());
		return ResponseEntity.status(HttpStatus.OK)
			.body(eventService.updateSellerEvent(updateSellerEventServiceRequest));
	}

	@DeleteMapping("/seller/events/{eventId}")
	public ResponseEntity<String> deleteSellerEvent(@PathVariable Long eventId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		DeleteEventServiceRequest deleteEventServiceRequest = new DeleteEventServiceRequest(eventId,
			userDetails.getUsername());
		return ResponseEntity.status(HttpStatus.OK)
			.body(eventService.deleteSellerEvent(deleteEventServiceRequest));
	}

	/*  유저  */

	@GetMapping("/events")
	public List<GetEventsResponse> getEvents() {
		return eventService.getGlobalEvents();
	}

	@GetMapping("/restaurants/{restaurantId}/events")
	public List<GetEventsResponse> getRestaurantEvents(@PathVariable Long restaurantId) {
		return eventService.getRestaurantEvents(restaurantId);
	}


	/*  쿠폰 생성자  */

	//광역 이벤트 쿠폰 생성자 생성
	@PostMapping("/admin/events/{eventId}/creator")
	public ResponseEntity<String> createAdminCouponCreator(
		CreateCouponCreatorControllerRequest createCouponCreatorControllerRequest, @PathVariable Long eventId) {
		CreateAdminCouponCreatorServiceRequest createAdminCouponCreatorServiceRequest = new CreateAdminCouponCreatorServiceRequest(
			createCouponCreatorControllerRequest, eventId);

		return ResponseEntity.status(HttpStatus.OK)
			.body(couponCreatorService.createAdminCouponCreator(createAdminCouponCreatorServiceRequest));

	}

	//레스토랑 이벤트 쿠폰 생성자 생성
	@PostMapping("/seller/events/{eventId}/creator")
	public ResponseEntity<String> createSellerCouponCreator(
		CreateCouponCreatorControllerRequest createCouponCreatorControllerRequest, @PathVariable Long eventId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		CreateSellerCouponCreatorServiceRequest createSellerCouponCreatorServiceRequest = new CreateSellerCouponCreatorServiceRequest(
			createCouponCreatorControllerRequest, eventId, userDetails.getUsername());

		return ResponseEntity.status(HttpStatus.OK)
			.body(couponCreatorService.createSellerCouponCreator(createSellerCouponCreatorServiceRequest));
	}

	//광역 이벤트 쿠폰 생성자 수정
	@PutMapping("/admin/events/{eventId}/creator/{creatorId}")
	public ResponseEntity<String> updateAdminCouponCreator(
		UpdateCouponCreatorControllerRequest updateCouponCreatorControllerRequest, @PathVariable
		Long eventId, @PathVariable Long creatorId) {
		UpdateAdminCouponCreatorServiceRequest updateAdminCouponCreatorServiceRequest = new UpdateAdminCouponCreatorServiceRequest(
			updateCouponCreatorControllerRequest, eventId, creatorId);
		return ResponseEntity.status(HttpStatus.OK)
			.body(couponCreatorService.updateAdminCouponCreator(updateAdminCouponCreatorServiceRequest));
	}

	//레스토랑 이벤트 쿠폰 생성자 수정
	@PutMapping("/seller/events/{eventId}/creator/{creatorId}")
	public ResponseEntity<String> updateSellerCouponCreator(
		UpdateCouponCreatorControllerRequest updateCouponCreatorControllerRequest,
		@PathVariable Long eventId, @PathVariable Long creatorId,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		UpdateSellerCouponCreatorServiceRequest updateSellerCouponCreatorServiceRequest = new UpdateSellerCouponCreatorServiceRequest(
			updateCouponCreatorControllerRequest, eventId, creatorId, userDetails.getUsername());
		return ResponseEntity.status(HttpStatus.OK)
			.body(couponCreatorService.updateSellerCouponCreator(updateSellerCouponCreatorServiceRequest));
	}
	//@GetMapping("/restaurants/{restaurantId}/events/{eventId}")

}
