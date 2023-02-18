package team.waitingcatch.app.event.service.usercoupon;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.dto.usercoupon.CreateUserCouponServiceRequest;
import team.waitingcatch.app.event.dto.usercoupon.GetUserCouponResponse;
import team.waitingcatch.app.event.dto.usercoupon.UserCouponResponse;
import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.entity.UserCoupon;
import team.waitingcatch.app.event.repository.CouponCreatorRepository;
import team.waitingcatch.app.event.repository.UserCouponRepository;
import team.waitingcatch.app.event.service.couponcreator.InternalCouponCreatorService;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.service.InternalUserService;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCouponServiceImpl implements UserCouponService, InternalUserCouponService {

	private final InternalCouponCreatorService internalCouponCreatorService;
	private final InternalUserService internalUserService;
	private final UserCouponRepository userCouponRepository;
	private final CouponCreatorRepository couponCreatorRepository;

	//유저 쿠폰을 생성한다
	@Override
	public void createUserCoupon(CreateUserCouponServiceRequest createUserCouponserviceRequest) {
		CouponCreator couponCreator = internalCouponCreatorService._getCouponCreatorById(
			createUserCouponserviceRequest.getCreatorId());
		User user = internalUserService._getUserByUsername(createUserCouponserviceRequest.getUsername());
		UserCoupon userCoupon = new UserCoupon(user, couponCreator);
		userCouponRepository.save(userCoupon);
	}

	//유저 쿠폰을 조회한다.
	@Override
	public List<GetUserCouponResponse> getUserCoupon(User user) {
		List<UserCoupon> userCoupons = userCouponRepository.findByUserAndIsUsedFalse(user);
		List<GetUserCouponResponse> getUserCouponResponses = new ArrayList<>();

		for (UserCoupon userCoupon : userCoupons) {
			CouponCreator couponCreator = couponCreatorRepository.findById(userCoupon.getId())
				.orElseThrow(() -> new IllegalArgumentException("해당 쿠폰생성자를 찾을수 없습니다."));
			UserCouponResponse userCouponResponse = new UserCouponResponse(userCoupon, couponCreator);

			GetUserCouponResponse getUserCouponResponse = new GetUserCouponResponse(userCouponResponse);
			getUserCouponResponses.add(getUserCouponResponse);
		}
		return getUserCouponResponses;
	}

}
