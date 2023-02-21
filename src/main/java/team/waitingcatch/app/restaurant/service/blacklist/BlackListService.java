package team.waitingcatch.app.restaurant.service.blacklist;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.blacklist.DeleteUserBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistByRestaurantIdServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistResponse;

public interface BlackListService {
	void deleteUserBlackListByRestaurant(DeleteUserBlacklistByRestaurantServiceRequest serviceRequest);

	List<GetBlacklistResponse> getBlackListByRestaurantIdRequest(
		GetBlacklistByRestaurantIdServiceRequest serviceRequest);
}