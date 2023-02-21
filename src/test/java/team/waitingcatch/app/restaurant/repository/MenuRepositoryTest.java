package team.waitingcatch.app.restaurant.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import team.waitingcatch.app.common.Address;
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.restaurant.dto.blacklist.CreateBlacklistInternalServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.SaveDummyRestaurantRequest;
import team.waitingcatch.app.restaurant.entity.Blacklist;
import team.waitingcatch.app.restaurant.entity.BlacklistRequest;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.enums.UserRoleEnum;
import team.waitingcatch.app.user.repository.UserRepository;

@SpringBootTest
class MenuRepositoryTest {
	@Autowired
	BlacklistRequestRepository blacklistRequestRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	RestaurantRepository restaurantRepository;
	@Autowired
	MenuRepository menuRepository;

	@Test
	void test() {
		User seller = new User(UserRoleEnum.SELLER, "name", "aa@gmail.com", "username", "pw", "nickname",
			"01020202202");
		userRepository.save(seller);
		User user = new User(UserRoleEnum.USER, "name1", "bb@gmail.com", "username1", "pw", "nickname1", "01020202203");
		userRepository.save(user);
		Restaurant restaurant = new Restaurant(
			new SaveDummyRestaurantRequest("name", new Address("p", "c", "s"), new Position(50.0, 50.0), "01029292929",
				"category", seller));
		restaurantRepository.save(restaurant);
		BlacklistRequest blacklistRequest = new BlacklistRequest(restaurant, user, "des");
		blacklistRequestRepository.save(blacklistRequest);
		BlacklistRequest findBlacklistRequest = blacklistRequestRepository.findByIdWithRestaurant(1L).get();
		System.out.println("blacklistRequest.getUser().getId() = " + findBlacklistRequest.getUser().getId());
		System.out.println("blacklistRequest.getUser().getId() = " + findBlacklistRequest.getUser().getUsername());
	}

	@Test
	void findByRestaurantId() {
		menuRepository.findAllByRestaurantId(null);
	}
}