package team.waitingcatch.app.restaurant.service.requestblacklist;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.blacklist.ApproveBlackListServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CancelBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetRequestBlacklistResponse;
import team.waitingcatch.app.restaurant.dto.blacklist.DemandBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.entity.BlacklistRequest;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.repository.BlacklistRequestRepository;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;
import team.waitingcatch.app.restaurant.service.blacklist.InternalBlacklistService;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.enums.UserRoleEnum;
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
			.orElseThrow(() -> new IllegalArgumentException("Not found restaurant"));

		BlacklistRequest blackListRequest = new BlacklistRequest(restaurant, user, serviceRequest.getDescription());
		blacklistRequestRepository.save(blackListRequest);
	}

	//판매자가 요청한 한명의 고객의 블랙리스트 요청을 판매자가 취소한다.
	public void cancelRequestUserBlacklist(
		CancelBlacklistByRestaurantServiceRequest serviceRequest) {
		BlacklistRequest blackListRequest = blacklistRequestRepository.findByIdWithRestaurant(
				serviceRequest.getBlacklistId())
			.orElseThrow(() -> new IllegalArgumentException("Not found request blacklist"));

		if (!blackListRequest.getUser().getId().equals(serviceRequest.getSellerId())) {
			throw new IllegalArgumentException("잘못된 접근입니다.");
		}
		blackListRequest.checkWaitingStatus();
		blackListRequest.updateCancelStatus();
	}

	//관리자 부분

	//블랙리스트 요청 전체조회
	@Transactional(readOnly = true)
	public List<GetRequestBlacklistResponse> getRequestBlacklist() {
		List<BlacklistRequest> blacklistRequests = blacklistRequestRepository.findAll();
		return blacklistRequests.stream().map(GetRequestBlacklistResponse::new).collect(Collectors.toList());
	}

	public void approveBlacklistRequest(ApproveBlackListServiceRequest serviceRequest) {
		BlacklistRequest blackListRequest = blacklistRequestRepository.findById(serviceRequest.getBlacklistRequestId())
			.orElseThrow(() -> new IllegalArgumentException("Not found black list request"));
		blackListRequest.checkWaitingStatus();
		blackListRequest.updateApprovalStatus();
		internalBlackListService._createBlackList(blackListRequest.getRestaurant(), blackListRequest.getUser());
	}

}