package team.waitingcatch.app.restaurant.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.restaurant.entity.Menu;

public interface MenuRepository extends JpaRepository<Menu, Long> {
	@Query(value = "select m from Menu m where m.restaurant.id = :restaurantId")
	List<Menu> findAllByRestaurantId(@Param("restaurantId") Long restaurantId);

	@Modifying
	@Query(value = "update Menu m set m.is_deleted = true where m.restaurant_id = :restaurantId", nativeQuery = true)
	void bulkSoftDeleteByRestaurantId(@Param("restaurantId") Long restaurantId);
}
