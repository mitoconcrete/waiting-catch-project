package team.waitingcatch.app.restaurant.service.restaurant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import team.waitingcatch.app.common.Address;
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.restaurant.dto.RestaurantBasicInfoResponse;
import team.waitingcatch.app.restaurant.dto.RestaurantBasicInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.RestaurantDetailedInfoResponse;
import team.waitingcatch.app.restaurant.dto.RestaurantDetailedInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.RestaurantResponse;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;
import team.waitingcatch.app.user.entitiy.User;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceImplTest {

	@Mock
	private RestaurantRepository restaurantRepository;

	@InjectMocks
	private RestaurantServiceImpl restaurantService;

	@Test
	@DisplayName("모든 레스토랑 조회")
	void getRestaurants() {
		// given
		User user = mock(User.class);
		Position position = mock(Position.class);
		Address address = mock(Address.class);
		List<Restaurant> restaurants = new ArrayList<>();
		Restaurant restaurant = mock(Restaurant.class);

		restaurants.add(restaurant);

		when(restaurantRepository.findAll()).thenReturn(restaurants);
		when(restaurant.getUser()).thenReturn(user);
		when(restaurant.getPosition()).thenReturn(position);
		when(restaurant.getAddress()).thenReturn(address);
		when(restaurant.getName()).thenReturn("aaaa");

		// when
		List<RestaurantResponse> responses = restaurantService.getRestaurants();

		// then
		assertEquals("aaaa", responses.get(0).getRestaurantName());
	}

	@Test
	@DisplayName("레스토랑 기본정보 조회")
	void getRestaurantBasicInfo() {
		// given
		RestaurantBasicInfoServiceRequest request = mock(RestaurantBasicInfoServiceRequest.class);
		Restaurant restaurant = mock(Restaurant.class);
		Address address = mock(Address.class);

		when(restaurant.getName()).thenReturn("aaaa");
		when(restaurant.getAddress()).thenReturn(address);
		when(restaurant.getAddress().getProvince()).thenReturn("a");
		when(restaurant.getAddress().getCity()).thenReturn("A");

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
		Address address = mock(Address.class);

		when(restaurant.getName()).thenReturn("aaaa");
		when(restaurant.getAddress()).thenReturn(address);
		when(restaurant.getAddress().getProvince()).thenReturn("a");
		when(restaurant.getAddress().getCity()).thenReturn("A");

		when(restaurantRepository.findById(any(Long.class))).thenReturn(Optional.of(restaurant));

		// when
		RestaurantDetailedInfoResponse response = restaurantService.getRestaurantDetailedInfo(request);

		// then
		assertEquals("aaaa", response.getName());
	}

	@Test
	@DisplayName("레스토랑 조회 메소드")
	void _getRestaurant() {
		// given
		Restaurant restaurant = mock(Restaurant.class);

		when(restaurant.getName()).thenReturn("aaaa");
		when(restaurantRepository.findById(any(Long.class))).thenReturn(Optional.of(restaurant));

		// when
		Restaurant restaurant1 = restaurantService._getRestaurant(any(Long.class));

		// then
		assertEquals("aaaa", restaurant1.getName());
	}

	// @Test
	// @DisplayName("username으로 레스토링 조회")
	// void _getRestaurantFindByUsername() {
	// 	// given
	// 	Restaurant restaurant = mock(Restaurant.class);
	// 	User user = mock(User.class);
	//
	// 	when(restaurant.getUser()).thenReturn(user);
	// 	when(restaurant.getUser().getUsername()).thenReturn("a");
	// 	when(restaurant.getName()).thenReturn("aaaa");
	//
	// 	when(restaurantRepository.findByName(any(String.class))).thenReturn(Optional.of(restaurant));
	//
	// 	// when
	// 	Restaurant restaurant1 = restaurantService._getRestaurantFindByUsername(any(String.class));
	//
	// 	// then
	// 	assertEquals("aaaa", restaurant1.getName());
	// 	assertEquals("a", restaurant1.getUser().getUsername());
	// }

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
}