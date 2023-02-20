package team.waitingcatch.app.restaurant.service.restaurant;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.util.S3Uploader;
import team.waitingcatch.app.event.service.event.InternalEventService;
import team.waitingcatch.app.lineup.service.InternalLineupHistoryService;
import team.waitingcatch.app.lineup.service.InternalLineupService;
import team.waitingcatch.app.lineup.service.InternalReviewService;
import team.waitingcatch.app.restaurant.dto.restaurant.DeleteRestaurantByAdminServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.UpdateRestaurantEntityRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.UpdateRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.entity.RestaurantInfo;
import team.waitingcatch.app.restaurant.repository.RestaurantInfoRepository;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantServiceImpl implements RestaurantService, InternalRestaurantService {
	private final RestaurantRepository restaurantRepository;
	private final RestaurantInfoRepository restaurantInfoRepository;
	private final S3Uploader s3Uploader;

	private final InternalLineupService internalLineupService;

	private final InternalEventService internalEventService;

	private final InternalReviewService internalReviewService;

	private final InternalLineupHistoryService internalLineupHistoryService;

	@Override
	@Transactional(readOnly = true)
	public RestaurantBasicInfoResponse getRestaurantBasicInfo(RestaurantBasicInfoServiceRequest request) {
		Restaurant restaurant = _getRestaurant(request.getRestaurantId());
		return new RestaurantBasicInfoResponse(restaurant);
	}

	@Override
	@Transactional(readOnly = true)
	public RestaurantDetailedInfoResponse getRestaurantDetailedInfo(RestaurantDetailedInfoServiceRequest request) {
		Restaurant restaurant = _getRestaurant(request.getRestaurantId());
		return new RestaurantDetailedInfoResponse(restaurant);
	}

	@Override
	@Transactional(readOnly = true)
	public List<RestaurantResponse> getRestaurants() {
		return restaurantRepository.findAll().stream().map(RestaurantResponse::new).collect(Collectors.toList());
	}

	@Override
	public void deleteRestaurantByAdmin(DeleteRestaurantByAdminServiceRequest deleteRestaurantByAdminServiceRequest) {
		Restaurant restaurant = _getRestaurant(deleteRestaurantByAdminServiceRequest.getRestaurantId());
		restaurant.deleteRestaurant();
	}

	@Override
	public void updateRestaurant(UpdateRestaurantServiceRequest updateRestaurantServiceRequest) throws IOException {
		Restaurant restaurant = restaurantRepository.findByUser_Username(
				updateRestaurantServiceRequest.getSellerName())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 레스토랑 입니다."));
		RestaurantInfo restaurantInfo = restaurantInfoRepository.findById(restaurant.getId())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 레스토랑 정보입니다."));

		// 파일 업로드(여러개) 처리 부분

		String imageName = "";

		List<String> imageUrls = s3Uploader.uploadList(updateRestaurantServiceRequest.getFiles(), "restaurant");
		for (String imageUrl : imageUrls) {
			if (Objects.equals(imageUrl, "기본값")) {
				imageName += "기본값" + ",";
			}
			imageName += imageUrl + ",";
		}
		String lastCommaCutURL = imageName.substring(0, imageName.length() - 1);

		UpdateRestaurantEntityRequest updateRestaurantEntityRequest = new UpdateRestaurantEntityRequest(
			updateRestaurantServiceRequest, lastCommaCutURL);
		restaurant.updateRestaurant(updateRestaurantEntityRequest);
		restaurantInfo.updateRestaurantInfo(updateRestaurantEntityRequest);
	}

	@Override
	public Restaurant _getRestaurant(Long restaurantId) {
		return restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 레스토랑입니다."));
	}

	// @Override
	// public Restaurant _getRestaurantFindByUsername(String name) {
	// 	return restaurantRepository.findByName(name).orElseThrow(
	// 		() -> new IllegalArgumentException("레스토랑을 찾을수 없습니다.")
	// 	);
	// }

	@Override
	public Restaurant _getRestaurantByUserId(Long userId) {
		return restaurantRepository.findByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException("레스토랑을 찾을수 없습니다."));
	}

	@Override
	public void _openLineup(Long restaurantId) {
		RestaurantInfo restaurantInfo = restaurantInfoRepository.findById(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 레스토랑입니다."));
		restaurantInfo.openLineup();
	}

	@Override
	public void _closeLineup(Long restaurantId) {
		RestaurantInfo restaurantInfo = restaurantInfoRepository.findById(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 레스토랑입니다."));
		restaurantInfo.closeLineup();
	}

	@Override
	public void _deleteRestaurantBySellerId(Long sellerId) {
		Restaurant restaurant = _getRestaurantByUserId(sellerId);
		internalReviewService._bulkSoftDeleteByRestaurantId(restaurant.getId());
		internalLineupService._bulkSoftDeleteByRestaurantId(restaurant.getId());
		internalLineupHistoryService._bulkSoftDeleteByRestaurantId(restaurant.getId());
		internalEventService._bulkSoftDeleteByRestaurantId(restaurant.getId());
	}
}