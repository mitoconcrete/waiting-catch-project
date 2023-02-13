package team.waitingcatch.app.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.restaurant.entity.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}