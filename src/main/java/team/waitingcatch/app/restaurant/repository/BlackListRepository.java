package team.waitingcatch.app.restaurant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.restaurant.entity.BlackList;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {
	Optional<BlackList> findByUser_IdAndRestaurant_User_Username(Long userId, String sellerName);
}