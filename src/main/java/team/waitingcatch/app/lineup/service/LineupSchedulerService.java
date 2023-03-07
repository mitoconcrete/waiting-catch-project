package team.waitingcatch.app.lineup.service;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
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

	@Scheduled(cron = "0 30 4 * * *")
	public void resetStartWaitingNumber() {
		waitingNumberRepository.bulkResetWaitingNumber();
	}

	@Scheduled(cron = "0 0 5 * * *")
	public void transferLineupToLineupHistory() {
		final int size = 1000;
		int page = 0;

		Slice<Lineup> slice = lineupRepository.findAll(PageRequest.of(page, size));

		while (slice.hasNext()) {
			List<LineupHistory> lineupList = slice.getContent()
				.stream()
				.map(LineupHistory::of)
				.collect(Collectors.toList());

			lineupHistoryRepository.saveAll(lineupList);
			slice = lineupRepository.findAll(PageRequest.of(++page, size));
		}
		lineupRepository.deleteAll();
	}

	@Scheduled(cron = "0 0 22 * * *")
	@Transactional(readOnly = true)
	public void requestReview() {
		List<CustomerLineupInfoResponse> targetList = lineupRepository.findCustomerLineupInfoByIsReviewedFalse();
		for (CustomerLineupInfoResponse target : targetList) {
			String content = target.getName() + "님" + target.getRestaurantName() + "에 대해 리뷰를 작성해 주세요.";
			MessageRequest messageRequest = new MessageRequest(target.getName(), "WAITING CATCH", content);
			try {
				smsService.sendSms(messageRequest);
			} catch (JsonProcessingException | URISyntaxException | UnsupportedEncodingException |
					 NoSuchAlgorithmException | InvalidKeyException e) {
				throw new IllegalArgumentException(e);
			}
		}
	}
}