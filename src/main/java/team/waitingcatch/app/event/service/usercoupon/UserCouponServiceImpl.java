package team.waitingcatch.app.event.service.usercoupon;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCouponServiceImpl implements UserCouponService, InternalUserCouponService {
}
