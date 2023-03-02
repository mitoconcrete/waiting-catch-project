package team.waitingcatch.app.restaurant.service.category;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.requestseller.ConnectCategoryRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.entity.Category;

public interface InternalCategoryService {
	Category _getCategory(Long categoryId);

	void _connectCategoryRestaurant(ConnectCategoryRestaurantServiceRequest serviceRequest);

	List<String> _getCategoryNames(List<Long> categoryIds);
}
