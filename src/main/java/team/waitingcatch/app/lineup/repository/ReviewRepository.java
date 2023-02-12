package team.waitingcatch.app.lineup.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.lineup.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}