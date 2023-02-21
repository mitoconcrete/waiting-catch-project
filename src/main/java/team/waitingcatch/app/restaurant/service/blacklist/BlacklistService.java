package team.waitingcatch.app.restaurant.service.blacklist;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.blacklist.DeleteBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistByRestaurantIdServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistResponse;

public interface BlacklistService {
	void deleteBlacklistByRestaurant(DeleteBlacklistByRestaurantServiceRequest serviceRequest);

	List<GetBlacklistResponse> getBlacklistByRestaurantIdRequest(
		GetBlacklistByRestaurantIdServiceRequest serviceRequest);
}