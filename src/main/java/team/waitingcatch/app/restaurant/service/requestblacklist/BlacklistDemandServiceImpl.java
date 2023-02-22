package team.waitingcatch.app.restaurant.service.requestblacklist;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.blacklist.ApproveBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CancelBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CreateBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlackListDemandByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistDemandResponse;
import team.waitingcatch.app.restaurant.entity.BlacklistDemand;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum;
import team.waitingcatch.app.restaurant.repository.BlacklistDemandRepository;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;
import team.waitingcatch.app.restaurant.service.blacklist.InternalBlacklistService;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class BlacklistDemandServiceImpl implements BlacklistDemandService, InternalBlacklistDemandService {
	private final BlacklistDemandRepository blacklistDemandRepository;
	private final UserRepository userRepository;
	private final RestaurantRepository restaurantRepository;

	private final InternalBlacklistService internalBlackListService;

	//판매자가 한명의 고객을 블랙리스트 요청을 한다.
	@Override
	public void submitBlacklistDemand(CreateBlacklistDemandServiceRequest serviceRequest) {
		User user = userRepository.findById(serviceRequest.getUserId()).orElseThrow();

		List<BlacklistDemand> blacklistDemandList = blacklistDemandRepository.findByUser_IdAndRestaurant_User_Id(
			serviceRequest.getUserId(), serviceRequest.getSellerId());

		if (blacklistDemandList.size() >= 1) {
			for (BlacklistDemand blacklistDemand : blacklistDemandList) {
				blacklistDemand.checkBlacklistRequest();
			}
		}

		Restaurant restaurant = restaurantRepository.findByUserId(serviceRequest.getSellerId())
			.orElseThrow(() -> new IllegalArgumentException("Not found restaurant"));

		BlacklistDemand blackListDemand = new BlacklistDemand(restaurant, user, serviceRequest.getDescription());
		blacklistDemandRepository.save(blackListDemand);
	}

	//판매자가 요청한 한명의 고객의 블랙리스트 요청을 판매자가 취소한다.
	@Override
	public void cancelBlacklistDemand(CancelBlacklistDemandServiceRequest serviceRequest) {
		BlacklistDemand blacklistDemand = blacklistDemandRepository.findByIdAndRestaurantUserId(
				serviceRequest.getBlacklistDemandId(), serviceRequest.getSellerId())
			.orElseThrow(() -> new IllegalArgumentException("Not found black list request"));
		blacklistDemand.checkWaitingStatus();
		blacklistDemand.updateCancelStatus();
	}

	@Transactional(readOnly = true)
	@Override
	public List<GetBlacklistDemandResponse> getBlacklistDemands() {
		List<BlacklistDemand> blacklistDemands = blacklistDemandRepository.findAllByStatus(AcceptedStatusEnum.WAIT);
		return blacklistDemands.stream().map(GetBlacklistDemandResponse::new).collect(Collectors.toList());
	}

	@Override
	public void approveBlacklistDemand(ApproveBlacklistDemandServiceRequest serviceRequest) {
		BlacklistDemand blacklistDemand = blacklistDemandRepository.findById(serviceRequest.getBlacklistDemandId())
			.orElseThrow(() -> new IllegalArgumentException("Not found black list request"));
		blacklistDemand.checkWaitingStatus();
		blacklistDemand.updateApprovalStatus();
		internalBlackListService._createBlackList(blacklistDemand.getRestaurant(), blacklistDemand.getUser());
	}

	@Override
	public void rejectBlacklistDemand(Long blacklistDemandId) {
		BlacklistDemand blacklistDemand = blacklistDemandRepository.findById(blacklistDemandId)
			.orElseThrow(() -> new IllegalArgumentException("Not found blacklist request"));
		blacklistDemand.checkWaitingStatus();
		blacklistDemand.updateRejectionStatus();
	}

	@Override
	@Transactional(readOnly = true)
	public List<GetBlacklistDemandResponse> getBlackListDemandsByRestaurant(
		GetBlackListDemandByRestaurantServiceRequest getBlackListDemandByRestaurantControllerRequest) {
		Restaurant restaurant = restaurantRepository.findByUserId(
				getBlackListDemandByRestaurantControllerRequest.getSellerId())
			.orElseThrow(() -> new IllegalArgumentException("Not found restaurant request"));
		List<BlacklistDemand> blacklistDemands = blacklistDemandRepository.findAllByRestaurant_Id(restaurant.getId());

		return blacklistDemands.stream().map(GetBlacklistDemandResponse::new).collect(Collectors.toList());
	}
}