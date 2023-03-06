package team.waitingcatch.app.event.service.event;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import team.waitingcatch.app.event.dto.event.CreateEventControllerRequest;
import team.waitingcatch.app.event.dto.event.CreateEventServiceRequest;
import team.waitingcatch.app.event.dto.event.DeleteEventControllerRequest;
import team.waitingcatch.app.event.dto.event.DeleteEventServiceRequest;
import team.waitingcatch.app.event.dto.event.GetEventsResponse;
import team.waitingcatch.app.event.dto.event.UpdateEventServiceRequest;
import team.waitingcatch.app.event.dto.event.UpdateSellerEventServiceRequest;
import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.entity.Event;
import team.waitingcatch.app.event.repository.CouponCreatorRepository;
import team.waitingcatch.app.event.repository.EventRepository;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.service.restaurant.InternalRestaurantService;

@SpringBootTest
class EventServiceImplTest {
	@Mock
	private EventRepository eventRepository;

	@Mock
	private CouponCreatorRepository couponCreatorRepository;

	@InjectMocks
	private EventServiceImpl eventService;

	@Mock
	private InternalRestaurantService restaurantService;

	@Test
	void createAdminEvent() {

		//given
		LocalDateTime localTime = LocalDateTime.now();
		//클래스의 객체를 MOCK객체로 생성
		CreateEventControllerRequest createEventControllerRequest = mock(CreateEventControllerRequest.class);
		//객체의 getname이 호출될때 song이 반환되도록 설정
		when(createEventControllerRequest.getName()).thenReturn("song");
		when(createEventControllerRequest.getEventStartDate()).thenReturn(localTime.plus(1, ChronoUnit.DAYS));
		when(createEventControllerRequest.getEventEndDate()).thenReturn(localTime.plus(2, ChronoUnit.DAYS));

		//when
		eventService.createAdminEvent(createEventControllerRequest);

		//then
		//eventReposiroty.save가 한번 호출되었는지 검증
		verify(eventRepository, times(1)).save(any(Event.class));
	}

	@Test
	void createSellerEvent() {
		//given
		LocalDateTime localTime = LocalDateTime.now();
		//클래스의 객체를 MOCK객체로 생성
		CreateEventServiceRequest createEventServiceRequest = mock(CreateEventServiceRequest.class);
		Restaurant restaurant = mock(Restaurant.class);
		//객체의 getname이 호출될때 song이 반환되도록 설정
		when(restaurant.getName()).thenReturn("TestRes");
		when(restaurant.getId()).thenReturn(1L);
		when(createEventServiceRequest.getName()).thenReturn("song");
		when(createEventServiceRequest.getEventStartDate()).thenReturn(localTime.plus(1, ChronoUnit.DAYS));
		when(createEventServiceRequest.getEventEndDate()).thenReturn(localTime.plus(2, ChronoUnit.DAYS));
		when(createEventServiceRequest.getId()).thenReturn(1L);
		when(restaurantService._getRestaurantByUserId(any(Long.class))).thenReturn(restaurant);

		//when
		eventService.createSellerEvent(createEventServiceRequest);

		//then
		//eventReposiroty.save가 한번 호출되었는지 검증
		verify(eventRepository, times(1)).save(any(Event.class));

	}

	@Test
	void updateAdminEvent() {
		//given
		LocalDateTime localTime = LocalDateTime.now();
		UpdateEventServiceRequest updateEventServiceRequest = mock(UpdateEventServiceRequest.class);
		Event event = mock(Event.class);
		when(updateEventServiceRequest.getEventId()).thenReturn(1L);
		when(updateEventServiceRequest.getName()).thenReturn("aa");
		when(updateEventServiceRequest.getEventStartDate()).thenReturn(localTime.plus(1, ChronoUnit.DAYS));
		when(updateEventServiceRequest.getEventEndDate()).thenReturn(localTime.plus(2, ChronoUnit.DAYS));
		when(event.getId()).thenReturn(1L);
		when(event.getName()).thenReturn("aa");
		when(event.getEventStartDate()).thenReturn(localTime.plus(3, ChronoUnit.DAYS));
		when(event.getEventEndDate()).thenReturn(localTime.plus(4, ChronoUnit.DAYS));
		when(eventRepository.findByIdAndIsDeletedFalse(any(Long.class))).thenReturn(Optional.of(event));

		//when

		eventService.updateAdminEvent(updateEventServiceRequest);

		//then
		assertEquals("aa", event.getName());
	}

	@Test
	void updateSellerEvent() {
		//given
		Restaurant restaurant = mock(Restaurant.class);
		LocalDateTime localTime = LocalDateTime.now();
		UpdateSellerEventServiceRequest updateSellerEventServiceRequest = mock(UpdateSellerEventServiceRequest.class);
		Event event = mock(Event.class);
		when(restaurant.getName()).thenReturn("TestRes");
		when(restaurant.getId()).thenReturn(1L);
		when(updateSellerEventServiceRequest.getEventId()).thenReturn(1L);
		when(updateSellerEventServiceRequest.getName()).thenReturn("aa");
		when(updateSellerEventServiceRequest.getEventStartDate()).thenReturn(localTime.plus(1, ChronoUnit.DAYS));
		when(updateSellerEventServiceRequest.getEventEndDate()).thenReturn(localTime.plus(2, ChronoUnit.DAYS));
		when(event.getId()).thenReturn(1L);
		when(event.getName()).thenReturn("aa");
		when(event.getEventStartDate()).thenReturn(localTime.plus(3, ChronoUnit.DAYS));
		when(event.getEventEndDate()).thenReturn(localTime.plus(4, ChronoUnit.DAYS));
		when(restaurantService._getRestaurantByUserId(any(Long.class))).thenReturn(restaurant);
		when(eventRepository.findByIdAndIsDeletedFalse(any(Long.class))).thenReturn(Optional.of(event));
		when(eventRepository.findByIdAndRestaurantAndIsDeletedFalse(1L, restaurant)).thenReturn(
			Optional.of(event));

		//when

		eventService.updateSellerEvent(updateSellerEventServiceRequest);

		//then
		assertEquals("aa", event.getName());
	}

	@Test
	void deleteAdminEvent() {
		DeleteEventControllerRequest deleteEventControllerRequest = mock(DeleteEventControllerRequest.class);
		Event event = mock(Event.class);
		when(deleteEventControllerRequest.getEventId()).thenReturn(1L);
		when(event.getId()).thenReturn(1L);
		when(event.getName()).thenReturn("aa");
		when(eventRepository.findByIdAndIsDeletedFalse(any(Long.class))).thenReturn(Optional.of(event));

		eventService.deleteAdminEvent(deleteEventControllerRequest);

		verify(event).deleteEvent();
	}

	@Test
	void deleteSellerEvent() {
		Restaurant restaurant = mock(Restaurant.class);
		when(restaurant.getName()).thenReturn("TestRes");
		when(restaurant.getId()).thenReturn(1L);
		when(restaurantService._getRestaurantByUserId(any(Long.class))).thenReturn(restaurant);

		DeleteEventServiceRequest deleteEventControllerRequest = mock(DeleteEventServiceRequest.class);
		Event event = mock(Event.class);
		when(deleteEventControllerRequest.getEventId()).thenReturn(1L);
		when(event.getId()).thenReturn(1L);
		when(event.getName()).thenReturn("aa");
		when(eventRepository.findByIdAndIsDeletedFalse(any(Long.class))).thenReturn(Optional.of(event));
		when(eventRepository.findByIdAndRestaurantAndIsDeletedFalse(1L, restaurant)).thenReturn(
			Optional.of(event));
		eventService.deleteSellerEvent(deleteEventControllerRequest);

		verify(event).deleteEvent();
	}

	@Test
	void getGlobalEvents() {
		List<Event> events = new ArrayList<>();
		Event event = mock(Event.class);
		events.add(event);
		when(eventRepository.findByRestaurantIsNullAndIsDeletedFalse()).thenReturn(events);
		CouponCreator couponCreator = mock(CouponCreator.class);
		when(couponCreator.getId()).thenReturn(1L);
		when(couponCreator.getName()).thenReturn("테스트생성자");
		List<CouponCreator> couponCreators = new ArrayList<>();
		couponCreators.add(couponCreator);
		when(couponCreatorRepository.findByEventAndIsDeletedFalse(any(Event.class))).thenReturn(couponCreators);
		when(eventRepository.findByIdAndIsDeletedFalse(any(Long.class))).thenReturn(Optional.of(event));

		List<GetEventsResponse> list = eventService.getGlobalEvents();

		// then
		assertThat(list).hasSize(1);
	}

	@Test
	void getRestaurantEvents() {
		Restaurant restaurant = mock(Restaurant.class);
		when(restaurant.getName()).thenReturn("TestRes");
		when(restaurant.getId()).thenReturn(1L);
		when(restaurantService._getRestaurantByUserId(any(Long.class))).thenReturn(restaurant);

		List<Event> events = new ArrayList<>();
		List<CouponCreator> couponCreators = new ArrayList<>();

		Event event = mock(Event.class);
		events.add(event);
		when(eventRepository.findByRestaurantIsNullAndIsDeletedFalse()).thenReturn(events);
		CouponCreator couponCreator = mock(CouponCreator.class);
		when(couponCreator.getId()).thenReturn(1L);
		when(couponCreator.getName()).thenReturn("테스트생성자");

		couponCreators.add(couponCreator);
		when(couponCreatorRepository.findByEventWithEvent(any(Event.class))).thenReturn(couponCreators);
		when(eventRepository.findByIdAndIsDeletedFalse(any(Long.class))).thenReturn(Optional.of(event));

		List<GetEventsResponse> list = eventService.getGlobalEvents();

		// then
		assertThat(list).hasSize(1);
	}

}