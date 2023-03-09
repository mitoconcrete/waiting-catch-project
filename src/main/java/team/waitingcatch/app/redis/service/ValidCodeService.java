package team.waitingcatch.app.redis.service;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.fasterxml.jackson.core.JsonProcessingException;

import team.waitingcatch.app.user.controller.CreateValidCodeResponse;
import team.waitingcatch.app.user.dto.CheckValidCodeRequest;
import team.waitingcatch.app.user.dto.CreateValidCodeRequest;

public interface ValidCodeService {
	CreateValidCodeResponse createValidCodeByPhoneNumber(CreateValidCodeRequest payload) throws
		UnsupportedEncodingException,
		URISyntaxException,
		NoSuchAlgorithmException,
		InvalidKeyException,
		JsonProcessingException;

	void checkValidCode(CheckValidCodeRequest payload);
}
