package team.waitingcatch.app.event.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_coupon_id")
    private Long id;

    @Column(nullable = false)
    private Long user_id;

    @Column(nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private CouponCreator coupon_creator_id;

    @Column(nullable = false)
    private boolean is_used;

}
