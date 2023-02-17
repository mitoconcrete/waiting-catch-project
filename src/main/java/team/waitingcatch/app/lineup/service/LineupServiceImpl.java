package team.waitingcatch.app.lineup.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.lineup.dto.PastLineupResponse;
import team.waitingcatch.app.lineup.dto.StartLineupEntityRequest;
import team.waitingcatch.app.lineup.dto.StartLineupServiceRequest;
import team.waitingcatch.app.lineup.dto.TodayLineupResponse;
import team.waitingcatch.app.lineup.dto.UpdateArrivalStatusServiceRequest;
import team.waitingcatch.app.lineup.entity.Lineup;
import team.waitingcatch.app.lineup.repository.LineupHistoryRepository;
import team.waitingcatch.app.lineup.repository.LineupRepository;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.service.restaurant.InternalRestaurantService;
import team.waitingcatch.app.user.service.InternalUserService;

@Service
@Transactional
@RequiredArgsConstructor
public class LineupServiceImpl implements LineupService, InternalLineupService {
	private final LineupRepository lineupRepository;
	private final LineupHistoryRepository lineupHistoryRepository;

	private final InternalUserService internalUserService;
	private final InternalRestaurantService internalRestaurantService;

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
	public List<PastLineupResponse> getPastLineups(Long userId) {
		List<PastLineupResponse> todayLineupList = lineupRepository.findAllByUserId(userId);
		List<PastLineupResponse> pastLineupList = lineupHistoryRepository.findAllByUserId(userId);
		return Stream.concat(todayLineupList.stream(), pastLineupList.stream()).collect(Collectors.toList());
	}

	@Override
	public void updateArrivalStatus(UpdateArrivalStatusServiceRequest serviceRequest) {
		Lineup restaurantInCustomer = _getLineupById(serviceRequest.getLineupId());
		Restaurant restaurantBySeller = internalRestaurantService._getRestaurantByUserId(serviceRequest.getSellerId());
		if (!restaurantInCustomer.isSameRestaurant(restaurantBySeller)) {
			throw new IllegalArgumentException("현재 레스토랑의 손님이 아닙니다.");
		}

		restaurantInCustomer.updateStatus(serviceRequest.getStatus());
	}

	@Transactional(readOnly = true)
	@Override
	public Lineup _getLineupById(Long id) {
		return lineupRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
	}
}