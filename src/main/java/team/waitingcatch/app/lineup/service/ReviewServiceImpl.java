package team.waitingcatch.app.lineup.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.util.S3Uploader;
import team.waitingcatch.app.lineup.dto.CreateReviewEntityRequest;
import team.waitingcatch.app.lineup.dto.CreateReviewServiceRequest;
import team.waitingcatch.app.lineup.entity.Review;
import team.waitingcatch.app.lineup.repository.ReviewRepository;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.service.restaurant.InternalRestaurantService;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService, InternalReviewService {
	private final ReviewRepository reviewRepository;

	private final InternalRestaurantService internalRestaurantService;

	private final S3Uploader s3Uploader;

	@Override
	public void createReview(CreateReviewServiceRequest serviceRequest) throws IOException {
		Restaurant restaurant = internalRestaurantService._getRestaurant(serviceRequest.getRestaurantId());
		List<String> imagePaths = s3Uploader.uploadList(serviceRequest.getImages(), "review");
		CreateReviewEntityRequest entityRequest = new CreateReviewEntityRequest(serviceRequest.getUser(), restaurant,
			serviceRequest.getRate(), serviceRequest.getContent(), imagePaths);
		reviewRepository.save(Review.craeteReview(entityRequest));
	}
}