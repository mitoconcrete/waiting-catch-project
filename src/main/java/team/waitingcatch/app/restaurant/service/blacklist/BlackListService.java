package team.waitingcatch.app.restaurant.service.blacklist;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.blacklist.DeleteUserBlackListByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlackListByRestaurantIdServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlackListResponse;

public interface BlackListService {
	void deleteUserBlackListByRestaurant(
		DeleteUserBlackListByRestaurantServiceRequest deleteUserBlackListByRestaurantServiceRequest);

	List<GetBlackListResponse> getBlackListByRestaurantIdRequest(
		GetBlackListByRestaurantIdServiceRequest getBlackListByRestaurantIdServiceRequest);
}
