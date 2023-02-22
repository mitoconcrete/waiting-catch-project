package team.waitingcatch.app.restaurant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.restaurant.entity.Blacklist;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
	Optional<Blacklist> findByUser_IdAndRestaurant_User_Username(Long userId, String sellerName);

	@Query("select b from  Blacklist b join fetch b.restaurant")
	Optional<Blacklist> findByIdWithRestaurant(@Param("blacklistId") Long id);

	List<Blacklist> findByRestaurant_Id(Long restaurantId);

	List<Blacklist> findAllByRestaurant_Id(Long restaurantId);
}