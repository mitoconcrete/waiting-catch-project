package team.waitingcatch.app.event.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.dto.event.CreateEventControllerRequest;
import team.waitingcatch.app.event.dto.event.CreateEventServiceRequest;
import team.waitingcatch.app.event.service.event.EventService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/seller")
public class SellerController {

	private final EventService eventService;

	//새로운 레스토랑 이벤트를 생성한다.
	@PostMapping("/restaurants/{restaurant_id}/events")
	public ResponseEntity<String> createSellerEvent(
		@RequestBody CreateEventControllerRequest createEventControllerRequest, @PathVariable Long restaurant_id) {
		CreateEventServiceRequest createEventServiceRequest = new CreateEventServiceRequest(
			createEventControllerRequest, restaurant_id);
		return ResponseEntity.status(HttpStatus.OK)
			.body(eventService.createSellerEvent(createEventServiceRequest));
	}

}
