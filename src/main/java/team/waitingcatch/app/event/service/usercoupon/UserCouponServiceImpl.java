package team.waitingcatch.app.event.service.usercoupon;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.dto.usercoupon.CreateUserCouponServiceRequest;
import team.waitingcatch.app.event.dto.usercoupon.GetUserCouponResponse;
import team.waitingcatch.app.event.dto.usercoupon.UserCouponServiceResponse;
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
	//0.1초 지연후 3번까지 트라이, IllegalStateException발생시 리트라이, IllegalArgumentException는 익셉션처리
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
		// boolean isCouponIssued = userCoupon.issueCoupon();
		if (isCouponIssued) {
			couponCreator.useCoupon();
			userCouponRepository.save(userCoupon);
		} else {
			throw new OptimisticLockingFailureException("요청이 많습니다. 다시 시도해주세요");
		}

	}

	//유저 쿠폰을 조회한다.
	@Override
	public List<GetUserCouponResponse> getUserCoupons(User user) {
		//List<UserCoupon> userCoupons = userCouponRepository.findByUserAndIsUsedFalse(user);
		//유저 아이디를 받아서 아이디에 맞는 쿠폰목록을 가저온다
		//List<UserCoupon> userCoupons = userCouponRepository.findByUserWithUserAndCouponCreator(user);
		//유저쿠폰의 레스토랑 리스트 가저온다
		//List<String> restaurants = userCouponRepository.findRestaurantNamesByUser(userCoupons);
		//쿠폰목록을 반환하기위한 리스트를 생성한다
		List<UserCouponServiceResponse> userCouponServiceResponses = userCouponRepository.findRestaurantNameAndUserAll(
			user);

		List<GetUserCouponResponse> getUserCouponResponses = new ArrayList<>();

		for (UserCouponServiceResponse userCouponServiceResponse : userCouponServiceResponses) {
			GetUserCouponResponse getUserCouponResponse = new GetUserCouponResponse(userCouponServiceResponse);
			getUserCouponResponses.add(getUserCouponResponse);
		}
		return getUserCouponResponses;
		// for (int i = 0; i < userCoupons.size(); i++) {
		// 	UserCouponResponse userCouponResponse = new UserCouponResponse(userCoupons.get(i));
		// 	String restaurantName = restaurants.get(i);
		// 	GetUserCouponResponse getUserCouponResponse = new GetUserCouponResponse(userCouponResponse,
		// 		restaurantName);
		// 	getUserCouponResponses.add(getUserCouponResponse);

		// for (UserCoupon userCoupon : userCoupons) {
		// 	//유저쿠폰을 담기위한 리스폰스를 만든다
		// 	UserCouponResponse userCouponResponse = new UserCouponResponse(userCoupon);
		// 	//유저쿠폰 레포지토리에서 해당유저쿠폰에 해당하는 레스토랑이름을 가저온다.(굳이?)
		// 	String restaurantName = userCouponRepository.findRestaurantNameByUserCoupon(userCoupon);
		// 	//반환하기위한 리스폰스+레스토랑 이름을 사용해 반환리스폰스를 작성한다
		// 	GetUserCouponResponse getUserCouponResponse = new GetUserCouponResponse(userCouponResponse, restaurantName);
		// 	getUserCouponResponses.add(getUserCouponResponse);
		// }

	}

	@Override
	public void useCoupon(Long couponId) {
		UserCoupon userCoupon = userCouponRepository.findByIdAndIsUsedFalse(couponId).orElseThrow(
			() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다.")
		);
		userCoupon.useCoupon();
	}

}
