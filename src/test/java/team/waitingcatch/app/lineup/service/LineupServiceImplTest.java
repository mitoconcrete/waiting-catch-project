package team.waitingcatch.app.lineup.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static team.waitingcatch.app.exception.ErrorCode.*;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;

import team.waitingcatch.app.common.util.DistanceCalculator;
import team.waitingcatch.app.common.util.sms.SmsService;
import team.waitingcatch.app.common.util.sms.dto.MessageRequest;
import team.waitingcatch.app.exception.IllegalRequestException;
import team.waitingcatch.app.lineup.dto.CallCustomerInfoResponse;
import team.waitingcatch.app.lineup.dto.CancelWaitingRequest;
import team.waitingcatch.app.lineup.dto.GetLineupHistoryRecordsServiceRequest;
import team.waitingcatch.app.lineup.dto.GetLineupRecordsServiceRequest;
import team.waitingcatch.app.lineup.dto.LineupRecordResponse;
import team.waitingcatch.app.lineup.dto.LineupRecordWithTypeResponse;
import team.waitingcatch.app.lineup.dto.StartWaitingServiceRequest;
import team.waitingcatch.app.lineup.dto.TodayLineupResponse;
import team.waitingcatch.app.lineup.dto.UpdateArrivalStatusServiceRequest;
import team.waitingcatch.app.lineup.entity.Lineup;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;
import team.waitingcatch.app.lineup.repository.LineupRepository;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.entity.RestaurantInfo;
import team.waitingcatch.app.restaurant.service.blacklist.InternalBlacklistService;
import team.waitingcatch.app.restaurant.service.restaurant.InternalRestaurantService;
import team.waitingcatch.app.user.entitiy.User;

@ExtendWith(MockitoExtension.class)
class LineupServiceImplTest {
	@InjectMocks
	LineupServiceImpl lineupServiceImpl;

	@Mock
	LineupRepository lineupRepository;

	@Mock
	InternalRestaurantService internalRestaurantService;

	@Mock
	InternalLineupHistoryService internalLineupHistoryService;

	@Mock
	InternalWaitingNumberService internalWaitingNumberService;

	@Mock
	InternalBlacklistService internalBlacklistService;

	@Mock
	DistanceCalculator distanceCalculator;

	@Mock
	SmsService smsService;

	@Test
	@DisplayName("줄서기 오픈")
	void openLineup(@Mock Restaurant restaurant) {
		when(internalRestaurantService._getRestaurantByUserId(anyLong())).thenReturn(restaurant);
		when(restaurant.getId()).thenReturn(1L);

		lineupServiceImpl.openLineup(1L);

		verify(internalRestaurantService)._openLineup(1L);
	}

	@Test
	@DisplayName("줄서기 마감")
	void closeLineup(@Mock Restaurant restaurant) {
		when(internalRestaurantService._getRestaurantByUserId(anyLong())).thenReturn(restaurant);
		when(restaurant.getId()).thenReturn(1L);

		lineupServiceImpl.closeLineup(1L);

		verify(internalRestaurantService)._closeLineup(1L);
	}

	@Test
	@DisplayName("줄서기 - 줄서기 마감")
	void startWaiting_closedLineup(@Mock StartWaitingServiceRequest serviceRequest) {
		Restaurant restaurant = mock(Restaurant.class);
		RestaurantInfo restaurantInfo = mock(RestaurantInfo.class);

		stubIsLineupActive(serviceRequest, restaurantInfo, restaurant, false);

		assertThatThrownBy(() -> lineupServiceImpl.startWaiting(serviceRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(CLOSED_LINEUP.getMessage());
	}

	@Test
	@DisplayName("줄서기 - 블랙리스트")
	void startWaiting_blacklisted(@Mock StartWaitingServiceRequest serviceRequest) {
		Restaurant restaurant = mock(Restaurant.class);
		RestaurantInfo restaurantInfo = mock(RestaurantInfo.class);

		stubIsLineupActive(serviceRequest, restaurantInfo, restaurant, true);

		User user = mock(User.class);

		stubUser(serviceRequest, user, true);

		assertThatThrownBy(() -> lineupServiceImpl.startWaiting(serviceRequest))
			.isInstanceOf(IllegalRequestException.class)
			.hasMessage(ILLEGAL_ACCESS.getMessage());
	}

	@Test
	@DisplayName("줄서기 - 동시 줄서기 횟수 초과")
	void startWaiting_exceedMaxLineupCount(@Mock StartWaitingServiceRequest serviceRequest) {
		Restaurant restaurant = mock(Restaurant.class);
		RestaurantInfo restaurantInfo = mock(RestaurantInfo.class);

		stubIsLineupActive(serviceRequest, restaurantInfo, restaurant, true);

		User user = mock(User.class);

		stubUser(serviceRequest, user, false);

		Lineup lineup = mock(Lineup.class);
		List<Lineup> lineups = List.of(lineup, lineup, lineup);
		when(lineupRepository.findAllByUserIdAndStatuses(1L,
			List.of(ArrivalStatusEnum.WAIT, ArrivalStatusEnum.CALL))).thenReturn(lineups);

		assertThatThrownBy(() -> lineupServiceImpl.startWaiting(serviceRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(EXCEED_MAX_LINEUP_COUNT.getMessage());
	}

	@Test
	@DisplayName("줄서기 - 최대 반경 초과")
	void startWaiting_exceedMaxDistance(@Mock StartWaitingServiceRequest serviceRequest) {
		Restaurant restaurant = mock(Restaurant.class);
		RestaurantInfo restaurantInfo = mock(RestaurantInfo.class);

		stubIsLineupActive(serviceRequest, restaurantInfo, restaurant, true);

		User user = mock(User.class);

		stubUser(serviceRequest, user, false);

		Lineup lineup = mock(Lineup.class);
		List<Lineup> lineups = List.of(lineup, lineup);

		stubDistance(serviceRequest, restaurant, lineups, 5.0);

		assertThatThrownBy(() -> lineupServiceImpl.startWaiting(serviceRequest))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage(DISTANCE_EXCEEDED.getMessage());
	}

	@Test
	@DisplayName("줄서기 - 정상 동작")
	void startWaiting(@Mock StartWaitingServiceRequest serviceRequest) {
		Restaurant restaurant = mock(Restaurant.class);
		RestaurantInfo restaurantInfo = mock(RestaurantInfo.class);

		stubIsLineupActive(serviceRequest, restaurantInfo, restaurant, true);

		User user = mock(User.class);

		stubUser(serviceRequest, user, false);

		Lineup lineup = mock(Lineup.class);
		List<Lineup> lineups = List.of(lineup, lineup);

		stubDistance(serviceRequest, restaurant, lineups, 2.0);

		when(internalWaitingNumberService.getWaitingNumber(1L)).thenReturn(1);

		lineupServiceImpl.startWaiting(serviceRequest);

		verify(lineupRepository).save(any(Lineup.class));
		verify(restaurantInfo).addLineupCount();
	}

	private void stubIsLineupActive(StartWaitingServiceRequest serviceRequest, RestaurantInfo restaurantInfo,
		Restaurant restaurant, boolean isLineupActive) {
		when(serviceRequest.getRestaurantId()).thenReturn(1L);
		when(internalRestaurantService._getRestaurantInfoWithRestaurantByRestaurantId(anyLong())).thenReturn(
			restaurantInfo);
		when(restaurantInfo.getRestaurant()).thenReturn(restaurant);
		when(restaurantInfo.isLineupActive()).thenReturn(isLineupActive);
	}

	private void stubUser(StartWaitingServiceRequest serviceRequest, User user, boolean isExist) {
		when(serviceRequest.getUser()).thenReturn(user);
		when(user.getId()).thenReturn(1L);
		when(internalBlacklistService._existsByRestaurantIdAndUserId(1L, 1L)).thenReturn(isExist);
	}

	private void stubDistance(StartWaitingServiceRequest serviceRequest, Restaurant restaurant, List<Lineup> lineups,
		double distance) {

		when(lineupRepository.findAllByUserIdAndStatuses(1L,
			List.of(ArrivalStatusEnum.WAIT, ArrivalStatusEnum.CALL))).thenReturn(lineups);

		when(serviceRequest.getLatitude()).thenReturn(0.0);
		when(serviceRequest.getLongitude()).thenReturn(0.0);
		when(restaurant.getLongitude()).thenReturn(0.0);
		when(restaurant.getLongitude()).thenReturn(0.0);

		when(distanceCalculator.distanceInKilometerByHaversine(0.0, 0.0, 0.0, 0.0)).thenReturn(distance);
	}

	@Test
	@DisplayName("줄서기 취소 - 다른 유저의 줄서기 취소 시도")
	void cancelWaiting_isNotSameUser(@Mock CancelWaitingRequest request) {
		when(request.getLineupId()).thenReturn(1L);

		Lineup lineup = mock(Lineup.class);

		stub_getByIdWithUser(lineup);

		when(request.getUserId()).thenReturn(1L);
		when(lineup.isSameUserId(1L)).thenReturn(false);

		assertThatThrownBy(() -> lineupServiceImpl.cancelWaiting(request))
			.isInstanceOf(IllegalRequestException.class)
			.hasMessage(ILLEGAL_ACCESS.getMessage());
	}

	@Test
	@DisplayName("줄서기 취소 - 존재하지 않는 줄서기 취소 시도")
	void cancelWaiting_notFoundLineup(@Mock CancelWaitingRequest request) {
		when(request.getLineupId()).thenReturn(1L);

		Lineup lineup = mock(Lineup.class);

		stub_getByIdWithUser(lineup);

		when(request.getUserId()).thenReturn(1L);
		when(lineup.isSameUserId(1L)).thenReturn(true);
		when(lineupRepository.findRestaurantIdById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> lineupServiceImpl.cancelWaiting(request))
			.isInstanceOf(NoSuchElementException.class)
			.hasMessage(NOT_FOUND_LINEUP.getMessage());
	}

	@Test
	@DisplayName("줄서기 취소 - 정상 동작")
	void cancelWaiting(@Mock CancelWaitingRequest request) {
		when(request.getLineupId()).thenReturn(1L);

		Lineup lineup = mock(Lineup.class);

		stub_getByIdWithUser(lineup);

		when(request.getUserId()).thenReturn(1L);
		when(lineup.isSameUserId(1L)).thenReturn(true);
		when(lineupRepository.findRestaurantIdById(1L)).thenReturn(Optional.of(1L));

		RestaurantInfo restaurantInfo = mock(RestaurantInfo.class);
		when(internalRestaurantService._getRestaurantInfoWithRestaurantByRestaurantId(1L)).thenReturn(restaurantInfo);

		lineupServiceImpl.cancelWaiting(request);

		verify(restaurantInfo).subtractLineupCount();
	}

	private void stub_getByIdWithUser(Lineup lineup) {
		when(lineupRepository.findByIdWithUser(1L)).thenReturn(Optional.of(lineup));
	}

	@Test
	@DisplayName("판매자 오늘 줄서기 조회")
	void getTodayLineups() {
		TodayLineupResponse todayLineupResponse = mock(TodayLineupResponse.class);
		List<TodayLineupResponse> todayLineupResponses = List.of(todayLineupResponse, todayLineupResponse);
		when(lineupRepository.findTodayLineupsBySellerId(anyLong())).thenReturn(todayLineupResponses);

		assertThat(lineupServiceImpl.getTodayLineups(1L)).containsExactlyElementsOf(todayLineupResponses);
	}

	@Test
	@DisplayName("오늘 내 줄서기 내역 조회")
	void getLineupRecords(@Mock GetLineupRecordsServiceRequest serviceRequest) {
		when(serviceRequest.getUserId()).thenReturn(1L);
		when(serviceRequest.getStatus()).thenReturn(ArrivalStatusEnum.ARRIVE);

		LineupRecordResponse lineupRecordResponse = mock(LineupRecordResponse.class);
		List<LineupRecordResponse> lineupRecordResponses = List.of(lineupRecordResponse, lineupRecordResponse);
		when(lineupRepository.findRecordsByUserIdAndStatus(serviceRequest.getUserId(),
			serviceRequest.getStatus())).thenReturn(lineupRecordResponses);

		assertThat(lineupServiceImpl.getLineupRecords(serviceRequest)).hasSameSizeAs(lineupRecordResponses);
	}

	@Test
	@DisplayName("내 줄서기 히스토리 조회")
	void getLineupHistoryRecords(@Mock GetLineupHistoryRecordsServiceRequest serviceRequest, @Mock Pageable pageable) {
		when(serviceRequest.getId()).thenReturn(1L);
		when(serviceRequest.getUserId()).thenReturn(1L);
		when(serviceRequest.getStatus()).thenReturn(ArrivalStatusEnum.ARRIVE);

		LineupRecordResponse lineupRecordResponse = mock(LineupRecordResponse.class);
		Slice<LineupRecordResponse> slice = new SliceImpl<>(List.of(lineupRecordResponse), pageable, true);

		when(internalLineupHistoryService._getRecordsByUserId(serviceRequest.getId(), serviceRequest.getUserId(),
			serviceRequest.getStatus(), pageable)).thenReturn(slice);

		LineupRecordWithTypeResponse lineupRecordWithTypeResponse = mock(LineupRecordWithTypeResponse.class);
		List<LineupRecordWithTypeResponse> content = List.of(lineupRecordWithTypeResponse);

		assertThat(lineupServiceImpl.getLineupHistoryRecords(serviceRequest, pageable)).hasSameSizeAs(
			new SliceImpl<>(content, pageable, slice.hasNext()));
	}

	@Test
	@DisplayName("고객 상태 변경 - 다른 레스토랑의 줄서기 상태 변경 시도")
	void updateArrivalStatus_isNotSameRestaurant(@Mock UpdateArrivalStatusServiceRequest serviceRequest) {
		when(serviceRequest.getLineupId()).thenReturn(1L);

		Lineup lineup = mock(Lineup.class);

		stub_getByIdWithUser(lineup);

		RestaurantInfo restaurantInfo = mock(RestaurantInfo.class);
		Restaurant restaurant = mock(Restaurant.class);

		when(serviceRequest.getSellerId()).thenReturn(1L);
		when(internalRestaurantService._getRestaurantInfoWithRestaurantByUserId(serviceRequest.getSellerId()))
			.thenReturn(restaurantInfo);
		when(restaurantInfo.getRestaurant()).thenReturn(restaurant);
		when(lineup.isSameRestaurant(restaurant)).thenReturn(false);

		assertThatThrownBy(() -> lineupServiceImpl.updateArrivalStatus(serviceRequest))
			.isInstanceOf(IllegalRequestException.class)
			.hasMessage(NOT_IN_RESTAURANT.getMessage());
	}

	@Test
	@DisplayName("고객 상태 변경 - 고객 호출")
	void updateArrivalStatus_call(@Mock UpdateArrivalStatusServiceRequest serviceRequest) throws
		UnsupportedEncodingException,
		URISyntaxException,
		NoSuchAlgorithmException,
		InvalidKeyException,
		JsonProcessingException {

		when(serviceRequest.getLineupId()).thenReturn(1L);

		Lineup lineup = mock(Lineup.class);

		stub_getByIdWithUser(lineup);

		RestaurantInfo restaurantInfo = mock(RestaurantInfo.class);
		Restaurant restaurant = mock(Restaurant.class);

		stubUpdateArrivalStatus(serviceRequest, restaurantInfo, restaurant, lineup, ArrivalStatusEnum.CALL);

		CallCustomerInfoResponse customerInfo = mock(CallCustomerInfoResponse.class);
		stubCustomerInfo(lineup, customerInfo);

		lineupServiceImpl.updateArrivalStatus(serviceRequest);

		verify(smsService).sendSms(any(MessageRequest.class));
	}

	@Test
	@DisplayName("고객 상태 변경 - 고객 취소")
	void updateArrivalStatus_cancel(@Mock UpdateArrivalStatusServiceRequest serviceRequest) throws
		UnsupportedEncodingException,
		URISyntaxException,
		NoSuchAlgorithmException,
		InvalidKeyException,
		JsonProcessingException {

		when(serviceRequest.getLineupId()).thenReturn(1L);

		Lineup lineup = mock(Lineup.class);

		stub_getByIdWithUser(lineup);

		RestaurantInfo restaurantInfo = mock(RestaurantInfo.class);
		Restaurant restaurant = mock(Restaurant.class);

		stubUpdateArrivalStatus(serviceRequest, restaurantInfo, restaurant, lineup, ArrivalStatusEnum.CANCEL);

		CallCustomerInfoResponse customerInfo = mock(CallCustomerInfoResponse.class);
		stubCustomerInfo(lineup, customerInfo);

		lineupServiceImpl.updateArrivalStatus(serviceRequest);

		verify(restaurantInfo).subtractLineupCount();
		verify(smsService).sendSms(any(MessageRequest.class));
	}

	@Test
	@DisplayName("고객 상태 변경 - 고객 도착")
	void updateArrivalStatus_arrive(@Mock UpdateArrivalStatusServiceRequest serviceRequest) throws
		UnsupportedEncodingException,
		URISyntaxException,
		NoSuchAlgorithmException,
		InvalidKeyException,
		JsonProcessingException {

		when(serviceRequest.getLineupId()).thenReturn(1L);

		Lineup lineup = mock(Lineup.class);

		stub_getByIdWithUser(lineup);

		RestaurantInfo restaurantInfo = mock(RestaurantInfo.class);
		Restaurant restaurant = mock(Restaurant.class);

		stubUpdateArrivalStatus(serviceRequest, restaurantInfo, restaurant, lineup, ArrivalStatusEnum.ARRIVE);

		when(lineupRepository.findAllByUserId(lineup.getUserId())).thenReturn(List.of(lineup));

		CallCustomerInfoResponse customerInfo = mock(CallCustomerInfoResponse.class);
		stubCustomerInfo(lineup, customerInfo);

		lineupServiceImpl.updateArrivalStatus(serviceRequest);

		verify(restaurantInfo).subtractLineupCount();
		verify(smsService).sendSms(any(MessageRequest.class));
	}

	@Test
	@DisplayName("고객 상태 변경 - 잘못된 상태로 변경 시도")
	void updateArrivalStatus_illegalArrivalStatus(@Mock UpdateArrivalStatusServiceRequest serviceRequest) {
		when(serviceRequest.getLineupId()).thenReturn(1L);

		Lineup lineup = mock(Lineup.class);

		stub_getByIdWithUser(lineup);

		RestaurantInfo restaurantInfo = mock(RestaurantInfo.class);
		Restaurant restaurant = mock(Restaurant.class);

		stubUpdateArrivalStatus(serviceRequest, restaurantInfo, restaurant, lineup, ArrivalStatusEnum.WAIT);

		assertThatThrownBy(() -> lineupServiceImpl.updateArrivalStatus(serviceRequest))
			.isInstanceOf(IllegalRequestException.class)
			.hasMessage(INTERNAL_ERROR.getMessage());
	}

	private void stubUpdateArrivalStatus(UpdateArrivalStatusServiceRequest serviceRequest,
		RestaurantInfo restaurantInfo,
		Restaurant restaurant, Lineup lineup, ArrivalStatusEnum call) {
		when(serviceRequest.getSellerId()).thenReturn(1L);
		when(internalRestaurantService._getRestaurantInfoWithRestaurantByUserId(serviceRequest.getSellerId()))
			.thenReturn(restaurantInfo);
		when(restaurantInfo.getRestaurant()).thenReturn(restaurant);
		when(lineup.isSameRestaurant(restaurant)).thenReturn(true);

		when(lineup.updateStatus(serviceRequest.getStatus())).thenReturn(call);
	}

	private void stubCustomerInfo(Lineup lineup, CallCustomerInfoResponse customerInfo) {
		when(lineup.getId()).thenReturn(1L);
		when(lineupRepository.findCallCustomerInfoById(lineup.getId())).thenReturn(customerInfo);
		when(customerInfo.getPhoneNumber()).thenReturn("010-0000-0000");
	}
}