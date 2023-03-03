package team.waitingcatch.app.event.service.usercoupon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.OptimisticLockException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.dto.usercoupon.CreateUserCouponServiceRequest;
import team.waitingcatch.app.event.dto.usercoupon.GetUserCouponResponse;
import team.waitingcatch.app.event.dto.usercoupon.UserCouponResponse;
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
	public void createUserCoupon(CreateUserCouponServiceRequest createUserCouponserviceRequest) {
		CouponCreator couponCreator = internalCouponCreatorService._getCouponCreatorById(
			createUserCouponserviceRequest.getCreatorId());
		User user = internalUserService._getUserByUsername(createUserCouponserviceRequest.getUsername());

		int retryCount = 3;
		// 쿠폰 발급 가능 여부를 확인하고, 발급 처리합니다.
		Optional<UserCoupon> userCoupon = null;

		for (int i = 1; i <= retryCount; i++) {
			try {
				//이미 발급받은 쿠폰이 있는지
				userCoupon = userCouponRepository.findUserCouponWithRelations(user, couponCreator);
				if (userCoupon.isPresent()) {
					throw new IllegalArgumentException("이미 발급받은 쿠폰입니다.");
				}
				userCoupon = Optional.of(new UserCoupon(user, couponCreator));
				boolean isCouponIssued = userCoupon.get().issueCoupon();
				if (isCouponIssued) {
					userCouponRepository.save(userCoupon.get());
					break;
				}
			} catch (OptimisticLockException ex) {
				if (i == retryCount) {
					throw new IllegalArgumentException("시도횟수가 초과하였습니다. 다시 시도해주세요");
				}
			}
		}

	}

	//유저 쿠폰을 조회한다.
	@Override
	public List<GetUserCouponResponse> getUserCoupons(User user) {
		//List<UserCoupon> userCoupons = userCouponRepository.findByUserAndIsUsedFalse(user);
		List<UserCoupon> userCoupons = userCouponRepository.findByUserWithUserAndCouponCreator(user);
		List<GetUserCouponResponse> getUserCouponResponses = new ArrayList<>();

		for (UserCoupon userCoupon : userCoupons) {
			UserCouponResponse userCouponResponse = new UserCouponResponse(userCoupon);
			String restaurantName = userCouponRepository.findRestaurantNameByUserCoupon(userCoupon);
			GetUserCouponResponse getUserCouponResponse = new GetUserCouponResponse(userCouponResponse, restaurantName);
			getUserCouponResponses.add(getUserCouponResponse);
		}
		return getUserCouponResponses;
	}

	@Override
	public void useCoupon(Long couponId) {
		UserCoupon userCoupon = userCouponRepository.findByIdAndIsUsedFalse(couponId).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다.")
		);
		userCoupon.useCoupon();
	}

}
