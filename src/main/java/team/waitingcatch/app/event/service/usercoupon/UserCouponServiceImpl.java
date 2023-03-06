package team.waitingcatch.app.event.service.usercoupon;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.dto.usercoupon.CreateUserCouponServiceRequest;
import team.waitingcatch.app.event.dto.usercoupon.GetUserCouponResponse;
import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.entity.UserCoupon;
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

	//유저 쿠폰을 생성한다
	@Override
	@Retryable(maxAttempts = 3, backoff = @Backoff(100), value = OptimisticLockingFailureException.class, exclude = {
		IllegalArgumentException.class})
	public void createUserCoupon(CreateUserCouponServiceRequest createUserCouponserviceRequest) {
		CouponCreator couponCreator = internalCouponCreatorService._getCouponCreatorById(
			createUserCouponserviceRequest.getCreatorId());
		User user = internalUserService._getUserByUsername(createUserCouponserviceRequest.getUsername());

		userCouponRepository.findUserCouponWithRelations(user, couponCreator).ifPresent(
			u -> {
				throw new IllegalArgumentException("이미 발급받은 쿠폰입니다.");
			}
		);
		UserCoupon userCoupon = new UserCoupon(user, couponCreator);
		boolean isCouponIssued = couponCreator.hasCouponBalance();
		if (isCouponIssued) {
			couponCreator.useCoupon();
			userCouponRepository.save(userCoupon);
		}

	}

	@Recover
	private void recover(OptimisticLockingFailureException e) {
		throw new IllegalArgumentException("요청이 많습니다. 다시 시도해주세요");
	}

	//유저 쿠폰을 조회한다.
	@Override
	public List<GetUserCouponResponse> getUserCoupons(User user) {
		return userCouponRepository.findRestaurantNameAndUserAll(user).stream()
			.map(response -> new GetUserCouponResponse(response))
			.collect(Collectors.toList());
	}

	@Override
	public void useCoupon(Long couponId) {
		UserCoupon userCoupon = userCouponRepository.findByIdAndIsUsedFalse(couponId).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다.")
		);
		userCoupon.useCoupon();
	}

}
}