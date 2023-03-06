package team.waitingcatch.app.restaurant.service.blacklist;

import static team.waitingcatch.app.exception.ErrorCode.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.exception.DuplicateRequestException;
import team.waitingcatch.app.restaurant.dto.blacklist.CreateBlacklistInternalServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.DeleteBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlackListByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistByRestaurantIdServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistResponse;
import team.waitingcatch.app.restaurant.entity.Blacklist;
import team.waitingcatch.app.restaurant.entity.BlacklistDemand;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.repository.BlacklistDemandRepository;
import team.waitingcatch.app.restaurant.repository.BlacklistRepository;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;
import team.waitingcatch.app.restaurant.service.restaurant.InternalRestaurantService;
import team.waitingcatch.app.user.entitiy.User;

@Service
@RequiredArgsConstructor
@Transactional
public class BlacklistServiceImpl implements BlacklistService, InternalBlacklistService {
	private final BlacklistRepository blacklistRepository;
	private final InternalRestaurantService internalRestaurantService;
	private final RestaurantRepository restaurantRepository;
	private final BlacklistDemandRepository blacklistDemandRepository;

	@Override
	public void deleteBlacklistByRestaurant(DeleteBlacklistByRestaurantServiceRequest serviceRequest) {
		Blacklist blacklist = blacklistRepository.findByIdAndRestaurantUserId(
			serviceRequest.getBlacklistId(),
			serviceRequest.getSellerId()
		).orElseThrow(() -> new NoSuchElementException(NOT_FOUND_BLACKLIST.getMessage()));

		if (blacklist.isDeleted()) {
			throw new DuplicateRequestException(ALREADY_DELETED_BLACKLIST);
		}

		BlacklistDemand blacklistDemand = blacklistDemandRepository.findByUser_IdAndRestaurant_User_IdAndStatusApproval(
				blacklist.getUser().getId(), blacklist.getRestaurant().getUser().getId())
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_BLACKLIST_DEMAND.getMessage()));

		blacklist.checkDeleteStatus();
		blacklist.deleteSuccess();
		blacklistDemand.updateCancelStatus();
	}

	@Transactional(readOnly = true)
	@Override
	public List<GetBlacklistResponse> getBlackListByRestaurantId(
		GetBlacklistByRestaurantIdServiceRequest serviceRequest) {

		Restaurant restaurant = internalRestaurantService._getRestaurantById(serviceRequest.getRestaurantId());
		List<Blacklist> blackList = blacklistRepository.findAllByRestaurant(restaurant);
		return blackList.stream().map(GetBlacklistResponse::new).collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	@Override
	public Page<GetBlacklistResponse> getBlacklist(Pageable payload) {
		Page<Blacklist> result = blacklistRepository.findAllByIsDeletedFalse(payload);
		return new PageImpl<>(result.getContent().stream().map(GetBlacklistResponse::new).collect(Collectors.toList()),
			payload, result.getTotalElements());
	}

	@Transactional(readOnly = true)
	@Override
	public List<GetBlacklistResponse> getBlackListByRestaurant(
		GetBlackListByRestaurantServiceRequest getBlackListByRestaurantServiceRequest) {
		Restaurant restaurant = restaurantRepository.findById(getBlackListByRestaurantServiceRequest.getSellerId())
			.orElseThrow(() -> new NoSuchElementException(NOT_FOUND_RESTAURANT.getMessage()));
		List<Blacklist> blackList = blacklistRepository.findAllByRestaurant_Id(restaurant.getId());
		return blackList.stream().map(GetBlacklistResponse::new).collect(Collectors.toList());
	}

	@Override
	public boolean _existsByRestaurantIdAndUserId(Long restaurantId, Long userId) {
		return blacklistRepository.findByRestaurantIdAndUserIdAndIsDeletedFalse(restaurantId, userId).isPresent();
	}

	@Override
	public void _createBlackList(Restaurant restaurant, User user) {
		blacklistRepository.findByUserIdAndRestaurantUserIdAndIsDeletedFalse(user.getId(), restaurant.getUser().getId())
			.ifPresent(blacklist -> {
				throw new DuplicateRequestException(ALREADY_BANNED_USER);
			});

		var serviceRequest = new CreateBlacklistInternalServiceRequest(restaurant, user);
		Blacklist newBlackList = new Blacklist(serviceRequest);
		blacklistRepository.save(newBlackList);
	}
}