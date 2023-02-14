package team.waitingcatch.app.restaurant.service.restaurant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import team.waitingcatch.app.common.Address;
import team.waitingcatch.app.common.Position;
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
}