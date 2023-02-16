package team.waitingcatch.app.restaurant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.restaurant.entity.BlackListRequest;

public interface BlackListRequestRepository extends JpaRepository<BlackListRequest, Long> {
	Optional<BlackListRequest> findByUser_IdAndRestaurant_User_Username(Long userId, String username);

}