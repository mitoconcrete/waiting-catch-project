package team.waitingcatch.app.restaurant.service.blacklist;

import team.waitingcatch.app.restaurant.dto.blacklist.DeleteUserBlackListByRestaurantServiceRequest;

public interface BlackListService {
	void deleteUserBlackListByRestaurant(
		DeleteUserBlackListByRestaurantServiceRequest deleteUserBlackListByRestaurantServiceRequest);
}
