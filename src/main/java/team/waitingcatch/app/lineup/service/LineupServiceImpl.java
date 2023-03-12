package team.waitingcatch.app.lineup.service;

import static team.waitingcatch.app.exception.ErrorCode.*;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
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
	public static final int MAX_DISTANCE = 3;
	public static final int MAX_LINEUP_COUNT = 3;

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

	@Retryable(maxAttempts = 3, backoff = @Backoff(1000), value = OptimisticLockingFailureException.class)
	@Override
	public void startWaiting(StartWaitingServiceRequest serviceRequest) {
		long restaurantId = serviceRequest.getRestaurantId();
		RestaurantInfo restaurantInfo = internalRestaurantService._getRestaurantInfoWithRestaurantByRestaurantId(
			restaurantId);
		Restaurant restaurant = restaurantInfo.getRestaurant();

		if (!restaurantInfo.isLineupActive()) {
			throw new IllegalArgumentException(CLOSED_LINEUP.getMessage());
		}

		boolean isBlacklist = internalBlacklistService._existsByRestaurantIdAndUserId(restaurantId,
			serviceRequest.getUser().getId());

		if (isBlacklist) {
			throw new IllegalRequestException(ILLEGAL_ACCESS);
		}

		List<Lineup> lineups = lineupRepository.findAllByUserIdAndStatuses(
			serviceRequest.getUser().getId(),
			Arrays.asList(ArrivalStatusEnum.WAIT, ArrivalStatusEnum.CALL));

		if (lineups.size() >= MAX_LINEUP_COUNT) {
			throw new IllegalArgumentException(EXCEED_MAX_LINEUP_COUNT.getMessage());
		}

		double userLatitude = serviceRequest.getLatitude();
		double userLongitude = serviceRequest.getLongitude();
		double restaurantLatitude = restaurant.getLatitude();
		double restaurantLongitude = restaurant.getLongitude();

		if (distanceCalculator.distanceInKilometerByHaversine(userLatitude, userLongitude, restaurantLatitude,
			restaurantLongitude) > MAX_DISTANCE) {
			throw new IllegalArgumentException(DISTANCE_EXCEEDED.getMessage());
		}

		int waitingNumber = internalWaitingNumberService.getWaitingNumber(restaurantId);

		StartLineupEntityRequest entityRequest = new StartLineupEntityRequest(serviceRequest, restaurant,
			waitingNumber);

		Lineup lineup = Lineup.of(entityRequest);
		lineupRepository.save(lineup);
		restaurantInfo.addLineupCount();
	}

	@Recover
	private void recover(OptimisticLockingFailureException e) {
		throw new IllegalArgumentException(CONNCURRENT_REQUEST_FAILURE.getMessage());
	}

	@Recover
	private void recover(RuntimeException e) {
		throw new IllegalArgumentException(e.getMessage());
	}

	@Override
	public void cancelWaiting(CancelWaitingRequest request) {
		long lineupId = request.getLineupId();
		Lineup lineup = _getByIdWithUser(lineupId);
		if (!lineup.isSameUserId(request.getUserId())) {
			throw new IllegalRequestException(ILLEGAL_ACCESS);
		}
		lineup.updateStatus(ArrivalStatusEnum.CANCEL);

		Long restaurantId = lineupRepository.findRestaurantIdById(lineupId)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_LINEUP.getMessage()));

		RestaurantInfo restaurantInfo = internalRestaurantService._getRestaurantInfoWithRestaurantByRestaurantId(
			restaurantId);
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
		return lineupRepository.findRecordsByUserIdAndStatus(serviceRequest.getUserId(), serviceRequest.getStatus())
			.stream()
			.map(lineupRecord -> LineupRecordWithTypeResponse.of(lineupRecord, StoredLineupTableNameEnum.LINEUP))
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Override
	public Slice<LineupRecordWithTypeResponse> getLineupHistoryRecords(
		GetLineupHistoryRecordsServiceRequest serviceRequest,
		Pageable pageable) {

		Slice<LineupRecordResponse> slice = internalLineupHistoryService._getRecordsByUserId(serviceRequest.getId(),
			serviceRequest.getUserId(), serviceRequest.getStatus(), pageable);

		List<LineupRecordWithTypeResponse> content = slice
			.get()
			.map(
				lineupRecord -> LineupRecordWithTypeResponse.of(lineupRecord, StoredLineupTableNameEnum.LINEUP_HISTORY))
			.collect(Collectors.toList());

		return new SliceImpl<>(content, pageable, slice.hasNext());
	}

	@Override
	public void updateArrivalStatus(UpdateArrivalStatusServiceRequest serviceRequest) {
		Lineup restaurantInCustomer = _getByIdWithUser(serviceRequest.getLineupId());

		RestaurantInfo restaurantInfo = internalRestaurantService._getRestaurantInfoWithRestaurantByUserId(
			serviceRequest.getSellerId());
		Restaurant restaurantBySeller = restaurantInfo.getRestaurant();

		if (!restaurantInCustomer.isSameRestaurant(restaurantBySeller)) {
			throw new IllegalRequestException(NOT_IN_RESTAURANT);
		}

		ArrivalStatusEnum updatedStatus = restaurantInCustomer.updateStatus(serviceRequest.getStatus());

		if (updatedStatus == ArrivalStatusEnum.CALL) {
			sendSms(restaurantInCustomer.getId(), updatedStatus);
		} else if (updatedStatus == ArrivalStatusEnum.CANCEL) {
			restaurantInfo.subtractLineupCount();
			sendSms(restaurantInCustomer.getId(), updatedStatus);
		} else if (updatedStatus == ArrivalStatusEnum.ARRIVE) {
			restaurantInfo.subtractLineupCount();
			List<Lineup> lineups = lineupRepository.findAllByUserId(restaurantInCustomer.getUserId());
			lineups.stream()
				.filter(lineup -> (
					lineup.getStatus() == ArrivalStatusEnum.WAIT || lineup.getStatus() == ArrivalStatusEnum.CALL))
				.forEach(lineup -> {
					lineup.updateStatus(ArrivalStatusEnum.CANCEL);
					restaurantInfo.subtractLineupCount();
				});

			if (!lineups.isEmpty()) {
				sendSms(restaurantInCustomer.getId(), updatedStatus);
			}
		} else {
			throw new IllegalRequestException(INTERNAL_ERROR);
		}
	}

	private void sendSms(Long lineupId, ArrivalStatusEnum status) {
		CallCustomerInfoResponse customerInfo = lineupRepository.findCallCustomerInfoById(lineupId);

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("[WAITING CATCH]")
			.append(System.lineSeparator())
			.append(customerInfo.getRestaurantName());

		if (status == ArrivalStatusEnum.ARRIVE) {
			stringBuilder.append("에 도착하셨네요.")
				.append(System.lineSeparator())
				.append("아직 줄서기 중인 곳은 취소 처리해 드릴게요.");
		} else {
			stringBuilder.append("에서 대기번호 ")
				.append(customerInfo.getWaitingNumber())
				.append("번 고객님을 ")
				.append(status.getValue())
				.append("하셨어요.");
		}

		MessageRequest messageRequest = new MessageRequest(customerInfo.getPhoneNumber(), "WAITING CATCH",
			stringBuilder.toString());

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
		return lineupRepository.findById(id)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_LINEUP.getMessage()));
	}

	@Transactional(readOnly = true)
	@Override
	public Lineup _getByIdWithUser(Long id) {
		return lineupRepository.findByIdWithUser(id)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_LINEUP.getMessage()));
	}

	@Override
	public void _bulkSoftDeleteByRestaurantId(Long restaurantId) {
		lineupRepository.bulkSoftDeleteByRestaurantId(restaurantId);
	}
}