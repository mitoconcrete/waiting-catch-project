package team.waitingcatch.app.restaurant.service.blacklist;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.blacklist.CreateBlacklistInternalServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.DeleteBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistByRestaurantIdServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistResponse;
import team.waitingcatch.app.restaurant.entity.Blacklist;
import team.waitingcatch.app.restaurant.entity.BlacklistDemand;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.repository.BlacklistDemandRepository;
import team.waitingcatch.app.restaurant.repository.BlacklistRepository;
import team.waitingcatch.app.restaurant.service.restaurant.InternalRestaurantService;
import team.waitingcatch.app.user.entitiy.User;

@Service
@RequiredArgsConstructor
@Transactional
public class BlacklistServiceImpl implements BlacklistService, InternalBlacklistService {
	private final BlacklistRepository blacklistRepository;
	private final InternalRestaurantService internalRestaurantService;

	private final BlacklistDemandRepository blacklistDemandRepository;

	@Override
	public void deleteBlacklistByRestaurant(DeleteBlacklistByRestaurantServiceRequest serviceRequest) {
		Blacklist blacklist = blacklistRepository.findByIdAndRestaurantUserId(
			serviceRequest.getBlacklistId(),
			serviceRequest.getSellerId()
		).orElseThrow(() -> new IllegalArgumentException("Not found blacklist user"));

		if (blacklist.isDeleted()) {
			throw new IllegalArgumentException("이미 블랙리스트에서 삭제된 고객입니다. 블랙리스트를 원하시면 다시 신청해주세요.");
		}

		BlacklistDemand blacklistDemand = blacklistDemandRepository.findByUser_IdAndRestaurant_User_IdAndStatusApproval(
				blacklist.getUser().getId(), blacklist.getRestaurant().getUser().getId())
			.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 블랙리스트 요청입니다."));

		blacklist.checkDeleteStatus();
		blacklist.deleteSuccess();
		blacklistDemand.updateCancelStatus();
	}

	@Transactional(readOnly = true)
	@Override
	public List<GetBlacklistResponse> getBlackListByRestaurantId(
		GetBlacklistByRestaurantIdServiceRequest serviceRequest) {

		Restaurant restaurant = internalRestaurantService._getById(serviceRequest.getRestaurantId());
		List<Blacklist> blackList = blacklistRepository.findAllByRestaurant(restaurant);
		return blackList.stream().map(GetBlacklistResponse::new).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Override
	public List<GetBlacklistResponse> getBlacklist() {
		return blacklistRepository.findAllByIsDeletedFalse().stream()
			.map(GetBlacklistResponse::new)
			.collect(Collectors.toList());
	}

	@Override
	public void _createBlackList(Restaurant restaurant, User user) {
		blacklistRepository.findByUserIdAndRestaurantUserIdAndIsDeletedFalse(user.getId(), restaurant.getUser().getId())
			.ifPresent(b -> {
				throw new IllegalArgumentException("이미 차단된 사용자 입니다");
			});

		var serviceRequest = new CreateBlacklistInternalServiceRequest(restaurant, user);
		Blacklist newBlackList = new Blacklist(serviceRequest);
		blacklistRepository.save(newBlackList);
	}
}