package team.waitingcatch.app.restaurant.service.requestblacklist;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.blacklist.RequestUserBlackListByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.entity.BlackListRequest;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.repository.BlackListRequestRepository;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class BlackListRequestServiceImpl implements BlackListRequestService, InternalBlackListRequestService {
	private final BlackListRequestRepository blackListRequestRepository;
	private final UserRepository userRepository;
	private final RestaurantRepository restaurantRepository;

	//판매자가 한명의 고객을 블랙리스트 요청을 한다.
	public void requestUserBlackList(
		RequestUserBlackListByRestaurantServiceRequest requestUserBlackListByRestaurantServiceRequest) {
		// 리팩토링 필요함
		//	User user = internalUserService._findById(requestUserBlackListByRestaurantServiceRequest.getUserId());
		User user = userRepository.findById(requestUserBlackListByRestaurantServiceRequest.getUserId()).orElseThrow();
		//Internal service 내부로직에서 중복체크를 해주면 여기서 따로 해줄 필요가 없음.
		// 리팩토링 필요함
		//Restaurant restaurant = internalRestaurantService._findBySellerName(
		// 	requestUserBlackListByRestaurantServiceRequest.getSellerName());

		Restaurant restaurant = restaurantRepository.findByUser_Username(
			requestUserBlackListByRestaurantServiceRequest.getSellerName());

		BlackListRequest blackListRequest = new BlackListRequest(restaurant, user,
			requestUserBlackListByRestaurantServiceRequest.getDescription());
		blackListRequestRepository.save(blackListRequest);
	}

}
