package team.waitingcatch.app.lineup.service;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.util.sms.SmsService;
import team.waitingcatch.app.common.util.sms.dto.MessageRequest;
import team.waitingcatch.app.lineup.dto.CallCustomerInfoResponse;
import team.waitingcatch.app.lineup.dto.LineupRecordWithTypeResponse;
import team.waitingcatch.app.lineup.dto.StartLineupEntityRequest;
import team.waitingcatch.app.lineup.dto.StartLineupServiceRequest;
import team.waitingcatch.app.lineup.dto.TodayLineupResponse;
import team.waitingcatch.app.lineup.dto.UpdateArrivalStatusServiceRequest;
import team.waitingcatch.app.lineup.entity.Lineup;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;
import team.waitingcatch.app.lineup.enums.StoredLineupTableNameEnum;
import team.waitingcatch.app.lineup.repository.LineupRepository;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.service.restaurant.InternalRestaurantService;

@Service
@Transactional
@RequiredArgsConstructor
public class LineupServiceImpl implements LineupService, InternalLineupService {
	private final LineupRepository lineupRepository;

	private final InternalRestaurantService internalRestaurantService;
	private final InternalLineupHistoryService internalLineupHistoryService;

	private final SmsService smsService;

	@Override
	public void openLineup(Long sellerId) {
		Long restaurantId = internalRestaurantService._getRestaurant(sellerId).getId();
		internalRestaurantService._openLineup(restaurantId);
	}

	@Override
	public void closeLineup(Long sellerId) {
		Long restaurantId = internalRestaurantService._getRestaurant(sellerId).getId();
		internalRestaurantService._closeLineup(restaurantId);
	}

	@Override
	public void startWaiting(StartLineupServiceRequest serviceRequest) {
		Long restaurantId = serviceRequest.getRestaurantId();
		Restaurant restaurant = internalRestaurantService._getRestaurant(restaurantId);
		// if (isLineupActive()) RestaurantInfo 수정 후 추가
		Integer lastWaitingNumber = lineupRepository.findLastWaitingNumberByRestaurantId(restaurantId);
		StartLineupEntityRequest entityRequest = new StartLineupEntityRequest(serviceRequest, restaurant,
			lastWaitingNumber);
		Lineup lineup = Lineup.createLineup(entityRequest);
		lineupRepository.save(lineup);
	}

	@Transactional(readOnly = true)
	@Override
	public List<TodayLineupResponse> getLineups(Long sellerId) {
		return lineupRepository.findAllBySellerId(sellerId);
	}

	@Transactional(readOnly = true)
	@Override
	public List<LineupRecordWithTypeResponse> getPastLineups(Long userId) {
		List<LineupRecordWithTypeResponse> todayLineupList = lineupRepository.findAllByUserId(userId)
			.stream()
			.map(lineupRecord -> LineupRecordWithTypeResponse.of(lineupRecord, StoredLineupTableNameEnum.LINEUP))
			.collect(Collectors.toList());

		List<LineupRecordWithTypeResponse> pastLineupList = internalLineupHistoryService._getAllRecordByUserId(userId)
			.stream()
			.map(lineupRecord -> LineupRecordWithTypeResponse.of(lineupRecord, StoredLineupTableNameEnum.LINEUP_HISTORY))
			.collect(Collectors.toList());

		return Stream.concat(todayLineupList.stream(), pastLineupList.stream()).collect(Collectors.toList());
	}

	@Override
	public void updateArrivalStatus(UpdateArrivalStatusServiceRequest serviceRequest) {
		Lineup restaurantInCustomer = _getById(serviceRequest.getLineupId());
		Restaurant restaurantBySeller = internalRestaurantService._getRestaurantByUserId(serviceRequest.getSellerId());
		if (!restaurantInCustomer.isSameRestaurant(restaurantBySeller)) {
			throw new IllegalArgumentException("현재 레스토랑의 손님이 아닙니다.");
		}

		ArrivalStatusEnum updateStatus = restaurantInCustomer.updateStatus(serviceRequest.getStatus());
		if (updateStatus == ArrivalStatusEnum.CALL) {
			callCustomer(restaurantInCustomer.getId());
		}
	}

	private void callCustomer(Long lineupId) {
		CallCustomerInfoResponse customerInfo = lineupRepository.findCallCustomerInfoById(lineupId);
		String content = "[호출]%n레스토랑: " + customerInfo.getRestaurantName() + "%n대기번호: " +
			customerInfo.getWaitingNumber();
		MessageRequest messageRequest = new MessageRequest(customerInfo.getPhoneNumber(), "Waiting Catch",
			content);

		try {
			smsService.sendSms(messageRequest);
		} catch (JsonProcessingException | URISyntaxException | UnsupportedEncodingException |
				 NoSuchAlgorithmException | InvalidKeyException e) {
			throw new IllegalArgumentException(e);
		}
	}

	@Transactional(readOnly = true)
	@Override
	public Lineup _getById(Long id) {
		return lineupRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 줄서기입니다."));
	}
}