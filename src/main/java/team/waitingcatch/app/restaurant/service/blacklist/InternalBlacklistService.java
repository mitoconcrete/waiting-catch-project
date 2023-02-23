package team.waitingcatch.app.restaurant.service.blacklist;

import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.user.entitiy.User;

public interface InternalBlacklistService {
	boolean _existsByRestaurantIdAndUserId(Long restaurantId, Long userId);
	void _createBlackList(Restaurant restaurant, User user);
}