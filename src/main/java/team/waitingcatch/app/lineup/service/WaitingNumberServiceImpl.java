package team.waitingcatch.app.lineup.service;

import static team.waitingcatch.app.exception.ErrorCode.*;

import java.util.NoSuchElementException;

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
	public int getWaitingNumber(long restaurantId) {
		WaitingNumber waitingNumber = waitingNumberRepository.findByRestaurantId(restaurantId)
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_WAITING_NUMBER.getMessage()));
		waitingNumber.updateNextNumber();
		return waitingNumber.getNextNumber() - 1;
	}
}