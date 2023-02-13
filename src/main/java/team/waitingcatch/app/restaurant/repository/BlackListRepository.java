package team.waitingcatch.app.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.restaurant.entity.BlackList;

public interface BlackListRepository extends JpaRepository<BlackList, Long> {

}