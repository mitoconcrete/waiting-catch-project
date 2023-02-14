package team.waitingcatch.app.event.service.usercoupon;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.dto.usercoupon.CreateUserCouponServiceRequest;
import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.entity.UserCoupon;
import team.waitingcatch.app.event.repository.UserCouponRepository;
import team.waitingcatch.app.event.service.couponcreator.InternalCouponCreatorService;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCouponServiceImpl implements UserCouponService, InternalUserCouponService {

	private final InternalCouponCreatorService internalCouponCreatorService;
	private final UserRepository userRepository;
	private final UserCouponRepository userCouponRepository;

	//유저 쿠폰을 생성한다
	@Override
	public String createUserCoupon(CreateUserCouponServiceRequest createUserCouponserviceRequest) {
		CouponCreator couponCreator = internalCouponCreatorService._getCouponCreatorFindById(
			createUserCouponserviceRequest.getCreatorId());
		User user = userRepository.findByUsername(createUserCouponserviceRequest.getUsername()).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 유저입니다.")    //도달불가.옵티널이므로 명시적 기재
		);
		UserCoupon userCoupon = new UserCoupon(user, couponCreator);
		userCouponRepository.save(userCoupon);
		return "쿠폰이 발급되었습니다.";
	}
}
