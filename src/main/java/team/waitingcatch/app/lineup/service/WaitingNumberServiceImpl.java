package team.waitingcatch.app.lineup.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.lineup.entity.WaitingNumber;
import team.waitingcatch.app.lineup.repository.WaitingNumberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class WaitingNumberServiceImpl implements InternalWaitingNumberService {
	private final WaitingNumberRepository waitingNumberRepository;

	@Override
	public int getWaitingNumber(Long restaurantId) {
		WaitingNumber waitingNumber = waitingNumberRepository.findByRestaurantId(restaurantId)
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 대기 번호입니다."));
		waitingNumber.updateNextNumber();
		return waitingNumber.getNextNumber() - 1;
	}
}