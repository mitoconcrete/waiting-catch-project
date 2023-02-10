package team.waitingcatch.app.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import team.waitingcatch.app.restaurant.entity.BlackList;
import team.waitingcatch.app.restaurant.entity.BlackListRequest;

@Repository
public interface BlackListRequestRepository extends JpaRepository<BlackListRequest, Long> {

}