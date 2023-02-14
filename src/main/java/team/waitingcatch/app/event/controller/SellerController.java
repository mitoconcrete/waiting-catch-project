package team.waitingcatch.app.event.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.dto.event.CreateEventControllerRequest;
import team.waitingcatch.app.event.dto.event.CreateEventServiceRequest;
import team.waitingcatch.app.event.dto.event.DeleteEventServiceRequest;
import team.waitingcatch.app.event.dto.event.UpdateEventControllerRequest;
import team.waitingcatch.app.event.dto.event.UpdateSellerEventServiceRequest;
import team.waitingcatch.app.event.service.event.EventService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@RequiredArgsConstructor
@RestController
public class SellerController {

	private final EventService eventService;

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

}
