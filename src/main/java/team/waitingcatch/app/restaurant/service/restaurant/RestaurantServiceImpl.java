package team.waitingcatch.app.restaurant.service.restaurant;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import team.waitingcatch.app.common.util.DistanceCalculator;
import team.waitingcatch.app.common.util.S3Uploader;
import team.waitingcatch.app.restaurant.dto.restaurant.DeleteRestaurantByAdminServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantsWithin3kmRadiusResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantsWithin3kmRadiusServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantsResponse;
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
	private final DistanceCalculator distanceCalculator;
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
	public List<SearchRestaurantsResponse> searchRestaurantsByKeyword(SearchRestaurantServiceRequest request) {
		String keyword = request.getKeyword();
		double latitude = request.getLatitude();
		double longitude = request.getLongitude();

		return restaurantRepository.findRestaurantsBySearchKeywordsContaining(keyword).stream()
			.map(response -> new SearchRestaurantsResponse(
				response, distanceCalculator.distanceInKilometerByHaversine(
				latitude, longitude, response.getLatitude(), response.getLongitude())))
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<RestaurantsWithin3kmRadiusResponse> getRestaurantsWithin3kmRadius(
		RestaurantsWithin3kmRadiusServiceRequest request) {
		return restaurantRepository.findRestaurantsByDistance(request.getLatitude(), request.getLongitude()).stream()
			.map(response -> new RestaurantsWithin3kmRadiusResponse(
				response, distanceCalculator.distanceInKilometerByHaversine(
				request.getLatitude(), request.getLongitude(), response.getLatitude(), response.getLongitude())))
			.collect(Collectors.toList());
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

	@Override
	public Restaurant _getRestaurantByUserId(Long userId) {
		return restaurantRepository.findByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException("레스토랑을 찾을수 없습니다."));
	}
}
