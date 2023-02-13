package team.waitingcatch.app.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.restaurant.entity.CategoryRestaurant;

public interface RestaurantCategoryRepository extends JpaRepository<CategoryRestaurant, Long> {
}
