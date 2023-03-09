package team.waitingcatch.app.restaurant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import team.waitingcatch.app.restaurant.entity.RestaurantInfo;
import team.waitingcatch.app.restaurant.repository.querydsl.RestaurantInfoRepositoryCustom;

public interface RestaurantInfoRepository extends JpaRepository<RestaurantInfo, Long>, RestaurantInfoRepositoryCustom {
	@Query("select ri from RestaurantInfo ri join ri.restaurant r where r.user.id = :userId")
	Optional<RestaurantInfo> findByUserId(@Param("userId") Long userId);
	
	@Query("select ri from RestaurantInfo ri join fetch ri.restaurant where ri.restaurant.id = :restaurantId")
	Optional<RestaurantInfo> findByRestaurantIdWithRestaurant(@Param("restaurantId") Long restaurantId);

	@Query("select ri from RestaurantInfo ri join fetch ri.restaurant r where r.user.id = :userId")
	Optional<RestaurantInfo> findByUserIdWithRestaurant(@Param("userId") Long userId);
}