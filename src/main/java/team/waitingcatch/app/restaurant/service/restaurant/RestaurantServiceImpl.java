package team.waitingcatch.app.restaurant.service.restaurant;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.util.DistanceCalculator;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantsResponse;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantServiceImpl implements RestaurantService, InternalRestaurantService {
	private final RestaurantRepository restaurantRepository;
	private final DistanceCalculator distanceCalculator;

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
				response.getLatitude(), response.getLongitude(), latitude, longitude)))
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<RestaurantResponse> getRestaurants() {
		return restaurantRepository.findAll().stream()
			.map(RestaurantResponse::new)
			.collect(Collectors.toList());
	}

	@Override
	public Restaurant _getRestaurant(Long restaurantId) {
		return restaurantRepository.findById(restaurantId).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 레스토랑입니다.")
		);
	}

	@Override
	public Restaurant _getRestaurantByUserId(Long userId) {
		return restaurantRepository.findByUserId(userId).orElseThrow(
			() -> new IllegalArgumentException("레스토랑을 찾을수 없습니다.")
		);
	}
}
