package team.waitingcatch.app.restaurant.service.restaurant;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.util.DistanceCalculator;
import team.waitingcatch.app.common.util.image.ImageUploader;
import team.waitingcatch.app.restaurant.dto.requestseller.ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.DeleteRestaurantByAdminServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantsWithinRadiusResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantsWithinRadiusServiceRequest;
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
	private final RestaurantInfoRepository restaurantInfoRepository;
	private final JPAQueryFactory queryFactory;

	private final ImageUploader imageUploader;
	private final DistanceCalculator distanceCalculator;

	@Override
	@Transactional(readOnly = true)
	public RestaurantBasicInfoResponse getRestaurantBasicInfo(RestaurantBasicInfoServiceRequest request) {
		Restaurant restaurant = _getRestaurantById(request.getRestaurantId());
		return new RestaurantBasicInfoResponse(restaurant);
	}

	@Override
	@Transactional(readOnly = true)
	public RestaurantDetailedInfoResponse getRestaurantDetailedInfo(RestaurantDetailedInfoServiceRequest request) {
		Restaurant restaurant = _getRestaurantById(request.getRestaurantId());
		RestaurantInfo restaurantInfo = restaurantInfoRepository.findByRestaurantId(restaurant.getId()).orElseThrow(
			() -> new IllegalArgumentException("레스토랑이 존재하지 않습니다.")
		);
		return new RestaurantDetailedInfoResponse(restaurant, restaurantInfo);
	}

	@Override
	@Transactional(readOnly = true)
	public List<SearchRestaurantsResponse> searchRestaurantsByKeyword(SearchRestaurantServiceRequest request) {
		String keyword = request.getKeyword();
		double latitude = request.getLatitude();
		double longitude = request.getLongitude();

		return restaurantInfoRepository.findRestaurantsBySearchKeywordsContaining(keyword).stream()
			.map(response -> new SearchRestaurantsResponse(
				response, distanceCalculator.distanceInKilometerByHaversine(
				latitude, longitude, response.getLatitude(), response.getLongitude())))
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<RestaurantsWithinRadiusResponse> getRestaurantsWithinRadius(
		RestaurantsWithinRadiusServiceRequest request) {
		return restaurantInfoRepository.findRestaurantsByDistance(request.getLatitude(), request.getLongitude(),
				request.getDistance())
			.stream()
			.map(response -> new RestaurantsWithinRadiusResponse(
				response, distanceCalculator.distanceInKilometerByHaversine(
				request.getLatitude(), request.getLongitude(), response.getLatitude(), response.getLongitude())))
			.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Page<RestaurantResponse> getRestaurants(Pageable pageable) {
		Page<Restaurant> restaurants = restaurantRepository.findAll(pageable);

		return new PageImpl<>(
			(restaurantRepository.findAll(pageable).getContent().stream().map(RestaurantResponse::new).collect(
				Collectors.toList())), pageable,
			restaurants.getTotalElements());
	}

	@Override
	public void deleteRestaurantByAdmin(DeleteRestaurantByAdminServiceRequest deleteRestaurantByAdminServiceRequest) {
		Restaurant restaurant = _getRestaurantById(deleteRestaurantByAdminServiceRequest.getRestaurantId());
		//String transferToString[] = restaurant.getImages().split(",");
		// for (int i = 0; i < transferToString.length; i++) {
		// 	s3Uploader.deleteS3(transferToString[i]);
		// }
		restaurant.deleteRestaurant();
	}
	// 현재 있는 것은

	//업데이트시 -> 현재 있는것은 1.있는것 2. 있는것 3. 새로 4.새로
	//업데이트시 -> 현재 있는것은 1.새로 2. 새로 3. 새로 4.새로
	@Override
	public void updateRestaurant(UpdateRestaurantServiceRequest updateRestaurantServiceRequest) throws IOException {
		Restaurant restaurant = restaurantRepository.findByUserId(updateRestaurantServiceRequest.getSellerId())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 레스토랑 입니다."));
		RestaurantInfo restaurantInfo = restaurantInfoRepository.findById(restaurant.getId())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 레스토랑 정보입니다."));
		String imageName = "";

		List<String> imageUrls = imageUploader.uploadList(updateRestaurantServiceRequest.getFiles(), "restaurant");
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
	public Restaurant _getRestaurantById(Long restaurantId) {
		return restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 레스토랑입니다."));
	}

	@Override
	public RestaurantInfo _getRestaurantInfoByRestaurantId(Long id) {
		return restaurantInfoRepository.findByRestaurantId(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 레스토랑 정보입니다."));
	}

	@Override
	public Restaurant _getRestaurantByUserId(Long userId) {
		return restaurantRepository.findByUserId(userId)
			.orElseThrow(() -> new IllegalArgumentException("레스토랑을 찾을 수 없습니다."));
	}

	@Override
	public void createRestaurant(ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest request) {
		Restaurant restaurant = new Restaurant(request);
		restaurantRepository.save(restaurant);
		RestaurantInfo restaurantInfo = new RestaurantInfo(restaurant);
		restaurantInfoRepository.save(restaurantInfo);
	}

	public void _openLineup(Long restaurantId) {
		RestaurantInfo restaurantInfo = restaurantInfoRepository.findByRestaurantId(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 레스토랑입니다."));
		restaurantInfo.openLineup();
	}

	@Override
	public void _closeLineup(Long restaurantId) {
		RestaurantInfo restaurantInfo = restaurantInfoRepository.findByRestaurantId(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 레스토랑입니다."));
		restaurantInfo.closeLineup();
	}

	@Override
	public Restaurant _deleteRestaurantBySellerId(Long sellerId) {
		Restaurant restaurant = _getRestaurantByUserId(sellerId);
		restaurant.deleteRestaurant();
		return restaurant;
	}
}