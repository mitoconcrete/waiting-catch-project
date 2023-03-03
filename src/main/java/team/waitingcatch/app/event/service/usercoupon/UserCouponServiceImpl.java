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
		//유저 아이디를 받아서 아이디에 맞는 쿠폰목록을 가저온다
		List<UserCoupon> userCoupons = userCouponRepository.findByUserWithUserAndCouponCreator(user);
		//유저쿠폰의 레스토랑 리스트 가저온다
		List<String> restaurants = userCouponRepository.findRestaurantNamesByUser(userCoupons);
		//쿠폰목록을 반환하기위한 리스트를 생성한다
		List<GetUserCouponResponse> getUserCouponResponses = new ArrayList<>();

		for (int i = 0; i < userCoupons.size(); i++) {
			UserCouponResponse userCouponResponse = new UserCouponResponse(userCoupons.get(i));
			String restaurantName = restaurants.get(i);
			GetUserCouponResponse getUserCouponResponse = new GetUserCouponResponse(userCouponResponse, restaurantName);
			getUserCouponResponses.add(getUserCouponResponse);

		}

		// for (UserCoupon userCoupon : userCoupons) {
		// 	//유저쿠폰을 담기위한 리스폰스를 만든다
		// 	UserCouponResponse userCouponResponse = new UserCouponResponse(userCoupon);
		// 	//유저쿠폰 레포지토리에서 해당유저쿠폰에 해당하는 레스토랑이름을 가저온다.(굳이?)
		// 	String restaurantName = userCouponRepository.findRestaurantNameByUserCoupon(userCoupon);
		// 	//반환하기위한 리스폰스+레스토랑 이름을 사용해 반환리스폰스를 작성한다
		// 	GetUserCouponResponse getUserCouponResponse = new GetUserCouponResponse(userCouponResponse, restaurantName);
		// 	getUserCouponResponses.add(getUserCouponResponse);
		// }
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
