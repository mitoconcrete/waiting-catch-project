package team.waitingcatch.app.restaurant.service.blacklist;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import team.waitingcatch.app.restaurant.dto.blacklist.CreateBlacklistInternalServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.DeleteBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlackListByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistByRestaurantIdServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistResponse;
import team.waitingcatch.app.restaurant.entity.Blacklist;
import team.waitingcatch.app.restaurant.entity.BlacklistDemand;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum;
import team.waitingcatch.app.restaurant.repository.BlacklistDemandRepository;
import team.waitingcatch.app.restaurant.repository.BlacklistRepository;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;
import team.waitingcatch.app.restaurant.service.restaurant.InternalRestaurantService;
import team.waitingcatch.app.user.entitiy.User;

@ExtendWith(MockitoExtension.class)
class BlacklistServiceImplTest {

	@Mock
	private BlacklistRepository blacklistRepository;
	@Mock
	private InternalRestaurantService internalRestaurantService;
	@Mock
	private RestaurantRepository restaurantRepository;
	@Mock
	private BlacklistDemandRepository blacklistDemandRepository;
	@InjectMocks
	private BlacklistServiceImpl blacklistService;

	@Test
	@DisplayName("Delete Blacklist")
	void deleteBlacklistByRestaurant() {
		DeleteBlacklistByRestaurantServiceRequest serviceRequest = mock(
			DeleteBlacklistByRestaurantServiceRequest.class);
		Restaurant restaurant = mock(Restaurant.class);
		User user = mock(User.class);
		CreateBlacklistInternalServiceRequest serviceRequest1 = new CreateBlacklistInternalServiceRequest(restaurant,
			user);
		Blacklist blacklist = new Blacklist(serviceRequest1);
		BlacklistDemand blacklistDemand = new BlacklistDemand(restaurant, user, "hiyo");
		when(restaurant.getUser()).thenReturn(user);
		when(blacklistRepository.findByIdAndRestaurantUserId(any(Long.class), any(Long.class))).thenReturn(
			Optional.of(blacklist));
		when(blacklistDemandRepository.findByUser_IdAndRestaurant_User_IdAndStatusApproval(any(Long.class),
			any(Long.class))).thenReturn(
			Optional.of(blacklistDemand));

		//when
		blacklistService.deleteBlacklistByRestaurant(serviceRequest);
		//then
		assertTrue(blacklist.isDeleted());
		assertEquals(blacklistDemand.getStatus(), AcceptedStatusEnum.CANCEL);
	}

	@Test
	@DisplayName("Get Blacklists By Restaurant Id")
	void getBlackListByRestaurantId() {
		GetBlacklistByRestaurantIdServiceRequest serviceRequest = mock(GetBlacklistByRestaurantIdServiceRequest.class);
		User user = mock(User.class);
		Restaurant restaurant = mock(Restaurant.class);
		CreateBlacklistInternalServiceRequest serviceRequest1 = new CreateBlacklistInternalServiceRequest(restaurant,
			user);
		List<Blacklist> blacklistList = new ArrayList<>();
		Blacklist blacklist = new Blacklist(serviceRequest1);
		blacklistList.add(blacklist);
		when(restaurant.getName()).thenReturn("korean");
		when(blacklistRepository.findAllByRestaurant(any())).thenReturn(blacklistList);
		//when
		List<GetBlacklistResponse> blacklistResponses = blacklistService.getBlackListByRestaurantId(serviceRequest);
		//then
		assertEquals("korean", blacklistResponses.get(0).getRestaurantName());

	}

	@Test
	@DisplayName("Get a blacklist")
	void getBlacklist() {
		Pageable pageable = mock(Pageable.class);

		List<Blacklist> blacklistList = new ArrayList<>();
		Page<Blacklist> pagedResponse = new PageImpl<>(blacklistList);

		Restaurant restaurant = mock(Restaurant.class);
		User user = mock(User.class);
		CreateBlacklistInternalServiceRequest serviceRequest = new CreateBlacklistInternalServiceRequest(restaurant,
			user);

		when(blacklistRepository.findAllByIsDeletedFalse(pageable)).thenReturn(pagedResponse);
		//when
		Page<GetBlacklistResponse> blacklistResponses = blacklistService.getBlacklist(pageable);
		//then
		assertEquals(1, blacklistResponses.getTotalPages());

	}

	@Test
	@DisplayName("Get blacklists by restaurant")
	void getBlackListByRestaurant() {
		GetBlackListByRestaurantServiceRequest request = mock(
			GetBlackListByRestaurantServiceRequest.class);
		Restaurant restaurant = mock(Restaurant.class);
		User user = mock(User.class);
		CreateBlacklistInternalServiceRequest serviceRequest = new CreateBlacklistInternalServiceRequest(restaurant,
			user);

		List<Blacklist> blacklistList = new ArrayList<>();
		Blacklist blacklist = new Blacklist(serviceRequest);
		blacklistList.add(blacklist);

		when(blacklist.getRestaurant().getName()).thenReturn("korean");
		when(restaurantRepository.findById(anyLong())).thenReturn(Optional.ofNullable(restaurant));
		when(blacklistRepository.findAllByRestaurant_Id(anyLong())).thenReturn(blacklistList);
		//when
		List<GetBlacklistResponse> blacklistResponses = blacklistService.getBlackListByRestaurant(request);
		//then
		assertEquals("korean", blacklistResponses.get(0).getRestaurantName());
	}

	@Test
	@DisplayName("_exist By restaurantId and User Id")
	void _existsByRestaurantIdAndUserId() {
		Blacklist blacklist = mock(Blacklist.class);
		when(blacklistRepository.findByRestaurantIdAndUserIdAndIsDeletedFalse(anyLong(), anyLong())).thenReturn(
			Optional.ofNullable(blacklist));
		//when
		Boolean aBoolean = blacklistService._existsByRestaurantIdAndUserId(anyLong(), anyLong());
		//then
		assertEquals(true, aBoolean);
	}

	@Test
	@DisplayName("_createBlackList()")
	void _createBlackList() {
		Restaurant restaurant = mock(Restaurant.class);
		User user = mock(User.class);
		Blacklist blacklist = mock(Blacklist.class);

		when(restaurant.getUser()).thenReturn(user);
		when(blacklistRepository.findByUserIdAndRestaurantUserIdAndIsDeletedFalse(anyLong(), anyLong())).thenReturn(
			Optional.ofNullable(null));

		//when
		blacklistService._createBlackList(restaurant, user);
		//then
		verify(blacklistRepository, times(1)).save(any(Blacklist.class));
	}

}