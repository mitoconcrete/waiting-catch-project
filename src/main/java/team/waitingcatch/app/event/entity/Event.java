package team.waitingcatch.app.event.entity;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column
    private Long restaurant_id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime event_start_date;

    @Column(nullable = false)
    private LocalDateTime event_end_date;

}
