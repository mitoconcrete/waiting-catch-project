package team.waitingcatch.app.redis.service;

import static team.waitingcatch.app.exception.ErrorCode.*;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.util.sms.SmsService;
import team.waitingcatch.app.common.util.sms.dto.MessageRequest;
import team.waitingcatch.app.redis.entity.ValidCode;
import team.waitingcatch.app.redis.repository.ValidCodeRepository;
import team.waitingcatch.app.user.controller.CreateValidCodeResponse;
import team.waitingcatch.app.user.dto.CheckValidCodeRequest;
import team.waitingcatch.app.user.dto.CreateValidCodeRequest;

@Service
@RequiredArgsConstructor
public class ValidCodeServiceImpl implements ValidCodeService {

	private final SmsService smsService;
	private final ValidCodeRepository validCodeRepository;

	private final long VALID_CODE_LIVE_TIME = 1000 * 60 * 5L;

	@Override
	public CreateValidCodeResponse createValidCodeByPhoneNumber(CreateValidCodeRequest payload) throws
		UnsupportedEncodingException,
		URISyntaxException,
		NoSuchAlgorithmException,
		InvalidKeyException,
		JsonProcessingException {
		Random random = new Random();
		int validCode = random.nextInt(900) + 100;
		ValidCode newValidCode;
		Optional<ValidCode> pastValidCode = validCodeRepository.findById(payload.getPhoneNumber());

		// 기존 것이 있다면, 연장 및 업데이트
		if (pastValidCode.isPresent()) {
			newValidCode = pastValidCode.get();
			newValidCode.updateValidCode(validCode, VALID_CODE_LIVE_TIME);
		}
		// 새롭게 만들어야 하는 상황이라면, 새롭게 만들어준다.
		else {
			newValidCode = new ValidCode(payload.getPhoneNumber(), validCode, VALID_CODE_LIVE_TIME);
		}

		// 인증번호를 보낸다.
		String content =
			"[인증번호]" + System.lineSeparator() + "아래의 번호를 입력하세요. :" + System.lineSeparator() + "" + validCode + "";
		MessageRequest messageRequest = new MessageRequest(payload.getPhoneNumber(), "Waiting Catch", content);
		smsService.sendSms(messageRequest);

		long expireTime = validCodeRepository.save(newValidCode).getExpiration();
		return new CreateValidCodeResponse(expireTime);
	}

	@Override
	public void checkValidCode(CheckValidCodeRequest payload) {
		ValidCode validCode = validCodeRepository.findById(payload.getPhoneNumber()).orElseThrow(
			() -> new NoSuchElementException(INVALID_VALID_CODE.getMessage()));
		if (!validCode.isMatchValidCode(payload.getValidCode())) {
			throw new IllegalArgumentException(INCORRECT_VALID_CODE.getMessage());
		}

		// 인증이 끝났다면, 제거해준다.
		validCodeRepository.delete(validCode);
	}
}
