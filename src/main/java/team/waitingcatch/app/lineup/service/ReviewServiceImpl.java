package team.waitingcatch.app.lineup.service;

import static team.waitingcatch.app.common.enums.ImageDirectoryEnum.*;
import static team.waitingcatch.app.exception.ErrorCode.*;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.util.image.ImageUploader;
import team.waitingcatch.app.exception.ErrorCode;
import team.waitingcatch.app.exception.IllegalRequestException;
import team.waitingcatch.app.lineup.dto.CreateReviewEntityRequest;
import team.waitingcatch.app.lineup.dto.CreateReviewServiceRequest;
import team.waitingcatch.app.lineup.dto.GetReviewResponse;
import team.waitingcatch.app.lineup.entity.Lineup;
import team.waitingcatch.app.lineup.entity.LineupHistory;
import team.waitingcatch.app.lineup.entity.Review;
import team.waitingcatch.app.lineup.enums.StoredLineupTableNameEnum;
import team.waitingcatch.app.lineup.repository.ReviewRepository;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.entity.RestaurantInfo;
import team.waitingcatch.app.restaurant.service.restaurant.InternalRestaurantService;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService, InternalReviewService {
	private final ReviewRepository reviewRepository;

	private final InternalRestaurantService internalRestaurantService;
	private final InternalLineupService internalLineupService;
	private final InternalLineupHistoryService internalLineupHistoryService;

	private final ImageUploader imageUploader;

	@Override
	public void createReview(CreateReviewServiceRequest serviceRequest) throws IOException {
		Lineup lineup = internalLineupService._getById(serviceRequest.getLineupId());
		if (!lineup.isReviewed()) {
			throw new IllegalRequestException(ALREADY_REVIEWD);
		}

		RestaurantInfo restaurantInfo = internalRestaurantService._getRestaurantInfoWithRestaurantByRestaurantId(
			serviceRequest.getRestaurantId());
		Restaurant restaurant = restaurantInfo.getRestaurant();

		List<String> imagePaths = imageUploader.uploadList(serviceRequest.getImages(), REVIEW.getValue());
		CreateReviewEntityRequest entityRequest = new CreateReviewEntityRequest(serviceRequest.getUser(), restaurant,
			serviceRequest.getRate(), serviceRequest.getContent(), imagePaths);

		reviewRepository.save(Review.of(entityRequest));
		restaurantInfo.updateAverageRate(entityRequest.getRate());

		if (serviceRequest.getType() == StoredLineupTableNameEnum.LINEUP) {
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
	public Slice<GetReviewResponse> getReviewsByRestaurantId(Long id, long restaurantId, Pageable pageable) {
		return reviewRepository.findAllByRestaurantId(id, restaurantId, pageable);
	}

	@Transactional(readOnly = true)
	@Override
	public Slice<GetReviewResponse> getReviewsByUserId(Long id, long userId, Pageable pageable) {
		return reviewRepository.findAllByUserId(id, userId, pageable);
	}

	@Override
	public void _bulkSoftDeleteByRestaurantId(Long restaurantId) {
		reviewRepository.bulkSoftDeleteByRestaurantId(restaurantId);
	}
}