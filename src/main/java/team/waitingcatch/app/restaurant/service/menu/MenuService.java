package team.waitingcatch.app.restaurant.service.menu;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.menu.CreateMenuServiceRequest;
import team.waitingcatch.app.restaurant.dto.menu.CustomerMenuResponse;
import team.waitingcatch.app.restaurant.dto.menu.DeleteMenuServiceRequest;
import team.waitingcatch.app.restaurant.dto.menu.MenuResponse;
import team.waitingcatch.app.restaurant.dto.menu.UpdateMenuServiceRequest;

public interface MenuService {
	List<CustomerMenuResponse> getRestaurantMenus(Long restaurantId);

	void createMenu(CreateMenuServiceRequest serviceRequest);

	List<MenuResponse> getMyRestaurantMenus(Long restaurantId);

	void updateMenu(UpdateMenuServiceRequest serviceRequest);

	void deleteMenu(DeleteMenuServiceRequest request);
}