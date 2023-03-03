package team.waitingcatch.app.event.service.couponcreator;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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

import team.waitingcatch.app.event.dto.couponcreator.CreateAdminCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.CreateSellerCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateAdminCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.UpdateSellerCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.event.GetCouponCreatorResponse;
import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.entity.Event;
import team.waitingcatch.app.event.repository.CouponCreatorRepository;
import team.waitingcatch.app.event.repository.EventRepository;
import team.waitingcatch.app.event.service.event.InternalEventService;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.service.restaurant.InternalRestaurantService;

@SpringBootTest
class CouponCreatorServiceImplTest {
	@InjectMocks
	private CouponCreatorServiceImpl couponCreatorService;

	@Mock
	private CouponCreatorRepository couponCreatorRepository;

	@Mock
	private InternalEventService eventService;

	@Mock
	private EventRepository eventRepository;

	@Mock
	private InternalRestaurantService restaurantService;

	@Test
	void createAdminCouponCreator() {
		//given
		LocalDateTime localTime = LocalDateTime.now();
		//클래스의 객체를 MOCK객체로 생성
		CreateAdminCouponCreatorServiceRequest createAdminCouponCreatorServiceRequest = mock(
			CreateAdminCouponCreatorServiceRequest.class);
		//객체의 getname이 호출될때 song이 반환되도록 설정
		when(createAdminCouponCreatorServiceRequest.getName()).thenReturn("테스트");
		when(createAdminCouponCreatorServiceRequest.getEventId()).thenReturn(1L);

		Event event = mock(Event.class);
		when(event.getId()).thenReturn(1L);
		when(event.getName()).thenReturn("테스트레스토랑");
		when(eventService._getEventById(any(Long.class))).thenReturn(event);

		//when
		couponCreatorService.createAdminCouponCreator(createAdminCouponCreatorServiceRequest);

		//then
		//eventReposiroty.save가 한번 호출되었는지 검증
		verify(couponCreatorRepository, times(1)).save(any(CouponCreator.class));

	}

	@Test
	void getAdminCouponCreator() {
		Long eventId = 1L;
		List<CouponCreator> couponCreators = new ArrayList<>();
		CouponCreator couponCreator = mock(CouponCreator.class);
		when(couponCreator.getId()).thenReturn(1L);
		when(couponCreator.getName()).thenReturn("테스터");
		couponCreators.add(couponCreator);
		when(couponCreatorRepository.findAllByEventId(any(Long.class))).thenReturn(couponCreators);

		List<GetCouponCreatorResponse> list = couponCreatorService.getAdminCouponCreator(eventId);

		assertThat(list).hasSize(1);

	}

	@Test
	void createSellerCouponCreator() {
		CreateSellerCouponCreatorServiceRequest createSellerCouponCreatorServiceRequest = mock(
			CreateSellerCouponCreatorServiceRequest.class);

		when(createSellerCouponCreatorServiceRequest.getEventId()).thenReturn(1L);
		when(createSellerCouponCreatorServiceRequest.getUserId()).thenReturn(1L);

		Restaurant restaurant = mock(Restaurant.class);
		when(restaurant.getName()).thenReturn("TestRes");
		when(restaurant.getId()).thenReturn(1L);
		when(restaurantService._getRestaurantByUserId(any(Long.class))).thenReturn(restaurant);

		Event event = mock(Event.class);
		when(event.getId()).thenReturn(1L);
		when(event.getName()).thenReturn("aa");
		when(eventRepository.findByIdAndRestaurantAndIsDeletedFalse(1L, restaurant)).thenReturn(
			Optional.of(event));

		couponCreatorService.createSellerCouponCreator(createSellerCouponCreatorServiceRequest);

		verify(couponCreatorRepository, times(1)).save(any(CouponCreator.class));

	}

	@Test
	void updateAdminCouponCreator() {
		UpdateAdminCouponCreatorServiceRequest updateAdminCouponCreatorServiceRequest = mock(
			UpdateAdminCouponCreatorServiceRequest.class);

		when(updateAdminCouponCreatorServiceRequest.getEventId()).thenReturn(1L);
		when(updateAdminCouponCreatorServiceRequest.getName()).thenReturn("테스터");

		Event event = mock(Event.class);
		when(event.getId()).thenReturn(1L);
		when(event.getName()).thenReturn("테스터");
		when(eventService._getEventById(any(Long.class))).thenReturn(event);

		CouponCreator couponCreator = mock(CouponCreator.class);
		when(couponCreator.getId()).thenReturn(1L);
		when(couponCreator.getName()).thenReturn("테스터");
		when(couponCreatorRepository.findById(any(Long.class))).thenReturn(Optional.of(couponCreator));

		couponCreatorService.updateAdminCouponCreator(updateAdminCouponCreatorServiceRequest);

		assertEquals("테스터", event.getName());

	}

	@Test
	void updateSellerCouponCreator() {

		UpdateSellerCouponCreatorServiceRequest updateSellerCouponCreatorServiceRequest = mock(
			UpdateSellerCouponCreatorServiceRequest.class);

		when(updateSellerCouponCreatorServiceRequest.getEventId()).thenReturn(1L);
		when(updateSellerCouponCreatorServiceRequest.getName()).thenReturn("테스터");

		Restaurant restaurant = mock(Restaurant.class);
		when(restaurant.getName()).thenReturn("TestRes");
		when(restaurant.getId()).thenReturn(1L);
		when(restaurantService._getRestaurantByUserId(any(Long.class))).thenReturn(restaurant);

		Event event = mock(Event.class);
		when(event.getId()).thenReturn(1L);
		when(event.getName()).thenReturn("테스터");
		when(eventRepository.findByIdAndRestaurantAndIsDeletedFalse(1L, restaurant)).thenReturn(
			Optional.of(event));

		CouponCreator couponCreator = mock(CouponCreator.class);
		when(couponCreator.getId()).thenReturn(1L);
		when(couponCreator.getName()).thenReturn("테스터");
		when(couponCreatorRepository.findById(any(Long.class))).thenReturn(Optional.of(couponCreator));

		couponCreatorService.updateSellerCouponCreator(updateSellerCouponCreatorServiceRequest);

		assertEquals("테스터", event.getName());

	}
	
}