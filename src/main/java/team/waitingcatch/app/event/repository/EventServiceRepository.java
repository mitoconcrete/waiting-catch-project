package team.waitingcatch.app.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import team.waitingcatch.app.event.entity.Event;

public interface EventServiceRepository extends JpaRepository<Event, Long> {

}
