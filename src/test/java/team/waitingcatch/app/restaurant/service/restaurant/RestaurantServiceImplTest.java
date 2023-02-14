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

		when(restaurant.getName()).thenReturn("aaaa");
		when(restaurantRepository.findById(any(Long.class))).thenReturn(Optional.of(restaurant));

		// when
		RestaurantBasicInfoResponse response = restaurantService.getRestaurantBasicInfo(request);

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
}