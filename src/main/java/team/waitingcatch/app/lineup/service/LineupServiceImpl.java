package team.waitingcatch.app.lineup.service;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.util.DistanceCalculator;
import team.waitingcatch.app.common.util.sms.SmsService;
import team.waitingcatch.app.common.util.sms.dto.MessageRequest;
import team.waitingcatch.app.lineup.dto.CallCustomerInfoResponse;
import team.waitingcatch.app.lineup.dto.CancelWaitingRequest;
import team.waitingcatch.app.lineup.dto.GetLineupRecordsServiceRequest;
import team.waitingcatch.app.lineup.dto.LineupRecordWithTypeResponse;
import team.waitingcatch.app.lineup.dto.StartLineupEntityRequest;
import team.waitingcatch.app.lineup.dto.StartWaitingServiceRequest;
import team.waitingcatch.app.lineup.dto.TodayLineupResponse;
import team.waitingcatch.app.lineup.dto.UpdateArrivalStatusServiceRequest;
import team.waitingcatch.app.lineup.entity.Lineup;
import team.waitingcatch.app.lineup.enums.ArrivalStatusEnum;
import team.waitingcatch.app.lineup.enums.StoredLineupTableNameEnum;
import team.waitingcatch.app.lineup.repository.LineupRepository;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.entity.RestaurantInfo;
import team.waitingcatch.app.restaurant.service.blacklist.InternalBlacklistService;
import team.waitingcatch.app.restaurant.service.restaurant.InternalRestaurantService;

@Service
@Transactional
@RequiredArgsConstructor
public class LineupServiceImpl implements LineupService, InternalLineupService {
	private final LineupRepository lineupRepository;

	private final InternalRestaurantService internalRestaurantService;
	private final InternalLineupHistoryService internalLineupHistoryService;
	private final InternalWaitingNumberService internalWaitingNumberService;
	private final InternalBlacklistService internalBlacklistService;

	private final DistanceCalculator distanceCalculator;
	private final SmsService smsService;

	@Override
	public void openLineup(Long sellerId) {
		Long restaurantId = internalRestaurantService._getRestaurantByUserId(sellerId).getId();
		internalRestaurantService._openLineup(restaurantId);
	}

	@Override
	public void closeLineup(Long sellerId) {
		Long restaurantId = internalRestaurantService._getRestaurantByUserId(sellerId).getId();
		internalRestaurantService._closeLineup(restaurantId);
	}

	@Override
	public void startWaiting(StartWaitingServiceRequest serviceRequest) {
		Long restaurantId = serviceRequest.getRestaurantId();
		RestaurantInfo restaurantInfo = internalRestaurantService._getRestaurantInfoByRestaurantId(restaurantId);
		Restaurant restaurant = internalRestaurantService._getRestaurantById(restaurantId);

		// if (!restaurantInfo.isLineupActive()) {
		// 	throw new IllegalArgumentException("줄서기가 마감되었습니다.");
		// }
		//
		boolean isBlacklist = internalBlacklistService._existsByRestaurantIdAndUserId(restaurantId,
			serviceRequest.getUser().getId());

		if (isBlacklist) {
			throw new IllegalArgumentException("블랙리스트는 이용할 수 없습니다");
		}

		double userLatitude = serviceRequest.getLatitude();
		double userLongitude = serviceRequest.getLongitude();
		double restaurantLatitude = restaurant.getLatitude();
		double restaurantLongitude = restaurant.getLongitude();

		if (distanceCalculator.distanceInKilometerByHaversine(userLatitude, userLongitude, restaurantLatitude,
			restaurantLongitude) > 2) {
			throw new IllegalArgumentException("2km 이내의 레스토랑에만 줄서기가 가능합니다");
		}

		// List<Lineup> lineupList = lineupRepository.findByRestaurantId(restaurantId);
		// int waitingNumber = 1;
		// if (lineupList.size() > 1) {
		// 	waitingNumber = generateWaitingNumber(lineupList.stream()
		// 		.max(Comparator.comparing(Lineup::getWaitingNumber))
		// 		.get()
		// 		.getWaitingNumber());
		// }
		//
		// Integer lastWaitingNumber = lineupRepository.findLastWaitingNumberByRestaurantId(restaurantId);
		// int waitingNumber = generateWaitingNumber(lastWaitingNumber);
		int waitingNumber = internalWaitingNumberService.getNextWaitingNumber(restaurantId);

		StartLineupEntityRequest entityRequest = new StartLineupEntityRequest(serviceRequest, restaurant,
			waitingNumber);

		Lineup lineup = Lineup.createLineup(entityRequest);
		lineupRepository.save(lineup);
		restaurantInfo.addLineupCount();
	}

	@Override
	public void cancelWaiting(CancelWaitingRequest request) {
		long lineupId = request.getLineupId();
		Lineup lineup = _getByIdWithUser(lineupId);
		if (!lineup.isSameUserId(request.getUserId())) {
			throw new IllegalArgumentException("유저 정보가 일치하지 않습니다.");
		}
		lineup.updateStatus(ArrivalStatusEnum.CANCEL);

		Long restaurantId = lineupRepository.findRestaurantIdById(lineupId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 줄서기입니다."));

		RestaurantInfo restaurantInfo = internalRestaurantService._getRestaurantInfoByRestaurantId(restaurantId);
		restaurantInfo.subtractLineupCount();
	}

	@Transactional(readOnly = true)
	@Override
	public List<TodayLineupResponse> getTodayLineups(Long sellerId) {
		return lineupRepository.findTodayLineupsBySellerId(sellerId);
	}

	@Transactional(readOnly = true)
	@Override
	public List<LineupRecordWithTypeResponse> getLineupRecords(GetLineupRecordsServiceRequest serviceRequest) {
		List<LineupRecordWithTypeResponse> todayLineupList = lineupRepository.findRecordsByUserIdAndStatus(
				serviceRequest.getUserId(), serviceRequest.getStatus())
			.stream()
			.map(lineupRecord -> LineupRecordWithTypeResponse.of(lineupRecord, StoredLineupTableNameEnum.LINEUP))
			.collect(Collectors.toList());

		List<LineupRecordWithTypeResponse> pastLineupList = internalLineupHistoryService._getRecordsByUserId(
				serviceRequest.getUserId(), serviceRequest.getStatus())
			.stream()
			.map(
				lineupRecord -> LineupRecordWithTypeResponse.of(lineupRecord, StoredLineupTableNameEnum.LINEUP_HISTORY))
			.collect(Collectors.toList());

		return Stream.concat(todayLineupList.stream(), pastLineupList.stream()).collect(Collectors.toList());
	}

	@Override
	public void updateArrivalStatus(UpdateArrivalStatusServiceRequest serviceRequest) {
		Lineup restaurantInCustomer = _getByIdWithUser(serviceRequest.getLineupId());
		Restaurant restaurantBySeller = internalRestaurantService._getRestaurantByUserId(serviceRequest.getSellerId());
		if (!restaurantInCustomer.isSameRestaurant(restaurantBySeller)) {
			throw new IllegalArgumentException("현재 레스토랑의 손님이 아닙니다.");
		}

		ArrivalStatusEnum updatedStatus = restaurantInCustomer.updateStatus(serviceRequest.getStatus());
		if (updatedStatus == ArrivalStatusEnum.CALL) {
			callCustomer(restaurantInCustomer.getId());
		} else if (updatedStatus == ArrivalStatusEnum.ARRIVE) {
			lineupRepository.findAllByUserId(restaurantInCustomer.getUserId())
				.stream()
				.filter(lineup -> lineup.getStatus() == (ArrivalStatusEnum.WAIT))
				.forEach(lineup -> lineup.updateStatus(ArrivalStatusEnum.CANCEL));
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

	private int generateWaitingNumber(Integer lastWaitingNumber) {
		return lastWaitingNumber != null ? lastWaitingNumber + 1 : 1;
	}

	@Transactional(readOnly = true)
	@Override
	public Lineup _getById(Long id) {
		return lineupRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 줄서기입니다."));
	}

	@Override
	public Lineup _getByIdWithUser(Long id) {
		return lineupRepository.findByIdWithUser(id)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 줄서기입니다."));
	}

	@Override
	public void _bulkSoftDeleteByRestaurantId(Long restaurantId) {
		lineupRepository.bulkSoftDeleteByRestaurantId(restaurantId);
	}
}