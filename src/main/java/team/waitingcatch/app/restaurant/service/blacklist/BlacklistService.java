package team.waitingcatch.app.restaurant.service.blacklist;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import team.waitingcatch.app.restaurant.dto.blacklist.DeleteBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlackListByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistByRestaurantIdServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistResponse;

public interface BlacklistService {
	void deleteBlacklistByRestaurant(DeleteBlacklistByRestaurantServiceRequest serviceRequest);

	List<GetBlacklistResponse> getBlackListByRestaurantId(GetBlacklistByRestaurantIdServiceRequest serviceRequest);

	Page<GetBlacklistResponse> getBlacklist(Pageable pageable);

	List<GetBlacklistResponse> getBlackListByRestaurant(
		GetBlackListByRestaurantServiceRequest getBlackListByRestaurantServiceRequest);
}