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
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.repository.BlacklistRepository;
import team.waitingcatch.app.user.entitiy.User;

@Service
@RequiredArgsConstructor
@Transactional
public class BlacklistServiceImpl implements BlacklistService, InternalBlacklistService {
	private final BlacklistRepository blacklistRepository;

	public void _createBlackList(Restaurant restaurant, User user) {
		var serviceRequest = new CreateBlacklistInternalServiceRequest(restaurant, user);
		Blacklist blackList = new Blacklist(serviceRequest);
		blacklistRepository.save(blackList);
	}

	public void deleteBlacklistByRestaurant(DeleteBlacklistByRestaurantServiceRequest serviceRequest) {
		Blacklist blacklist = blacklistRepository.findByIdWithRestaurant(serviceRequest.getBlacklistId())
			.orElseThrow(() -> new IllegalArgumentException("Not found blacklist user"));

		if (!blacklist.isSameRequester(serviceRequest.getSellerId())) {
			throw new IllegalArgumentException("잘못된 접근입니다.");
		}
		blacklist.checkDeleteStatus();
		blacklist.deleteSuccess();
	}

	@Transactional(readOnly = true)
	public List<GetBlacklistResponse> getBlacklistByRestaurantIdRequest(
		GetBlacklistByRestaurantIdServiceRequest serviceRequest) {

		List<Blacklist> blacklist = blacklistRepository.findAllByRestaurant_Id(serviceRequest.getRestaurantId());
		return blacklist.stream().map(GetBlacklistResponse::new).collect(Collectors.toList());
	}
}