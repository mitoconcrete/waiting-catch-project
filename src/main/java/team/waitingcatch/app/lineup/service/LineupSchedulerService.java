package team.waitingcatch.app.lineup.service;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.util.sms.SmsService;
import team.waitingcatch.app.common.util.sms.dto.MessageRequest;
import team.waitingcatch.app.lineup.dto.CustomerLineupInfoResponse;
import team.waitingcatch.app.lineup.entity.Lineup;
import team.waitingcatch.app.lineup.entity.LineupHistory;
import team.waitingcatch.app.lineup.repository.LineupHistoryRepository;
import team.waitingcatch.app.lineup.repository.LineupRepository;
import team.waitingcatch.app.lineup.repository.WaitingNumberRepository;

@Service
@EnableScheduling
@Transactional
@RequiredArgsConstructor
public class LineupSchedulerService {
	private final LineupRepository lineupRepository;
	private final LineupHistoryRepository lineupHistoryRepository;
	private final WaitingNumberRepository waitingNumberRepository;

	private final SmsService smsService;

	private static final int SIZE = 1;

	@Scheduled(cron = "0 30 4 * * *")
	public void resetStartWaitingNumber() {
		waitingNumberRepository.bulkResetWaitingNumber();
	}

	@Scheduled(cron = "0 0 5 * * *")
	public void transferLineupToLineupHistory() {
		int page = 0;
		Slice<Lineup> slice;
		Long lastId = null;

		do {
			slice = lineupRepository.findAll(lastId, PageRequest.of(page++, SIZE));
			List<LineupHistory> lineupList = slice.getContent()
				.stream()
				.map(LineupHistory::of)
				.collect(Collectors.toList());

			lineupHistoryRepository.saveAll(lineupList);
			lineupRepository.deleteAll(slice);

			lastId = slice.getContent().get(slice.getNumberOfElements() - 1).getId();
		} while (slice.hasNext());
	}

	@Scheduled(cron = "0 30 21 * * *")
	@Transactional(readOnly = true)
	public void requestReview() {
		int page = 0;
		Slice<CustomerLineupInfoResponse> lineupHistories;

		LocalDateTime localDateTime = LocalDateTime.now().minusDays(1L).minusMinutes(1L);
		Long lastId = null;
		do {
			lineupHistories = lineupHistoryRepository.findLineupHistoryToRequestReviewGreaterThanCreatedDate(lastId,
				localDateTime, PageRequest.of(page++, SIZE));

			lastId = lineupHistories.getContent().get(lineupHistories.getNumberOfElements() - 1).getLineupId();

			lineupHistoryRepository.bulkUpdateIsReceivedReviewRequest(lineupHistories.getContent()
				.stream()
				.map(CustomerLineupInfoResponse::getLineupId)
				.collect(Collectors.toList()));
		} while (lineupHistories.hasNext());

		page = 0;
		Slice<CustomerLineupInfoResponse> lineups;

		do {
			lineups = lineupRepository.findCustomerLineupInfoByIsReviewedFalse(PageRequest.of(page++, SIZE));
			List<Long> lineupIds = new ArrayList<>(SIZE);
			for (CustomerLineupInfoResponse response : lineups) {
				String content = "[WAITING CATCH]" + System.lineSeparator()
					+ response.getName() + "님 " + response.getRestaurantName() + "에서의 시간은 어떠셨나요?"
					+ System.lineSeparator() + "아래 URL에서 다른 분들께 고객님의 경험을 공유해 주세요!" + System.lineSeparator()
					+ "https://waitingcatch.com/customer/restaurants/" + response.getRestaurantId() + "/reviews"
					+ "그럼 고객님의 솔직한 리뷰를 기다리고 있을게요\uD83D\uDE42";

				MessageRequest messageRequest = new MessageRequest(response.getName(), "WAITING CATCH", content);

				try {
					smsService.sendSms(messageRequest);
				} catch (JsonProcessingException | URISyntaxException | UnsupportedEncodingException |
						 NoSuchAlgorithmException | InvalidKeyException e) {
					throw new IllegalArgumentException(e);
				}
				lineupIds.add(response.getLineupId());
			}
			lineupRepository.bulkUpdateIsReceivedReviewRequest(lineupIds);
		} while (lineups.hasNext());
	}
}