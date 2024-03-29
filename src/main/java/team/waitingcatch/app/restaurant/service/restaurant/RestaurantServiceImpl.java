package team.waitingcatch.app.restaurant.service.restaurant;

import static team.waitingcatch.app.common.enums.ImageDirectoryEnum.*;
import static team.waitingcatch.app.exception.ErrorCode.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import team.waitingcatch.app.common.util.DistanceCalculator;
import team.waitingcatch.app.common.util.image.ImageUploader;
import team.waitingcatch.app.restaurant.dto.requestseller.ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.DeleteRestaurantByAdminServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.GetRestaurantInfo;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantsWithinRadiusJpaResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantsWithinRadiusResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantsWithinRadiusServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantJpaResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantsResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.UpdateRestaurantEntityRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.UpdateRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.UpdateRestaurantWithoutImageEntityRequest;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.entity.RestaurantInfo;
import team.waitingcatch.app.restaurant.repository.RestaurantInfoRepository;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;
import team.waitingcatch.app.user.entitiy.User;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RestaurantServiceImpl implements RestaurantService, InternalRestaurantService {
	private final RestaurantRepository restaurantRepository;
	private final RestaurantInfoRepository restaurantInfoRepository;
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
		RestaurantInfo restaurantInfo = _getRestaurantInfoByRestaurantId(request.getRestaurantId());
		Restaurant restaurant = restaurantInfo.getRestaurant();
		return new RestaurantDetailedInfoResponse(restaurant, restaurantInfo);
	}

	@Override
	@Transactional(readOnly = true)
	public Slice<SearchRestaurantsResponse> searchRestaurantsByKeyword(SearchRestaurantServiceRequest request) {
		Slice<SearchRestaurantJpaResponse> jpaResponses =
			restaurantInfoRepository.findRestaurantsBySearchKeywordsContaining(
				request.getId(), request.getKeyword(), request.getPageable()
			);

		List<SearchRestaurantsResponse> content = new ArrayList<>();
		for (SearchRestaurantJpaResponse response : jpaResponses) {
			double distance = distanceCalculator.distanceInKilometerByHaversine(
				request.getLongitude(),
				request.getLatitude(),
				response.getLongitude(),
				response.getLatitude()
			);
			content.add(new SearchRestaurantsResponse(response, distance));
		}

		return new SliceImpl<>(content, jpaResponses.getPageable(), jpaResponses.hasNext());
	}

	@Override
	@Transactional(readOnly = true)
	public Slice<RestaurantsWithinRadiusResponse> getRestaurantsWithinRadius(
		RestaurantsWithinRadiusServiceRequest request) {
		// 현재 좌표
		double latitude = request.getLatitude();
		double longitude = request.getLongitude();
		// 좌표 이동 값
		double deltaLatitude =
			(1 / (6371 * (Math.PI / 180))) / 1000 * (request.getDistance() * 1000);
		double deltaLongitude =
			(1 / (6371 * (Math.PI / 180) * Math.cos(Math.toRadians(latitude)))) / 1000 * (request.getDistance() * 1000);

		// 현재 위치 기준 최대, 최소 좌표
		double maxLatitude = latitude + deltaLatitude;
		double minLatitude = latitude - deltaLatitude;
		double maxLongitude = longitude + deltaLongitude;
		double minLongitude = longitude - deltaLongitude;

		Slice<RestaurantsWithinRadiusJpaResponse> jpaResponses =
			restaurantInfoRepository.findRestaurantsByLatitudeAndLongitude(
				request.getLastDistance(), latitude, longitude, maxLatitude, maxLongitude,
				minLatitude, minLongitude, request.getPageable()
			);

		List<RestaurantsWithinRadiusResponse> content = jpaResponses.stream()
			.filter(restaurant -> restaurant.getDistanceBetween() <= request.getDistance())
			.map(RestaurantsWithinRadiusResponse::new)
			.collect(Collectors.toList());

		return new SliceImpl<>(content, jpaResponses.getPageable(), jpaResponses.hasNext());
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
	@Transactional(readOnly = true)
	public Page<RestaurantResponse> getRestaurantsByRestaurantName(String searchVal, Pageable pageable) {
		Page<Restaurant> restaurants = restaurantRepository.findByNameContaining(searchVal, pageable);
		return new PageImpl<>(
			(restaurantRepository.findByNameContaining(searchVal, pageable)
				.getContent()
				.stream()
				.map(RestaurantResponse::new)
				.collect(
					Collectors.toList())), pageable,
			restaurants.getTotalElements());
	}

	@Override
	public boolean deleteRestaurantByAdmin(
		DeleteRestaurantByAdminServiceRequest deleteRestaurantByAdminServiceRequest) {
		Restaurant restaurant = _getRestaurantById(deleteRestaurantByAdminServiceRequest.getRestaurantId());
		return restaurant.deleteRestaurant();

	}

	/*
		현재 있는 것은
		업데이트시 -> 현재 있는것은 1.있는것 2. 있는것 3. 새로 4.새로
		업데이트시 -> 현재 있는것은 1.새로 2. 새로 3. 새로 4.새로
	 */
	@Override
	public void updateRestaurant(UpdateRestaurantServiceRequest serviceRequest) throws IOException {
		RestaurantInfo restaurantInfo = _getRestaurantInfoWithRestaurantByUserId(serviceRequest.getSellerId());
		Restaurant restaurant = restaurantInfo.getRestaurant();

		for (MultipartFile multipartFile : serviceRequest.getImages()) {
			if (Objects.equals(multipartFile.getOriginalFilename(), "")) {
				UpdateRestaurantWithoutImageEntityRequest updateRestaurantEntityRequest = new UpdateRestaurantWithoutImageEntityRequest(
					serviceRequest);
				restaurant.updateRestaurantWithoutImage(updateRestaurantEntityRequest);
				restaurantInfo.updateRestaurantInfoWithoutImage(updateRestaurantEntityRequest);
			} else {
				List<String> imagePaths = imageUploader.uploadList(serviceRequest.getImages(),
					RESTAURANT.getValue());

				UpdateRestaurantEntityRequest updateRestaurantEntityRequest = new UpdateRestaurantEntityRequest(
					serviceRequest, imagePaths);
				restaurant.updateRestaurant(updateRestaurantEntityRequest);
				restaurantInfo.updateRestaurantInfo(updateRestaurantEntityRequest);
			}
		}

	}

	@Override
	public Restaurant _getRestaurantById(Long restaurantId) {
		return restaurantRepository.findById(restaurantId)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_RESTAURANT.getMessage()));
	}

	@Override
	public Restaurant _getRestaurantByUserId(Long userId) {
		return restaurantRepository.findByUserId(userId)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_RESTAURANT.getMessage()));
	}

	public RestaurantInfo _getRestaurantInfoByRestaurantId(Long restaurantId) {
		return restaurantInfoRepository.findByRestaurantIdWithRestaurant(restaurantId)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_RESTAURANT.getMessage()));
	}

	@Override
	public RestaurantInfo _getRestaurantInfoByUserId(Long userId) {
		return restaurantInfoRepository.findByUserId(userId)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_RESTAURANT_INFO.getMessage()));
	}

	@Override
	public RestaurantInfo _getRestaurantInfoWithRestaurantByRestaurantId(Long restaurantId) {
		return restaurantInfoRepository.findByRestaurantIdWithRestaurant(restaurantId)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_RESTAURANT_INFO.getMessage()));
	}

	@Override
	public RestaurantInfo _getRestaurantInfoWithRestaurantByUserId(Long userId) {
		return restaurantInfoRepository.findByUserIdWithRestaurant(userId)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_RESTAURANT_INFO.getMessage()));
	}

	@Override
	public void _createRestaurant(ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest request) {
		Restaurant restaurant = new Restaurant(request);
		restaurantRepository.save(restaurant);
		RestaurantInfo restaurantInfo = new RestaurantInfo(restaurant);
		restaurantInfoRepository.save(restaurantInfo);
	}

	public void _openLineup(Long restaurantId) {
		RestaurantInfo restaurantInfo = restaurantInfoRepository.findByRestaurantId(restaurantId)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_RESTAURANT_INFO.getMessage()));
		restaurantInfo.openLineup();
	}

	@Override
	public void _closeLineup(Long restaurantId) {
		RestaurantInfo restaurantInfo = restaurantInfoRepository.findByRestaurantId(restaurantId)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_RESTAURANT_INFO.getMessage()));
		restaurantInfo.closeLineup();
	}

	@Override
	public Restaurant _deleteRestaurantBySellerId(Long sellerId) {
		RestaurantInfo restaurantInfo = _getRestaurantInfoWithRestaurantByUserId(sellerId);
		Restaurant restaurant = restaurantInfo.getRestaurant();

		restaurant.deleteRestaurant();
		restaurantInfo.deleteRestaurantInfo();

		return restaurant;
	}

	@Override
	public GetRestaurantInfo getRestaurantInfo(User user) {
		RestaurantInfo restaurantInfo = _getRestaurantInfoWithRestaurantByUserId(user.getId());
		Restaurant restaurant = restaurantInfo.getRestaurant();
		return new GetRestaurantInfo(restaurant.getPhoneNumber(), restaurant.getCapacity(), restaurant.getDescription(),
			restaurantInfo.getOpenTime(), restaurantInfo.getCloseTime());
	}

}