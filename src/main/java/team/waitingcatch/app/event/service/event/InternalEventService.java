package team.waitingcatch.app.event.service.event;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import team.waitingcatch.app.event.dto.event.GetEventsResponse;
import team.waitingcatch.app.event.entity.Event;
import team.waitingcatch.app.restaurant.entity.Restaurant;

public interface InternalEventService {

	Event _getEventById(Long id);

	//이벤트목록+이벤트생성자목록을 가저온다.
	Page<GetEventsResponse> _getEventsResponse(Page<Event> events, Restaurant restaurant, Pageable pageable);

	List<GetEventsResponse> _getGlobalEventsResponse(List<Event> events);

	void _bulkSoftDeleteByRestaurantId(Long restaurantId);
}
