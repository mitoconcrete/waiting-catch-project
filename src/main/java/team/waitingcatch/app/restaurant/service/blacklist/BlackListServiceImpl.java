package team.waitingcatch.app.restaurant.service.blacklist;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BlackListServiceImpl implements BlackListService, InternalBlackListService {
}
