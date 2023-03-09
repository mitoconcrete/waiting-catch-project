package team.waitingcatch.app.redis.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;

import team.waitingcatch.app.exception.ErrorCode;
import team.waitingcatch.app.redis.repository.ValidCodeRepository;
import team.waitingcatch.app.user.dto.CheckValidCodeRequest;
import team.waitingcatch.app.user.dto.CreateValidCodeRequest;

@SpringBootTest
class ValidCodeServiceImplTest {

	@Autowired
	private ValidCodeService validCodeService;

	@Autowired
	private ValidCodeRepository validCodeRepository;

	@Test
	@DisplayName("휴대폰 번호를 이용하여 인증번호를 생성한뒤, 검증에 성공합니다.")
	void successCase() throws
		UnsupportedEncodingException,
		URISyntaxException,
		NoSuchAlgorithmException,
		InvalidKeyException,
		JsonProcessingException {
		// given
		String phoneNumber = "010-9789-0726";
		var createPayload = mock(CreateValidCodeRequest.class);
		when(createPayload.getPhoneNumber()).thenReturn(phoneNumber);
		var checkPayload = mock(CheckValidCodeRequest.class);
		when(checkPayload.getPhoneNumber()).thenReturn(phoneNumber);

		validCodeService.createValidCodeByPhoneNumber(createPayload);
		var instance = validCodeRepository.findById(createPayload.getPhoneNumber()).get();

		when(checkPayload.getValidCode()).thenReturn(instance.getValidCode());

		// when
		validCodeService.checkValidCode(checkPayload);

		// then
		assertFalse(validCodeRepository.existsById(checkPayload.getPhoneNumber()));
	}

	@Test
	@DisplayName("휴대폰 번호를 이용하여 인증번호를 생성한뒤, 다른 인증번호를 입력하여 검증에 실패합니다.")
	void failCase() throws
		UnsupportedEncodingException,
		URISyntaxException,
		NoSuchAlgorithmException,
		InvalidKeyException,
		JsonProcessingException {
		// given
		String phoneNumber = "010-9789-0726";
		var createPayload = mock(CreateValidCodeRequest.class);
		when(createPayload.getPhoneNumber()).thenReturn(phoneNumber);
		var checkPayload = mock(CheckValidCodeRequest.class);
		when(checkPayload.getPhoneNumber()).thenReturn(phoneNumber);

		validCodeService.createValidCodeByPhoneNumber(createPayload);
		var instance = validCodeRepository.findById(createPayload.getPhoneNumber()).get();

		when(checkPayload.getValidCode()).thenReturn(instance.getValidCode() - 1);

		// when & then
		assertThatThrownBy(() -> validCodeService.checkValidCode(checkPayload)).isInstanceOf(
			IllegalArgumentException.class).hasMessage(ErrorCode.INCORRECT_VALID_CODE.getMessage());
	}

	@Test
	@DisplayName("휴대폰 번호를 이용하여 인증번호를 생성한뒤, 다른 번호를 재입력하여 검증이 중단됩니다.")
	void failCase2() throws
		UnsupportedEncodingException,
		URISyntaxException,
		NoSuchAlgorithmException,
		InvalidKeyException,
		JsonProcessingException {
		String phoneNumber1 = "010-9789-0726";
		String phoneNumber2 = "010-9789-0727";
		var createPayload = mock(CreateValidCodeRequest.class);
		when(createPayload.getPhoneNumber()).thenReturn(phoneNumber1);
		var checkPayload = mock(CheckValidCodeRequest.class);
		when(checkPayload.getPhoneNumber()).thenReturn(phoneNumber2);

		validCodeService.createValidCodeByPhoneNumber(createPayload);
		var instance = validCodeRepository.findById(createPayload.getPhoneNumber()).get();

		when(checkPayload.getValidCode()).thenReturn(instance.getValidCode());
		// when & then
		assertThatThrownBy(() -> validCodeService.checkValidCode(checkPayload)).isInstanceOf(
			NoSuchElementException.class).hasMessage(ErrorCode.NOT_FOUND_VALID_CODE.getMessage());

		// 아직 지워지지 않았기에, DB에는 계속 남아있음.
		assertTrue(validCodeRepository.existsById(phoneNumber1));
	}
}