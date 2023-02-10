package team.waitingcatch.app.restaurant.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.service.BlackListService;
import team.waitingcatch.app.restaurant.service.InnerBlackListService;
import team.waitingcatch.app.restaurant.service.InnerSellerManagementService;
import team.waitingcatch.app.restaurant.service.SellerManagementService;

@Service
@RequiredArgsConstructor
@Transactional
public class SellerManagementServiceImpl implements SellerManagementService, InnerSellerManagementService {
}
