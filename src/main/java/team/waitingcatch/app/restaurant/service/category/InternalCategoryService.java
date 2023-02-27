package team.waitingcatch.app.restaurant.service.category;

import team.waitingcatch.app.restaurant.dto.requestseller.ConnectCategoryRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.entity.Category;

public interface InternalCategoryService {
	Category _getCategory(Long categoryId);

	void connectCategoryRestaurant(ConnectCategoryRestaurantServiceRequest serviceRequest);
}
