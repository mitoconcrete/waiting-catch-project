package team.waitingcatch.app.lineup.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.dto.GenericResponse;
import team.waitingcatch.app.lineup.dto.CreateReviewControllerRequest;
import team.waitingcatch.app.lineup.dto.CreateReviewServiceRequest;
import team.waitingcatch.app.lineup.dto.GetReviewResponse;
import team.waitingcatch.app.lineup.enums.StoredLineupTableNameEnum;
import team.waitingcatch.app.lineup.service.ReviewService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {
	private final ReviewService reviewService;

	@PostMapping(value = "/customer/restaurants/{restaurantId}/reviews", consumes = {MediaType.APPLICATION_JSON_VALUE,
		MediaType.MULTIPART_FORM_DATA_VALUE})
	@ResponseStatus(HttpStatus.CREATED)
	public void createReview(
		@PathVariable long restaurantId,
		@Valid @RequestPart CreateReviewControllerRequest controllerRequest,
		@RequestPart(required = false) List<MultipartFile> images,
		@AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {

		CreateReviewServiceRequest serviceRequest = new CreateReviewServiceRequest(
			StoredLineupTableNameEnum.valueOf(controllerRequest.getType()), controllerRequest.getLineupId(),
			userDetails.getUser(), restaurantId, controllerRequest.getRate(), controllerRequest.getContent(), images);

		reviewService.createReview(serviceRequest);
	}

	@DeleteMapping("/general/admin/review/{reviewId}")
	public void deleteReview(@PathVariable long reviewId) {
		reviewService.deleteReview(reviewId);
	}

	@GetMapping("/general/restaurants/{restaurantId}/reviews")
	public GenericResponse<Slice<GetReviewResponse>> getReviewsByRestaurant(
		@PathVariable long restaurantId,
		@RequestParam(required = false) Long lastId,
		@PageableDefault Pageable pageable) {

		return new GenericResponse(reviewService.getReviewsByRestaurantId(lastId, restaurantId, pageable));
	}

	@GetMapping("/customer/reviews")
	public GenericResponse<Slice<GetReviewResponse>> getReviewsByUser(
		@RequestParam(required = false) Long lastId,
		@AuthenticationPrincipal UserDetailsImpl userDetails,
		@PageableDefault Pageable pageable) {

		return new GenericResponse(reviewService.getReviewsByUserId(lastId, userDetails.getId(), pageable));
	}
}