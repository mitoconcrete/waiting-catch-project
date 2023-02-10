package team.waitingcatch.app.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.waitingcatch.app.event.entity.CouponCreator;
@Repository
public interface CouponCreatorRepository extends JpaRepository<CouponCreator,Long> {
}
