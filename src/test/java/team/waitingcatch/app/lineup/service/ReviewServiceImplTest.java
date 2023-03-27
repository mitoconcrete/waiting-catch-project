package team.waitingcatch.app.lineup.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static team.waitingcatch.app.exception.ErrorCode.*;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import team.waitingcatch.app.common.util.image.ImageUploader;
import team.waitingcatch.app.exception.IllegalRequestException;
import team.waitingcatch.app.lineup.dto.CreateReviewEntityRequest;
import team.waitingcatch.app.lineup.dto.CreateReviewServiceRequest;
import team.waitingcatch.app.lineup.entity.Lineup;
import team.waitingcatch.app.lineup.entity.LineupHistory;
import team.waitingcatch.app.lineup.entity.Review;
import team.waitingcatch.app.lineup.enums.StoredLineupTableNameEnum;
import team.waitingcatch.app.lineup.repository.ReviewRepository;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.entity.RestaurantInfo;
import team.waitingcatch.app.restaurant.service.restaurant.InternalRestaurantService;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {
	@InjectMocks
	ReviewServiceImpl reviewServiceImpl;

	@Mock
	ReviewRepository reviewRepository;

	@Mock
	InternalRestaurantService internalRestaurantService;

	@Mock
	InternalLineupService internalLineupService;

	@Mock
	InternalLineupHistoryService internalLineupHistoryService;

	@Mock
	ImageUploader imageUploader;

	@Test
	@DisplayName("리뷰 작성 - 이미 작성한 리뷰")
	void createReview_alreadyReviewed(@Mock CreateReviewServiceRequest serviceRequest) {
		when(serviceRequest.getLineupId()).thenReturn(1L);

		Lineup lineup = mock(Lineup.class);
		when(internalLineupService._getById(serviceRequest.getLineupId())).thenReturn(lineup);
		when(lineup.isReviewed()).thenReturn(true);

		assertThatThrownBy(() -> reviewServiceImpl.createReview(serviceRequest))
			.isInstanceOf(IllegalRequestException.class)
			.hasMessage(ALREADY_REVIEWD.getMessage());
	}

	@Test
	@DisplayName("리뷰 작성 - 정상 작동")
	void createReview(@Mock CreateReviewServiceRequest serviceRequest) throws IOException {
		when(serviceRequest.getLineupId()).thenReturn(1L);

		Lineup lineup = mock(Lineup.class);
		when(internalLineupService._getById(serviceRequest.getLineupId())).thenReturn(lineup);
		when(lineup.isReviewed()).thenReturn(false);

		RestaurantInfo restaurantInfo = mock(RestaurantInfo.class);
		Restaurant restaurant = mock(Restaurant.class);

		when(internalRestaurantService._getRestaurantInfoWithRestaurantByRestaurantId(serviceRequest.getRestaurantId()))
			.thenReturn(restaurantInfo);
		when(restaurantInfo.getRestaurant()).thenReturn(restaurant);

		CreateReviewEntityRequest entityRequest = mock(CreateReviewEntityRequest.class);
		when(serviceRequest.getType())
			.thenReturn(StoredLineupTableNameEnum.LINEUP)
			.thenReturn(StoredLineupTableNameEnum.LINEUP_HISTORY);

		reviewServiceImpl.createReview(serviceRequest);

		LineupHistory lineupHistory = mock(LineupHistory.class);
		when(internalLineupHistoryService._getById(serviceRequest.getLineupId())).thenReturn(lineupHistory);

		reviewServiceImpl.createReview(serviceRequest);

		verify(reviewRepository, times(2)).save(any(Review.class));
		verify(restaurantInfo, times(2)).updateAverageRate(entityRequest.getRate());
		verify(lineup).updateIsReviewed();
		verify(lineupHistory).updateIsReviewed();
	}

	@Test
	@DisplayName("리뷰 삭제 - 존재하지 않는 리뷰")
	void deleteReview_notFoundReview() {
		when(reviewRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> reviewServiceImpl.deleteReview(1L))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(NOT_FOUND_REVIEW.getMessage());
	}

	@Test
	@DisplayName("리뷰 삭제 - 정상 작동")
	void deleteReview() {
		Review review = mock(Review.class);
		when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));

		reviewServiceImpl.deleteReview(1L);

		verify(review).softDelete();
	}
}