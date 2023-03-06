package team.waitingcatch.app.event.service.usercoupon;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import team.waitingcatch.app.event.dto.usercoupon.CreateUserCouponServiceRequest;
import team.waitingcatch.app.event.dto.usercoupon.GetUserCouponResponse;
import team.waitingcatch.app.event.dto.usercoupon.UserCouponResponse;
import team.waitingcatch.app.event.dto.usercoupon.UserCouponServiceResponse;
import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.entity.Event;
import team.waitingcatch.app.event.entity.UserCoupon;
import team.waitingcatch.app.event.repository.CouponCreatorRepository;
import team.waitingcatch.app.event.repository.UserCouponRepository;
import team.waitingcatch.app.event.service.couponcreator.InternalCouponCreatorService;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.user.entitiy.User;
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
	private InternalCouponCreatorService couponCreatorService;

	@Test
	void createUserCoupon() {
		CreateUserCouponServiceRequest createUserCouponServiceRequest = mock(CreateUserCouponServiceRequest.class);
		when(createUserCouponServiceRequest.getCreatorId()).thenReturn(1L);
		when(createUserCouponServiceRequest.getUsername()).thenReturn("테스트");

		CouponCreator couponCreator = mock(CouponCreator.class);
		when(couponCreator.getId()).thenReturn(1L);
		when(couponCreator.getName()).thenReturn("테스트");
		when(couponCreator.getQuantity()).thenReturn(300);
		when(couponCreator.hasCouponBalance()).thenReturn(true);
		when(couponCreatorService._getCouponCreatorById(any(Long.class))).thenReturn(couponCreator);

		userCouponService.createUserCoupon(createUserCouponServiceRequest);

		verify(userCouponRepository, times(1)).save(any(UserCoupon.class));

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