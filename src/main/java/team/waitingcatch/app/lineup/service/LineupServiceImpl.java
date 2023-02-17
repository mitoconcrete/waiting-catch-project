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
	public void startLineup(StartLineupServiceRequest serviceRequest) {
		Long restaurantId = serviceRequest.getRestaurantId();
		Restaurant restaurant = internalRestaurantService._getRestaurant(restaurantId);
		// if (isLineupActive == true)
		Integer lastWaitingNumber = lineupRepository.findLastWaitingNumberByRestaurantId(restaurantId);
		StartLineupEntityRequest entityRequest = new StartLineupEntityRequest(serviceRequest, restaurant,
			lastWaitingNumber);
		Lineup lineup = Lineup.createLineup(entityRequest);
		lineupRepository.save(lineup);
	}

	@Override
	public List<TodayLineupResponse> getLineups(Long sellerId) {
		return lineupRepository.findAllBySellerId(sellerId);
	}

	@Override
	public List<PastLineupResponse> getPastLineups(Long userId) {
		String username = internalUserService._getUsernameById(userId);
		List<PastLineupResponse> todayLineupList = lineupRepository.findAllByUserId(userId);
		List<PastLineupResponse> pastLineupList = lineupHistoryRepository.findAllByUserId(userId);
		return Stream.concat(todayLineupList.stream(), pastLineupList.stream()).collect(Collectors.toList());
	}
}