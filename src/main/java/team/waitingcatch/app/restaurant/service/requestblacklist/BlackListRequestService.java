package team.waitingcatch.app.restaurant.service.requestblacklist;

import team.waitingcatch.app.restaurant.dto.blacklist.RequestUserBlackListByRestaurantServiceRequest;

public interface BlackListRequestService {
	void requestUserBlackList(
		RequestUserBlackListByRestaurantServiceRequest requestUserBlackListByRestaurantServiceRequest);

}
