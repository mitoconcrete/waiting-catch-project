package team.waitingcatch.app.redis.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.redis.repository.DeadAccessTokenRepository;

@Service
@RequiredArgsConstructor
public class DeadAccessTokenServiceImpl implements DeadAccessTokenService, InternalDeadAccessTokenService {
	private final DeadAccessTokenRepository deadAccessTokenRepository;
}
