package team.waitingcatch.app.restaurant.service.blacklist;

import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.user.entitiy.User;

public interface InternalBlackListService {
	void _createBlackList(
		Restaurant restaurant, User user);

}
