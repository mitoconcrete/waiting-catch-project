package team.waitingcatch.app.restaurant.service.blacklist;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.blacklist.CreateBlackListInternalServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.DeleteUserBlackListByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlackListByRestaurantIdServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlackListResponse;
import team.waitingcatch.app.restaurant.entity.BlackList;
import team.waitingcatch.app.restaurant.entity.BlackListRequest;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.repository.BlackListRepository;
import team.waitingcatch.app.restaurant.repository.BlackListRequestRepository;
import team.waitingcatch.app.restaurant.service.restaurant.InternalRestaurantService;
import team.waitingcatch.app.user.entitiy.User;

@Service
@RequiredArgsConstructor
@Transactional
public class BlackListServiceImpl implements BlackListService, InternalBlackListService {
	private final BlackListRepository blackListRepository;
	private final InternalRestaurantService internalRestaurantService;

	private final BlackListRequestRepository blackListRequestRepository;

	public void _createBlackList(
		Restaurant restaurant, User user) {

		blackListRepository.findByUserIdAndRestaurantUserIdAndIsDeletedFalse(
			user.getId(), restaurant.getUser().getId()).ifPresent(a -> {
			throw new IllegalArgumentException("이미 차단된 사용자 입니다");
		});

		CreateBlackListInternalServiceRequest createBlackListInternalServiceRequest
			= new CreateBlackListInternalServiceRequest(restaurant, user);
		BlackList newBlackList = new BlackList(createBlackListInternalServiceRequest);
		blackListRepository.save(newBlackList);
	}

	public void deleteUserBlackListByRestaurant(
		DeleteUserBlackListByRestaurantServiceRequest deleteUserBlackListByRestaurantServiceRequest) {
		BlackList blackList = blackListRepository.findByIdAndRestaurantUserId(
			deleteUserBlackListByRestaurantServiceRequest.getBlacklistId(),
			deleteUserBlackListByRestaurantServiceRequest.getSellerId()
		).orElseThrow(() -> new IllegalArgumentException("Not found blacklist user"));
		if (blackList.isDeleted()) {
			throw new IllegalArgumentException("이미 블랙리스트에서 삭제된 고객입니다. 블랙리스트를 원하시면 다시 신청해주세요.");
		}
		BlackListRequest blackListRequest = blackListRequestRepository.findByUser_IdAndRestaurant_User_IdAndStatus(
			blackList.getUser().getId(), blackList.getRestaurant()
				.getId());
		blackList.checkDeleteStatus();
		blackList.deleteSuccess();
		blackListRequest.updateCancelStatus();
	}

	@Transactional(readOnly = true)
	public List<GetBlackListResponse> getBlackListByRestaurantIdRequest(
		GetBlackListByRestaurantIdServiceRequest getBlackListByRestaurantIdServiceRequest) {
		Restaurant restaurant = internalRestaurantService._getRestaurantByUserId(
			getBlackListByRestaurantIdServiceRequest.getSellerId());
		List<BlackList> blackList = blackListRepository.findAllByRestaurant(
			restaurant);
		return blackList.stream().map(GetBlackListResponse::new).collect(Collectors.toList());
	}
}
