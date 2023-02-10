package team.waitingcatch.app.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.waitingcatch.app.event.entity.Event;

@Repository
public interface EventServiceRepository extends JpaRepository<Event,Long> {

}
