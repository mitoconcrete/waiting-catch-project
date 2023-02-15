package team.waitingcatch.app.event.service.usercoupon;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.dto.usercoupon.CreateUserCouponServiceRequest;
import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.entity.UserCoupon;
import team.waitingcatch.app.event.repository.UserCouponRepository;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.repository.UserRepository;
import team.waitingcatch.app.user.service.InternalUserService;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCouponServiceImpl implements UserCouponService, InternalUserCouponService {

	private final InternalUserService internalUserService;
	private final UserRepository userRepository;
	private final UserCouponRepository userCouponRepository;

	//유저 쿠폰을 생성한다
	@Override
	public void createUserCoupon(CreateUserCouponServiceRequest createUserCouponserviceRequest) {
		CouponCreator couponCreator = internalUserService._getUserByUsername(
			createUserCouponserviceRequest.getCreatorId());
		User user = userRepository.findByUsername(createUserCouponserviceRequest.getUsername()).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 유저입니다.")    //도달불가.옵티널이므로 명시적 기재
		);
		UserCoupon userCoupon = new UserCoupon(user, couponCreator);
		userCouponRepository.save(userCoupon);
	}
}
