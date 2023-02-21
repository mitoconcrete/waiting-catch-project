package team.waitingcatch.app.restaurant.service.blacklist;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.blacklist.CreateBlacklistInternalServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.DeleteUserBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistByRestaurantIdServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistResponse;
import team.waitingcatch.app.restaurant.entity.BlackList;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.repository.BlackListRepository;
import team.waitingcatch.app.user.entitiy.User;

@Service
@RequiredArgsConstructor
@Transactional
public class BlackListServiceImpl implements BlackListService, InternalBlackListService {
	private final BlackListRepository blackListRepository;

	public void _createBlackList(Restaurant restaurant, User user) {
		var serviceRequest = new CreateBlacklistInternalServiceRequest(restaurant, user);
		BlackList blackList = new BlackList(serviceRequest);
		blackListRepository.save(blackList);
	}

	public void deleteUserBlackListByRestaurant(DeleteUserBlacklistByRestaurantServiceRequest serviceRequest) {
		BlackList blackList = blackListRepository.findByIdWithRestaurant(serviceRequest.getBlacklistId())
			.orElseThrow(() -> new IllegalArgumentException("Not found blacklist user"));

		if (!blackList.isSameRequester(serviceRequest.getSellerId())) {
			throw new IllegalArgumentException("잘못된 접근입니다.");
		}
		blackList.checkDeleteStatus();
		blackList.deleteSuccess();
	}

	@Transactional(readOnly = true)
	public List<GetBlacklistResponse> getBlackListByRestaurantIdRequest(
		GetBlacklistByRestaurantIdServiceRequest serviceRequest) {

		List<BlackList> blackList = blackListRepository.findAllByRestaurant_Id(serviceRequest.getRestaurantId());
		return blackList.stream().map(GetBlacklistResponse::new).collect(Collectors.toList());
	}
}