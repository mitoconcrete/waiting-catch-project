package team.waitingcatch.app.restaurant.service.requestblacklist;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.blacklist.ApproveBlackListServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CancelRequestUserBlackListByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetRequestBlackListResponse;
import team.waitingcatch.app.restaurant.dto.blacklist.RequestUserBlackListByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.entity.BlackListRequest;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.repository.BlackListRequestRepository;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;
import team.waitingcatch.app.restaurant.service.blacklist.InternalBlackListService;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.enums.UserRoleEnum;
import team.waitingcatch.app.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class BlackListRequestServiceImpl implements BlackListRequestService, InternalBlackListRequestService {
	private final BlackListRequestRepository blackListRequestRepository;
	private final UserRepository userRepository;
	private final RestaurantRepository restaurantRepository;
	private final InternalBlackListService internalBlackListService;

	//판매자가 한명의 고객을 블랙리스트 요청을 한다.
	public void requestUserBlackList(
		RequestUserBlackListByRestaurantServiceRequest requestUserBlackListByRestaurantServiceRequest) {
		// 리팩토링 필요함
		//	User user = internalUserService._findById(requestUserBlackListByRestaurantServiceRequest.getUserId());
		User user = userRepository.findById(requestUserBlackListByRestaurantServiceRequest.getUserId()).orElseThrow();
		if (user.getRole() == UserRoleEnum.SELLER) {
			throw new IllegalArgumentException("잘못된 요청입니다.");
		}
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

	//판매자가 요청한 한명의 고객의 블랙리스트 요청을 판매자가 취소한다.
	public void cancelRequestUserBlackList(
		CancelRequestUserBlackListByRestaurantServiceRequest cancelRequestUserBlackListByRestaurantServiceRequest) {
		BlackListRequest blackListRequest = blackListRequestRepository.findByUser_IdAndRestaurant_User_Username(
				cancelRequestUserBlackListByRestaurantServiceRequest.getUserId(),
				cancelRequestUserBlackListByRestaurantServiceRequest.getSellerName())
			.orElseThrow(() -> new IllegalArgumentException("Not found request blacklist"));
		blackListRequest.checkWaitingStatus();
		blackListRequest.updateCancelStatus();
	}

	//관리자 부분

	//블랙리스트 요청 전체조회
	@Transactional(readOnly = true)
	public List<GetRequestBlackListResponse> getRequestBlackLists() {
		List<BlackListRequest> blackListRequests = blackListRequestRepository.findAll();
		return blackListRequests.stream().map(GetRequestBlackListResponse::new).collect(Collectors.toList());
	}

	public void approveBlackListRequest(ApproveBlackListServiceRequest approveBlackListServiceRequest) {
		BlackListRequest blackListRequest = blackListRequestRepository.findById(
			approveBlackListServiceRequest.getBlackListRequestId()).orElseThrow(
			() -> new IllegalArgumentException("Not found black list request"));
		blackListRequest.checkWaitingStatus();
		blackListRequest.updateApprovalStatus();
		internalBlackListService._createBlackList(
			blackListRequest.getRestaurant(), blackListRequest.getUser());
	}

}
