package team.waitingcatch.app.lineup.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.dto.GenericResponse;
import team.waitingcatch.app.lineup.dto.CreateReviewControllerRequest;
import team.waitingcatch.app.lineup.dto.CreateReviewServiceRequest;
import team.waitingcatch.app.lineup.dto.GetReviewResponse;
import team.waitingcatch.app.lineup.service.ReviewService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@RestController
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;

	@PostMapping("/restaurants/{restaurantId}/reviews")
	public void createReview(@PathVariable Long restaurantId,
		@RequestPart CreateReviewControllerRequest controllerRequest,
		@RequestPart(required = false) List<MultipartFile> images,
		@AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

		CreateReviewServiceRequest serviceRequest = new CreateReviewServiceRequest(userDetails.getUser(), restaurantId,
			controllerRequest.getRate(), controllerRequest.getContent(), images);
		reviewService.createReview(serviceRequest);
	}

	@DeleteMapping("/admin/review/{reviewId}")
	public void deleteReview(@PathVariable Long reviewId) {
		reviewService.deleteReview(reviewId);
	}

	@GetMapping("/restaurants/{restaurantId}/reviews")
	public GenericResponse<GetReviewResponse> getReviewsByRestaurant(@PathVariable Long restaurantId) {
		return new GenericResponse(reviewService.getReviewsByRestaurant(restaurantId));
	}
}