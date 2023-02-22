package team.waitingcatch.app.restaurant.service.requestblacklist;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.blacklist.DemandBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetRequestBlacklistResponse;
import team.waitingcatch.app.restaurant.entity.BlackListRequest;
import team.waitingcatch.app.restaurant.entity.BlacklistRequest;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum;
import team.waitingcatch.app.restaurant.repository.BlacklistRequestRepository;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;
import team.waitingcatch.app.restaurant.service.blacklist.InternalBlacklistService;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class BlacklistRequestServiceImpl implements BlacklistRequestService, InternalBlacklistRequestService {
	private final BlacklistRequestRepository blacklistRequestRepository;
	private final UserRepository userRepository;
	private final RestaurantRepository restaurantRepository;
	private final InternalBlacklistService internalBlackListService;

	//판매자가 한명의 고객을 블랙리스트 요청을 한다.
<<<<<<< HEAD:src/main/java/team/waitingcatch/app/restaurant/service/requestblacklist/BlacklistRequestServiceImpl.java
	public void requestUserBlackList(DemandBlacklistByRestaurantServiceRequest serviceRequest) {
		// 리팩토링 필요함
		//	User user = internalUserService._findById(requestUserBlackListByRestaurantServiceRequest.getUserId());
		User user = userRepository.findById(serviceRequest.getUserId()).orElseThrow();
		if (user.getRole() == UserRoleEnum.SELLER) {
			throw new IllegalArgumentException("잘못된 요청입니다.");
		}
		//Internal service 내부로직에서 중복체크를 해주면 여기서 따로 해줄 필요가 없음.
		// 리팩토링 필요함
		//Restaurant restaurant = internalRestaurantService._findBySellerName(
		// 	requestUserBlackListByRestaurantServiceRequest.getSellerName());

		Restaurant restaurant = restaurantRepository.findByUserId(serviceRequest.getSellerId())
=======
	public void requestUserBlackList(
		RequestUserBlackListByRestaurantServiceRequest requestUserBlackListByRestaurantServiceRequest) {
		User user = userRepository.findById(requestUserBlackListByRestaurantServiceRequest.getUserId()).orElseThrow();

		List<BlackListRequest> blackListRequestCheck = blackListRequestRepository.findByUser_IdAndRestaurant_User_Id(
			requestUserBlackListByRestaurantServiceRequest.getUserId(),
			requestUserBlackListByRestaurantServiceRequest.getSellerId()
		);
		if (blackListRequestCheck.size() >= 1) {
			for (BlackListRequest blackListRequest : blackListRequestCheck) {
				blackListRequest.checkBlacklistRequest();
			}
		}

		Restaurant restaurant = restaurantRepository.findByUserId(
				requestUserBlackListByRestaurantServiceRequest.getSellerId())
>>>>>>> f46d3a924e096003ef2634f434cefc3978de57be:src/main/java/team/waitingcatch/app/restaurant/service/requestblacklist/BlackListRequestServiceImpl.java
			.orElseThrow(() -> new IllegalArgumentException("Not found restaurant"));

		BlacklistRequest blackListRequest = new BlacklistRequest(restaurant, user, serviceRequest.getDescription());
		blacklistRequestRepository.save(blackListRequest);
	}

	//판매자가 요청한 한명의 고객의 블랙리스트 요청을 판매자가 취소한다.
<<<<<<< HEAD:src/main/java/team/waitingcatch/app/restaurant/service/requestblacklist/BlacklistRequestServiceImpl.java
	public void cancelRequestUserBlacklist(
		CancelBlacklistByRestaurantServiceRequest serviceRequest) {
		BlacklistRequest blackListRequest = blacklistRequestRepository.findByIdWithRestaurant(
				serviceRequest.getBlacklistId())
			.orElseThrow(() -> new IllegalArgumentException("Not found request blacklist"));

		if (!blackListRequest.getUser().getId().equals(serviceRequest.getSellerId())) {
			throw new IllegalArgumentException("잘못된 접근입니다.");
		}
=======
	public void cancelRequestUserBlackList(
		CancelRequestBlackListBySellerServiceRequest cancelRequestBlackListBySellerServiceRequest) {
		BlackListRequest blackListRequest = blackListRequestRepository.findByIdAndRestaurantUserId(
				cancelRequestBlackListBySellerServiceRequest.getRequestBlackListId(),
				cancelRequestBlackListBySellerServiceRequest.getSellerId())
			.orElseThrow(() -> new IllegalArgumentException("Not found black list request"));
>>>>>>> f46d3a924e096003ef2634f434cefc3978de57be:src/main/java/team/waitingcatch/app/restaurant/service/requestblacklist/BlackListRequestServiceImpl.java
		blackListRequest.checkWaitingStatus();
		blackListRequest.updateCancelStatus();
	}

	//관리자 부분

	//블랙리스트 요청 전체조회
	@Transactional(readOnly = true)
<<<<<<< HEAD:src/main/java/team/waitingcatch/app/restaurant/service/requestblacklist/BlacklistRequestServiceImpl.java
	public List<GetRequestBlacklistResponse> getRequestBlacklist() {
		List<BlacklistRequest> blacklistRequests = blacklistRequestRepository.findAll();
		return blacklistRequests.stream().map(GetRequestBlacklistResponse::new).collect(Collectors.toList());
	}

	public void approveBlacklistRequest(ApproveBlackListServiceRequest serviceRequest) {
		BlacklistRequest blackListRequest = blacklistRequestRepository.findById(serviceRequest.getBlacklistRequestId())
=======
	public List<GetRequestBlacklistResponse> getRequestBlackLists() {
		List<BlackListRequest> blackListRequests = blackListRequestRepository.findAllByStatus(AcceptedStatusEnum.WAIT);
		return blackListRequests.stream().map(GetRequestBlacklistResponse::new).collect(Collectors.toList());
	}

	// 블랙리스트 요청 승인
	public void approveBlackListRequest(ApproveBlackListServiceRequest approveBlackListServiceRequest) {

		BlackListRequest blackListRequest = blackListRequestRepository.findById(
				approveBlackListServiceRequest.getBlackListRequestId())
>>>>>>> f46d3a924e096003ef2634f434cefc3978de57be:src/main/java/team/waitingcatch/app/restaurant/service/requestblacklist/BlackListRequestServiceImpl.java
			.orElseThrow(() -> new IllegalArgumentException("Not found black list request"));
		blackListRequest.checkWaitingStatus();
		blackListRequest.updateApprovalStatus();
		internalBlackListService._createBlackList(blackListRequest.getRestaurant(), blackListRequest.getUser());
	}

<<<<<<< HEAD:src/main/java/team/waitingcatch/app/restaurant/service/requestblacklist/BlacklistRequestServiceImpl.java
}
=======
	// 블랙리스트 요청 거절
	@Override
	public void rejectBlacklistRequest(Long blacklistRequestId) {
		BlackListRequest blackListRequest = blackListRequestRepository.findById(blacklistRequestId)
			.orElseThrow(() -> new IllegalArgumentException("Not found blacklist request"));
		blackListRequest.checkWaitingStatus();
		blackListRequest.updateRejectionStatus();
	}

}
>>>>>>> f46d3a924e096003ef2634f434cefc3978de57be:src/main/java/team/waitingcatch/app/restaurant/service/requestblacklist/BlackListRequestServiceImpl.java