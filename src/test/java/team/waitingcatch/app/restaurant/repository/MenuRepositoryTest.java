package team.waitingcatch.app.restaurant.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuRepositoryTest {
	@Autowired
	MenuRepository menuRepository;

	@Test
	void findByRestaurantId() {
		menuRepository.findAllByRestaurantId(null);
	}
}