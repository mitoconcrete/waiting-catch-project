package team.waitingcatch.app.lineup.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.util.S3Uploader;
import team.waitingcatch.app.lineup.dto.CreateReviewEntityRequest;
import team.waitingcatch.app.lineup.dto.CreateReviewServiceRequest;
import team.waitingcatch.app.lineup.dto.GetReviewResponse;
import team.waitingcatch.app.lineup.entity.Lineup;
import team.waitingcatch.app.lineup.entity.LineupHistory;
import team.waitingcatch.app.lineup.entity.Review;
import team.waitingcatch.app.lineup.enums.StoredLineupTableNameEnum;
import team.waitingcatch.app.lineup.repository.ReviewRepository;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.service.restaurant.InternalRestaurantService;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService, InternalReviewService {
	private final ReviewRepository reviewRepository;

	private final InternalRestaurantService internalRestaurantService;
	private final InternalLineupService internalLineupService;
	private final InternalLineupHistoryService internalLineupHistoryService;

	private final S3Uploader s3Uploader;

	@Override
	public void createReview(CreateReviewServiceRequest serviceRequest) throws IOException {
		Restaurant restaurant = internalRestaurantService._getRestaurant(serviceRequest.getRestaurantId());
		List<String> imagePaths = s3Uploader.uploadList(serviceRequest.getImages(), "review");
		CreateReviewEntityRequest entityRequest = new CreateReviewEntityRequest(serviceRequest.getUser(), restaurant,
			serviceRequest.getRate(), serviceRequest.getContent(), imagePaths);

		reviewRepository.save(Review.craeteReview(entityRequest));

		if (serviceRequest.getType() == StoredLineupTableNameEnum.LINEUP) {
			Lineup lineup = internalLineupService._getById(serviceRequest.getLineupId());
			lineup.updateIsReviewed();
		} else {
			LineupHistory lineupHistory = internalLineupHistoryService._getById(serviceRequest.getLineupId());
			lineupHistory.updateIsReviewed();
		}
	}

	@Override
	public void deleteReview(Long reviewId) {
		Review review = reviewRepository.findById(reviewId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));
		review.softDelete();
	}

	@Transactional(readOnly = true)
	@Override
	public List<GetReviewResponse> getReviewsByRestaurantId(Long restaurantId) {
		return reviewRepository.findAllByRestaurantId(restaurantId);
	}

	@Transactional(readOnly = true)
	@Override
	public List<GetReviewResponse> getReviewsByUserId(Long userId) {
		return reviewRepository.findAllByUserId(userId);
	}
}