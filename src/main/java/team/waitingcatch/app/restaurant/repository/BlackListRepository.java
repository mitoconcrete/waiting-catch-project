package team.waitingcatch.app.restaurant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import team.waitingcatch.app.restaurant.entity.BlackList;

@Repository
public interface BlackListRepository extends JpaRepository<BlackList, Long> {

}