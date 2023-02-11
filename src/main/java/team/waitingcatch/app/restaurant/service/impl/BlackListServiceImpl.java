package team.waitingcatch.app.restaurant.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.service.BlackListService;
import team.waitingcatch.app.restaurant.service.InternalBlackListService;

@Service
@RequiredArgsConstructor
@Transactional
public class BlackListServiceImpl implements BlackListService, InternalBlackListService {
}
