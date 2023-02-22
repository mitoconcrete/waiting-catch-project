package team.waitingcatch.app.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.restaurant.entity.RestaurantInfo;
import team.waitingcatch.app.restaurant.repository.querydsl.RestaurantInfoRepositoryCustom;

public interface RestaurantInfoRepository
	extends JpaRepository<RestaurantInfo, Long>, RestaurantInfoRepositoryCustom {
}
