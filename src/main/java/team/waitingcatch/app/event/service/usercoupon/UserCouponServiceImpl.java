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
		if (couponCreator.createCoupon()) {
			userCouponRepository.save(userCoupon);
		}
		throw new IllegalArgumentException("쿠폰이 모두 소진되었습니다.");

	}

	//유저 쿠폰을 조회한다.
	@Override
	public List<GetUserCouponResponse> getUserCoupons(User user) {
		List<UserCoupon> userCoupons = userCouponRepository.findByUserAndIsUsedFalse(user);
		System.out.println(userCoupons.size());
		List<GetUserCouponResponse> getUserCouponResponses = new ArrayList<>();

		for (UserCoupon userCoupon : userCoupons) {
			UserCouponResponse userCouponResponse = new UserCouponResponse(userCoupon);
			String restaurantName = userCoupon.getCouponCreator().getEvent().getRestaurant().getName();
			GetUserCouponResponse getUserCouponResponse = new GetUserCouponResponse(userCouponResponse, restaurantName);
			getUserCouponResponses.add(getUserCouponResponse);
		}
		return getUserCouponResponses;
	}

	@Override
	public void useCoupon(Long couponId) {
		UserCoupon userCoupon = userCouponRepository.findByUserCouponIdAndIsUsedFalse(couponId).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다.")
		);
		userCoupon.useCoupon();
	}

}
