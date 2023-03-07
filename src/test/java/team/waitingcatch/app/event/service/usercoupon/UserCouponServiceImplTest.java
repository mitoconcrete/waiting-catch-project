package team.waitingcatch.app.event.service.usercoupon;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import team.waitingcatch.app.event.dto.couponcreator.CreateAdminCouponCreatorRequest;
import team.waitingcatch.app.event.dto.couponcreator.CreateAdminCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.CreateCouponCreatorControllerRequest;
import team.waitingcatch.app.event.dto.usercoupon.CreateUserCouponServiceRequest;
import team.waitingcatch.app.event.dto.usercoupon.GetUserCouponResponse;
import team.waitingcatch.app.event.dto.usercoupon.UserCouponResponse;
import team.waitingcatch.app.event.dto.usercoupon.UserCouponServiceResponse;
import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.entity.Event;
import team.waitingcatch.app.event.entity.UserCoupon;
import team.waitingcatch.app.event.enums.CouponTypeEnum;
import team.waitingcatch.app.event.repository.CouponCreatorRepository;
import team.waitingcatch.app.event.repository.UserCouponRepository;
import team.waitingcatch.app.event.service.couponcreator.InternalCouponCreatorService;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.repository.UserRepository;
import team.waitingcatch.app.user.service.InternalUserService;

@SpringBootTest
class UserCouponServiceImplTest {

	@InjectMocks
	private UserCouponServiceImpl userCouponService;

	@Mock
	private UserCouponRepository userCouponRepository;

	@Mock
	private CouponCreatorRepository couponCreatorRepository;
	@Mock
	private InternalUserService userService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private InternalCouponCreatorService couponCreatorService;

	@Test
	public void createUserCoupon() {

		// Given
		Long id = 1L;
		Event event = mock(Event.class);
		String name = "aa";
		int discountPrice = 1000;
		CouponTypeEnum discountType = CouponTypeEnum.PRICE;
		int quantity = 10;
		LocalDateTime expireDate = LocalDateTime.now().plusDays(1);
		boolean isDeleted = false;
		CreateCouponCreatorControllerRequest request2 = new CreateCouponCreatorControllerRequest();
		request2.setName(name);
		request2.setDiscountPrice(discountPrice);
		request2.setDiscountType(discountType);
		request2.setQuantity(quantity);
		request2.setExpireDate(expireDate);
		CreateAdminCouponCreatorServiceRequest request1 = new CreateAdminCouponCreatorServiceRequest(request2, 1L);
		CreateAdminCouponCreatorRequest request = new CreateAdminCouponCreatorRequest(request1, event);

		CouponCreator couponCreator1 = mock(CouponCreator.class);
		CouponCreator couponCreator = mock(CouponCreator.class, withSettings().useConstructor(request));
		when(couponCreator.getId()).thenReturn(1L);
		User user = mock(User.class);
		UserCoupon userCoupon = mock(UserCoupon.class);
		when(userCoupon.getId()).thenReturn(1L);

		when(couponCreatorRepository.findById(any(Long.class))).thenReturn(Optional.of(couponCreator));
		when(couponCreatorService._getCouponCreatorById(any(Long.class))).thenReturn(couponCreator);

		when(userService._getUserByUsername(name)).thenReturn(user);
		when(userCouponRepository.findUserCouponWithRelations(user, couponCreator1)).thenReturn(
			Optional.of(userCoupon));
		when(couponCreatorRepository.getHasCouponBalance(any(Long.class))).thenReturn(3);
		CreateUserCouponServiceRequest request3 = new CreateUserCouponServiceRequest(userCoupon.getId(), "aa");

		// When
		userCouponService.createUserCoupon(request3);

		verify(couponCreatorRepository, times(1)).save(any(CouponCreator.class));

	}

	@Test
	void getUserCoupons() {
		CouponCreator couponCreator = mock(CouponCreator.class);
		Event event = mock(Event.class);
		Restaurant restaurant = mock(Restaurant.class);

		when(event.getRestaurant()).thenReturn(restaurant);
		when(couponCreator.getEvent()).thenReturn(event);

		User user = mock(User.class);
		when(user.getId()).thenReturn(1L);
		when(user.getUsername()).thenReturn("테스터");
		when(userService._getUserByUsername(any(String.class))).thenReturn(user);

		List<UserCouponServiceResponse> userCouponServiceResponses = new ArrayList<>();
		UserCouponServiceResponse userCouponServiceResponse = mock(UserCouponServiceResponse.class);
		UserCouponResponse userCouponResponse = mock(UserCouponResponse.class);

		UserCoupon userCoupon = mock(UserCoupon.class);

		CouponCreator couponCreator1 = mock(CouponCreator.class);
		when(userCouponServiceResponse.getRestaurantName()).thenReturn("레스토랑테스트");
		when(userCouponResponse.getCouponCreator()).thenReturn(couponCreator1);
		when(couponCreator1.getId()).thenReturn(1L);
		when(couponCreator1.getName()).thenReturn("테스트");
		when(userCouponResponse.getUserCoupon()).thenReturn(userCoupon);
		when(userCoupon.getCouponCreator()).thenReturn(couponCreator);
		userCouponServiceResponses.add(userCouponServiceResponse);

		when(userCouponRepository.findRestaurantNameAndUserAll(user)).thenReturn(userCouponServiceResponses);

		List<GetUserCouponResponse> list = userCouponService.getUserCoupons(user);

		assertThat(list).hasSize(1);

	}

}