package team.waitingcatch.app.common;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Position {
    @Column(nullable = false)
    private float latitude;

    @Column(nullable = false)
    private float longitude;
}
