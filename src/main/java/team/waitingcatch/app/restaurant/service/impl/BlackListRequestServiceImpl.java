package team.waitingcatch.app.restaurant.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.service.BlackListRequestService;
import team.waitingcatch.app.restaurant.service.BlackListService;
import team.waitingcatch.app.restaurant.service.InnerBlackListRequestService;

@Service
@RequiredArgsConstructor
@Transactional
public class BlackListRequestServiceImpl implements BlackListRequestService, InnerBlackListRequestService {
}
