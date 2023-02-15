package team.waitingcatch.app.restaurant.service.menu;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.menu.CreateMenuServiceRequest;
import team.waitingcatch.app.restaurant.dto.menu.MenuResponse;

public interface MenuService {
	void createMenu(CreateMenuServiceRequest serviceRequest);

	List<MenuResponse> getMyRestaurantMenus(Long restaurantId);
}
