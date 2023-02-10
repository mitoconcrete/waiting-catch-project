package team.waitingcatch.app.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import team.waitingcatch.app.restaurant.entity.RestaurantInfo;

public interface RestaurantInfoRepository extends JpaRepository<RestaurantInfo, Long> {
}
