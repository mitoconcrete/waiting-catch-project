package team.waitingcatch.app.restaurant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.restaurant.entity.BlackList;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {
	Optional<BlackList> findByUser_IdAndRestaurant_User_Username(Long userId, String sellerName);

	List<BlackList> findByRestaurant_Id(Long restaurantId);

	@Query("select b from BlackList b join fetch b.restaurant where b.id = :blacklistId")
	Optional<BlackList> findByIdWithRestaurant(@Param("blacklistId") Long id);

	List<BlackList> findAllByRestaurant_Id(Long restaurantId);
}