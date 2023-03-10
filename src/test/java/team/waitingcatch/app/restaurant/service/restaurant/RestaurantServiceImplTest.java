package team.waitingcatch.app.restaurant.service.restaurant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.web.multipart.MultipartFile;

import team.waitingcatch.app.common.Address;
import team.waitingcatch.app.common.util.DistanceCalculator;
import team.waitingcatch.app.common.util.image.ImageUploader;
import team.waitingcatch.app.restaurant.dto.requestseller.ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.DeleteRestaurantByAdminServiceRequest;
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

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

	@Mock
	private RestaurantRepository restaurantRepository;

	@Mock
	private RestaurantInfoRepository restaurantInfoRepository;

	@Mock
	private DistanceCalculator distanceCalculator;

	@Mock
	private ImageUploader imageUploader;

	@InjectMocks
	private RestaurantServiceImpl restaurantService;

	@Test
	@DisplayName("모든 레스토랑 조회")
	void getRestaurants() {
		Pageable pageable = mock(Pageable.class);
		// given
		User user = mock(User.class);
		List<Restaurant> restaurants = new ArrayList<>();
		Restaurant restaurant = mock(Restaurant.class);

		Page<Restaurant> page = new PageImpl<>(restaurants);

		when(restaurantRepository.findAll(pageable)).thenReturn(page);

		// when
		Page<RestaurantResponse> responses = restaurantService.getRestaurants(pageable);

		// then
		assertEquals(1, responses.getTotalPages());
	}

	@Test
	@DisplayName("레스토랑 기본정보 조회")
	void getRestaurantBasicInfo() {
		// given
		RestaurantBasicInfoServiceRequest request = mock(RestaurantBasicInfoServiceRequest.class);
		Restaurant restaurant = mock(Restaurant.class);
		Address address = mock(Address.class);

		when(restaurant.getName()).thenReturn("aaaa");
		// when(restaurant.getAddress()).thenReturn(address);
		// when(restaurant.getAddress().getProvince()).thenReturn("a");
		// when(restaurant.getAddress().getCity()).thenReturn("A");

		when(restaurantRepository.findById(any(Long.class))).thenReturn(Optional.of(restaurant));

		// when
		RestaurantBasicInfoResponse response = restaurantService.getRestaurantBasicInfo(request);

		// then
		assertEquals("aaaa", response.getName());
	}

	@Test
	@DisplayName("레스토랑 상세정보 조회")
	void getRestaurantDetailedInfo() {
		// given
		RestaurantDetailedInfoServiceRequest request = mock(RestaurantDetailedInfoServiceRequest.class);
		Restaurant restaurant = mock(Restaurant.class);
		RestaurantInfo restaurantInfo = mock(RestaurantInfo.class);
		Address address = mock(Address.class);
		List<String> search = new ArrayList<>();
		search.add("한식");
		search.add("중식");

		when(restaurant.getName()).thenReturn("aaaa");
		when(restaurant.getSearchKeywords()).thenReturn(search);
		when(restaurantInfo.getRestaurant()).thenReturn(restaurant);
		// when(restaurant.getAddress()).thenReturn(address);
		// when(restaurant.getAddress().getProvince()).thenReturn("a");
		// when(restaurant.getAddress().getCity()).thenReturn("A");

		// when(restaurantRepository.findById(any(Long.class))).thenReturn(Optional.of(restaurant));
		when(restaurantInfoRepository.findByRestaurantIdWithRestaurant(any(Long.class))).thenReturn(
			Optional.of(restaurantInfo));

		// when
		RestaurantDetailedInfoResponse response = restaurantService.getRestaurantDetailedInfo(request);

		// then
		assertEquals("aaaa", response.getName());
	}

	@Test
	@DisplayName("검색 키워드로 레스토랑 목록 조회")
	void searchRestaurantsByKeyword() {
		// given
		SearchRestaurantServiceRequest request = mock(SearchRestaurantServiceRequest.class);
		List<SearchRestaurantJpaResponse> contents = new ArrayList<>();
		SearchRestaurantJpaResponse jpaResponse = mock(SearchRestaurantJpaResponse.class);
		contents.add(jpaResponse);
		Pageable pageable = mock(Pageable.class);
		Slice<SearchRestaurantJpaResponse> searchRestaurantJpaResponses =
			new SliceImpl<>(contents, pageable, true);
		List<String> search = new ArrayList<>();
		search.add("한식");
		search.add("중식");

		when(request.getKeyword()).thenReturn("aa");
		when(jpaResponse.getSearchKeyword()).thenReturn(search);
		when(jpaResponse.getName()).thenReturn("aaa");

		when(restaurantInfoRepository.findRestaurantsBySearchKeywordsContaining(
			any(Long.class),
			any(String.class),
			any()))
			.thenReturn(searchRestaurantJpaResponses);

		// when
		Slice<SearchRestaurantsResponse> responses = restaurantService.searchRestaurantsByKeyword(request);

		// then
		assertEquals("aaa", responses.getContent().get(0).getName());
		assertTrue(responses.hasNext());
	}

	@Test
	@DisplayName(("반경 3km 이내의 레스토랑 조회"))
	void getRestaurantsWithin3kmRadius() {
		// given
		RestaurantsWithinRadiusServiceRequest request = mock(RestaurantsWithinRadiusServiceRequest.class);
		List<RestaurantsWithinRadiusJpaResponse> content = new ArrayList<>();
		RestaurantsWithinRadiusJpaResponse jpaResponse = mock(RestaurantsWithinRadiusJpaResponse.class);
		content.add(jpaResponse);
		Pageable pageable = mock(Pageable.class);
		Slice<RestaurantsWithinRadiusJpaResponse> restaurantsWithinRadiusJpaResponseSlice =
			new SliceImpl<>(content, pageable, true);
		List<String> search = new ArrayList<>();
		search.add("한식");
		search.add("중식");

		when(request.getLatitude()).thenReturn(0.0);
		when(request.getLongitude()).thenReturn(0.0);
		when(jpaResponse.getName()).thenReturn("aaa");
		when(jpaResponse.getSearchKeyword()).thenReturn(search);
		// when(jpaResponse.getLatitude()).thenReturn(0.0);
		// when(jpaResponse.getLongitude()).thenReturn(0.0);
		// when(distanceCalculator.distanceInKilometerByHaversine(0.0, 0.0, 0.0, 0.0)).thenReturn(0.0);
		when(restaurantInfoRepository.findRestaurantsByLatitudeAndLongitude(
			any(Double.class),
			any(double.class),
			any(double.class),
			any(double.class),
			any(double.class),
			any(double.class),
			any(double.class),
			any()))
			.thenReturn(restaurantsWithinRadiusJpaResponseSlice);

		// when
		Slice<RestaurantsWithinRadiusResponse> responses = restaurantService.getRestaurantsWithinRadius(request);

		// then
		assertEquals("aaa", responses.getContent().get(0).getName());
	}

	// @Test
	// @DisplayName("반경 3km 이내의 레스토랑 조회")
	// void getRestaurantsWithin3kmRadius() {
	// 	// given
	// 	RestaurantsWithinRadiusServiceRequest request = mock(RestaurantsWithinRadiusServiceRequest.class);
	// 	List<RestaurantsWithinRadiusJpaResponse> content = new ArrayList<>();
	// 	RestaurantsWithinRadiusJpaResponse jpaResponse = mock(RestaurantsWithinRadiusJpaResponse.class);
	// 	content.add(jpaResponse);
	// 	Pageable pageable = mock(Pageable.class);
	// 	Slice<RestaurantsWithinRadiusJpaResponse> restaurantsWithinRadiusJpaResponseSlice =
	// 		new SliceImpl<>(content, pageable, true);
	// 	List<String> search = new ArrayList<>();
	// 	search.add("한식");
	// 	search.add("중식");
	//
	// 	when(request.getLatitude()).thenReturn(0.0);
	// 	when(request.getLongitude()).thenReturn(0.0);
	// 	when(jpaResponse.getName()).thenReturn("aaa");
	// 	when(jpaResponse.getSearchKeyword()).thenReturn(search);
	// 	when(jpaResponse.getLatitude()).thenReturn(0.0);
	// 	when(jpaResponse.getLongitude()).thenReturn(0.0);
	// 	when(distanceCalculator.distanceInKilometerByHaversine(0.0, 0.0, 0.0, 0.0)).thenReturn(0.0);
	// 	when(restaurantInfoRepository.findRestaurantsByDistance(
	// 		any(Long.class),
	// 		any(double.class),
	// 		any(double.class),
	// 		any(int.class),
	// 		any()))
	// 		.thenReturn(restaurantsWithinRadiusJpaResponseSlice);
	//
	// 	// when
	// 	Slice<RestaurantsWithinRadiusResponse> responses = restaurantService.getRestaurantsWithinRadius(request);
	//
	// 	// then
	// 	assertEquals("aaa", responses.getContent().get(0).getName());
	//
	// }

	@Test
	@DisplayName("레스토랑 삭제")
	void deleteRestaurantByAdmin() {
		// given
		DeleteRestaurantByAdminServiceRequest request = mock(DeleteRestaurantByAdminServiceRequest.class);
		Restaurant restaurant = mock(Restaurant.class);

		when(restaurantRepository.findById(any(Long.class))).thenReturn(Optional.of(restaurant));

		// when
		restaurantService.deleteRestaurantByAdmin(request);

		// then
		verify(restaurant, times(1)).deleteRestaurant();
	}

	@Test
	@DisplayName("레스토랑 수정")
	void updateRestaurant() throws IOException {
		// given
		MultipartFile file1 = mock(MultipartFile.class);
		MultipartFile file2 = mock(MultipartFile.class);
		List<MultipartFile> imageUrls = new ArrayList<>();
		imageUrls.add(file1);
		imageUrls.add(file2);
		UpdateRestaurantServiceRequest request = mock(UpdateRestaurantServiceRequest.class);
		Restaurant restaurant = mock(Restaurant.class);
		when(request.getImages()).thenReturn(imageUrls);
		RestaurantInfo restaurantInfo = mock(RestaurantInfo.class);
		when(file1.getOriginalFilename()).thenReturn("");
		when(file2.getOriginalFilename()).thenReturn("asd");
		when(restaurantInfoRepository.findByUserIdWithRestaurant(anyLong())).thenReturn(Optional.of(restaurantInfo));
		when(restaurantInfo.getRestaurant()).thenReturn(restaurant);
		when(imageUploader.uploadList(any(List.class), any(String.class))).thenReturn(imageUrls);
		// when
		restaurantService.updateRestaurant(request);
		verify(restaurant, times(1)).updateRestaurant(any(UpdateRestaurantEntityRequest.class));
		verify(restaurantInfo, times(1)).updateRestaurantInfo(any(UpdateRestaurantEntityRequest.class));
		verify(restaurant, times(1)).updateRestaurantWithoutImage
			(any(UpdateRestaurantWithoutImageEntityRequest.class));
		verify(restaurantInfo, times(1)).updateRestaurantInfoWithoutImage(
			any(UpdateRestaurantWithoutImageEntityRequest.class));
	}

	@Test
	@DisplayName("레스토랑 조회 메소드")
	void _getRestaurant() {
		// given
		Restaurant restaurant = mock(Restaurant.class);

		when(restaurant.getName()).thenReturn("aaaa");
		when(restaurantRepository.findById(any(Long.class))).thenReturn(Optional.of(restaurant));

		// when
		Restaurant restaurant1 = restaurantService._getRestaurantById(any(Long.class));

		// then
		assertEquals("aaaa", restaurant1.getName());
	}

	@Test
	@DisplayName("RestaurantId로 RestaurantInfo 조회")
	void _getRestaurantInfoByRestaurantId() {
		// given
		RestaurantInfo restaurantInfo = mock(RestaurantInfo.class);
		Restaurant restaurant = mock(Restaurant.class);

		when(restaurantInfo.getRestaurant()).thenReturn(restaurant);
		when(restaurant.getName()).thenReturn("aaa");
		when(restaurantInfoRepository.findByRestaurantIdWithRestaurant(any(Long.class))).thenReturn(
			Optional.of(restaurantInfo));

		// when
		RestaurantInfo restaurantInfo1 = restaurantService._getRestaurantInfoWithRestaurantByRestaurantId(
			any(Long.class));

		// then
		assertEquals("aaa", restaurantInfo1.getRestaurant().getName());
	}

	@Test
	@DisplayName("userId로 레스토랑 조회")
	void _getRestaurantByUserId() {
		// given
		Restaurant restaurant = mock(Restaurant.class);
		User user = mock(User.class);

		when(restaurant.getUser()).thenReturn(user);
		when(restaurant.getUser().getId()).thenReturn(1L);
		when(restaurant.getName()).thenReturn("aaaa");

		when(restaurantRepository.findByUserId(any(Long.class))).thenReturn(Optional.of(restaurant));

		// when
		Restaurant restaurant1 = restaurantService._getRestaurantByUserId(any(Long.class));

		// then
		assertEquals("aaaa", restaurant1.getName());
		assertEquals(1L, restaurant1.getUser().getId());
	}

	@Test
	@DisplayName("레스토랑 생성")
	void _createRestaurant() {
		// given
		ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest request =
			mock(ApproveSignUpSellerManagementEntityPassToRestaurantEntityRequest.class);

		// when
		restaurantService._createRestaurant(request);

		// then
		verify(restaurantRepository, times(1)).save(any(Restaurant.class));
		verify(restaurantInfoRepository, times(1)).save(any(RestaurantInfo.class));
	}

	@Test
	@DisplayName("줄서기 활성화")
	void _openLineup() {
		// given
		RestaurantInfo restaurantInfo = mock(RestaurantInfo.class);

		when(restaurantInfoRepository.findByRestaurantId(any(Long.class))).thenReturn(Optional.of(restaurantInfo));

		// when
		restaurantService._openLineup(any(Long.class));

		// then
		verify(restaurantInfo, times(1)).openLineup();
	}

	@Test
	@DisplayName("줄서기 비활성화")
	void _closeLineup() {
		// given
		RestaurantInfo restaurantInfo = mock(RestaurantInfo.class);

		when(restaurantInfoRepository.findByRestaurantId(any(Long.class))).thenReturn(Optional.of(restaurantInfo));

		// when
		restaurantService._closeLineup(any(Long.class));

		// then
		verify(restaurantInfo, times(1)).closeLineup();
	}

	@Test
	@DisplayName("SellerId로 Restaurant 삭제")
	void _deleteRestaurantBySellerId() {
		// given
		Restaurant restaurant = mock(Restaurant.class);
		RestaurantInfo restaurantInfo = mock(RestaurantInfo.class);

		when(restaurantInfo.getRestaurant()).thenReturn(restaurant);
		when(restaurantInfoRepository.findByUserIdWithRestaurant(any(Long.class))).thenReturn(
			Optional.of(restaurantInfo));

		// when
		restaurant = restaurantService._deleteRestaurantBySellerId(any(Long.class));

		// then
		verify(restaurant, times(1)).deleteRestaurant();
	}
}