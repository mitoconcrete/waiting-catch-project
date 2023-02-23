package team.waitingcatch.app.lineup.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.lineup.repository.WaitingNumberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class WaitingNumberServiceImpl implements InternalWaitingNumberService {
	private final WaitingNumberRepository waitingNumberRepository;

	@Override
	public int getNextWaitingNumber(Long restaurantId) {
		int waitingNumber = waitingNumberRepository.findByRestaurantId(restaurantId);
		waitingNumberRepository.updateWaitingNumber(restaurantId);
		return waitingNumber;
	}
}