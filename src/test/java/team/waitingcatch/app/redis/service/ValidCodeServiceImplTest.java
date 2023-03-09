package team.waitingcatch.app.redis.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import team.waitingcatch.app.user.dto.CheckValidCodeRequest;
import team.waitingcatch.app.user.dto.CreateValidCodeRequest;

@SpringBootTest
class ValidCodeServiceImplTest {

	@Autowired
	private ValidCodeService validCodeService;

	@Test
	@DisplayName("휴대폰 번호를 이용하여 인증번호를 생성합니다.")
	void successCase() {
		// given
		String phoneNumber = "010-9789-0726";
		var createPayload = mock(CreateValidCodeRequest.class);
		when(createPayload.getPhoneNumber()).thenReturn(phoneNumber);
		var checkPayload = mock(CheckValidCodeRequest.class);
		when(checkPayload.getPhoneNumber()).thenReturn(phoneNumber);

		assertThatThrownBy(() -> validCodeService.createValidCodeByPhoneNumber(createPayload))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("Empty key");
	}
}