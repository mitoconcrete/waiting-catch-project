package team.waitingcatch.app.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.restaurant.entity.BlackListRequest;

public interface BlackListRequestRepository extends JpaRepository<BlackListRequest, Long> {

}