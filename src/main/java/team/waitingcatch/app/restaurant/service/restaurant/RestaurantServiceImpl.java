package team.waitingcatch.app.restaurant.service.restaurant;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.util.S3Uploader;
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
		String url = s3Uploader.upload(updateRestaurantServiceRequest.getFile(), "restaurant");
		UpdateRestaurantEntityRequest updateRestaurantEntityRequest = new UpdateRestaurantEntityRequest(
			updateRestaurantServiceRequest, url);
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
}
