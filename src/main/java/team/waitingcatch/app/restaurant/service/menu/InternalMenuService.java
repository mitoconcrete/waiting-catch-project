package team.waitingcatch.app.restaurant.service.menu;

import java.util.List;

import team.waitingcatch.app.restaurant.entity.Menu;

public interface InternalMenuService {
	List<Menu> _getMenusByRestaurantId(Long restaurantId);

	Menu _getMenuById(Long menuId);
}
