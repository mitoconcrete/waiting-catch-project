package team.waitingcatch.app.restaurant.service.blacklist;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.blacklist.CreateBlackListInternalServiceRequest;
import team.waitingcatch.app.restaurant.entity.BlackList;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.repository.BlackListRepository;
import team.waitingcatch.app.user.entitiy.User;

@Service
@RequiredArgsConstructor
@Transactional
public class BlackListServiceImpl implements BlackListService, InternalBlackListService {
	private final BlackListRepository blackListRepository;

	public void _createBlackList(
		Restaurant restaurant, User user) {
		CreateBlackListInternalServiceRequest createBlackListInternalServiceRequest
			= new CreateBlackListInternalServiceRequest(restaurant, user);
		BlackList blackList = new BlackList(createBlackListInternalServiceRequest);
		blackListRepository.save(blackList);
	}
}
