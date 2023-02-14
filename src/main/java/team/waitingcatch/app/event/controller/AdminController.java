package team.waitingcatch.app.event.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.dto.event.CreateEventControllerRequest;
import team.waitingcatch.app.event.dto.event.UpdateEventControllerRequest;
import team.waitingcatch.app.event.dto.event.UpdateEventServiceRequest;
import team.waitingcatch.app.event.service.event.EventService;

@RequiredArgsConstructor
@RestController
public class AdminController {

	private final EventService eventService;

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

}
