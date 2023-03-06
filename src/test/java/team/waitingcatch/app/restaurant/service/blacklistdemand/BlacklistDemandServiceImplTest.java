package team.waitingcatch.app.restaurant.service.requestblacklist;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import team.waitingcatch.app.restaurant.dto.blacklist.ApproveBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CancelBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CreateBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlackListDemandByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistDemandResponse;
import team.waitingcatch.app.restaurant.entity.BlacklistDemand;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum;
import team.waitingcatch.app.restaurant.repository.BlacklistDemandRepository;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;
import team.waitingcatch.app.restaurant.service.blacklist.InternalBlacklistService;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.repository.UserRepository;

@Nested
@ExtendWith(MockitoExtension.class)
class BlacklistDemandServiceImplTest {
	@Mock
	private BlacklistDemandRepository blacklistDemandRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private RestaurantRepository restaurantRepository;
	@Mock
	private InternalBlacklistService internalBlackListService;
	@InjectMocks
	private BlacklistDemandServiceImpl blacklistDemandService;

	@Test
	@DisplayName("블랙리스트 요청")
	void submitBlacklistDemand() {
		//given
		CreateBlacklistDemandServiceRequest serviceRequest = new CreateBlacklistDemandServiceRequest(1L, 2L, "hiyo");
		User user = mock(User.class);
		List<BlacklistDemand> blacklistDemandList = new ArrayList<>();
		BlacklistDemand blacklistDemand = mock(BlacklistDemand.class);
		blacklistDemandList.add(blacklistDemand);
		Restaurant restaurant = mock(Restaurant.class);
		when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));
		when(blacklistDemandRepository.findByUser_IdAndRestaurant_User_Id(any(Long.class), any(Long.class))).thenReturn(
			blacklistDemandList);
		when(restaurantRepository.findByUserId(any(Long.class))).thenReturn(Optional.ofNullable(restaurant));
		//when
		blacklistDemandService.submitBlacklistDemand(serviceRequest);
		//then
		verify(blacklistDemandRepository, times(1)).save(any(BlacklistDemand.class));
	}

	@Test
	@DisplayName("블랙리스트 요청취소")
	void cancelBlacklistDemand() {
		CancelBlacklistDemandServiceRequest request = new CancelBlacklistDemandServiceRequest(1L, 2L);
		Restaurant restaurant = mock(Restaurant.class);
		User user = mock(User.class);

		BlacklistDemand blacklistDemand = new BlacklistDemand(restaurant, user, "desc");
		when(blacklistDemandRepository.findById(any(Long.class))).thenReturn(Optional.of(blacklistDemand));

		//when
		blacklistDemandService.cancelBlacklistDemand(request);
		//then
		verify(blacklistDemandRepository, times(1)).findById(any(Long.class));
	}

	@Test
	@DisplayName("블랙리스트 요청 조회")
	void getBlacklistDemands() {
		Pageable pageable = mock(Pageable.class);
		User user = mock(User.class);
		Restaurant restaurant = mock(Restaurant.class);
		AcceptedStatusEnum statusEnum = mock(AcceptedStatusEnum.class);

		List<BlacklistDemand> blacklistDemandList = new ArrayList<>();
		BlacklistDemand blacklistDemand = mock(BlacklistDemand.class);
		BlacklistDemand blacklistDemand1 = mock(BlacklistDemand.class);

		when(blacklistDemand.getUser()).thenReturn(user);
		when(blacklistDemand1.getUser()).thenReturn(user);
		when(blacklistDemand.getRestaurant()).thenReturn(restaurant);
		when(blacklistDemand1.getRestaurant()).thenReturn(restaurant);
		when(blacklistDemand.getRestaurant().getUser()).thenReturn(user);
		when(blacklistDemand1.getRestaurant().getUser()).thenReturn(user);
		when(blacklistDemand.getStatus()).thenReturn(statusEnum);
		when(blacklistDemand1.getStatus()).thenReturn(statusEnum);

		blacklistDemandList.add(blacklistDemand);
		blacklistDemandList.add(blacklistDemand1);
		Page<BlacklistDemand> blacklistDemands = new PageImpl<>(blacklistDemandList);

		when(blacklistDemandRepository.findAllByStatus(AcceptedStatusEnum.WAIT, pageable)).thenReturn(
			blacklistDemands);

		//when
		Page<GetBlacklistDemandResponse> a = blacklistDemandService.getBlacklistDemands(pageable);
		//then
		assertEquals(1, a.getTotalPages());

	}

	@Test
	@DisplayName("블랙리스트 요청 승인")
	void approveBlacklistDemand() {
		ApproveBlacklistDemandServiceRequest request = mock(ApproveBlacklistDemandServiceRequest.class);
		Restaurant restaurant = mock(Restaurant.class);
		User user = mock(User.class);
		BlacklistDemand blacklistDemand = new BlacklistDemand(restaurant, user, "hiyo");
		when(blacklistDemandRepository.findById(any(Long.class))).thenReturn(Optional.of(blacklistDemand));
		//when
		blacklistDemandService.approveBlacklistDemand(request);
		//then
		verify(blacklistDemandRepository, times(1)).findById(any(Long.class));
		assertEquals(AcceptedStatusEnum.APPROVE, blacklistDemand.getStatus());

	}

	@Test
	@DisplayName("블랙리스트 요청 거절")
	void rejectBlacklistDemand() {
		Restaurant restaurant = mock(Restaurant.class);
		User user = mock(User.class);
		BlacklistDemand blacklistDemand = new BlacklistDemand(restaurant, user, "hiyo");
		when(blacklistDemandRepository.findById(any(Long.class))).thenReturn(
			Optional.ofNullable(blacklistDemand));
		//when
		blacklistDemandService.rejectBlacklistDemand(any(Long.class));
		//then
		verify(blacklistDemandRepository, times(1)).findById(any(Long.class));
		assertEquals(AcceptedStatusEnum.REJECT, blacklistDemand.getStatus());
	}

	@Test
	@DisplayName("각 레스토랑별 블랙리스트요청 조회")
	void getBlackListDemandsByRestaurant() {
		GetBlackListDemandByRestaurantServiceRequest request = new GetBlackListDemandByRestaurantServiceRequest(1L);
		Restaurant restaurant = mock(Restaurant.class);
		User user = mock(User.class);
		List<BlacklistDemand> blacklistDemandList = new ArrayList<>();
		BlacklistDemand blacklistDemand = new BlacklistDemand(restaurant, user, "hiyo");
		blacklistDemandList.add(blacklistDemand);
		when(restaurant.getUser()).thenReturn(user);

		when(restaurantRepository.findByUserId(any(Long.class))).thenReturn(Optional.of(restaurant));
		when(blacklistDemandRepository.findAllByRestaurant_Id(any(Long.class))).thenReturn((blacklistDemandList));

		//when
		List<GetBlacklistDemandResponse> a = blacklistDemandService.getBlackListDemandsByRestaurant(request);
		//then
		verify(blacklistDemandRepository, times(1)).findAllByRestaurant_Id(any(Long.class));
		assertEquals(a.get(0).getDescription(), blacklistDemand.getDescription());

	}
}