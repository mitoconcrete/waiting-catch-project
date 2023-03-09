package team.waitingcatch.app.redis.entity;

import java.util.concurrent.TimeUnit;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import lombok.Getter;

@Getter
@RedisHash("valid-code")
public class ValidCode {
	@Id
	private String phoneNumber;
	private int validCode;
	@TimeToLive(unit = TimeUnit.MILLISECONDS)
	private long expiration;

	public ValidCode(String phoneNumber, int validCode, long expiration) {
		this.phoneNumber = phoneNumber;
		this.validCode = validCode;
		this.expiration = expiration;
	}

	public void updateValidCode(int validCode, long expiration) {
		this.validCode = validCode;
		this.expiration = expiration;
	}

	public boolean isMatchValidCode(int validCode) {
		return this.validCode == validCode;
	}
}
