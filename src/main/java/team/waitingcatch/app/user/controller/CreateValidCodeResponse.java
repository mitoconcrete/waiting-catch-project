package team.waitingcatch.app.user.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateValidCodeResponse {
	private final long remainTime;
}
