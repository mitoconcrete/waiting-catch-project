package team.waitingcatch.app.event.service.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import team.waitingcatch.app.event.dto.event.CreateEventControllerRequest;
import team.waitingcatch.app.event.dto.event.CreateEventServiceRequest;
import team.waitingcatch.app.event.dto.event.DeleteEventControllerRequest;
import team.waitingcatch.app.event.dto.event.DeleteEventServiceRequest;
import team.waitingcatch.app.event.dto.event.GetEventsResponse;
import team.waitingcatch.app.event.dto.event.UpdateEventServiceRequest;
import team.waitingcatch.app.event.dto.event.UpdateSellerEventServiceRequest;

public interface EventService {

	//새로운 광역 이벤트를 생성한다.
	void createAdminEvent(CreateEventControllerRequest createEventControllerRequest);

	//새로운 레스토랑 이벤트를 생성한다.
	void createSellerEvent(CreateEventServiceRequest createEventServiceRequest);

	//광역 이벤트를 수정한다
	void updateAdminEvent(UpdateEventServiceRequest updateEventServiceRequest);

	//레스토랑 이벤트를 수정한다
	void updateSellerEvent(UpdateSellerEventServiceRequest updateSellerEventServiceRequest);

	//광역 이벤트를 삭제한다.
	void deleteAdminEvent(DeleteEventControllerRequest deleteEventControllerRequest);

	//레스토랑 이벤트를 삭제한다.
	void deleteSellerEvent(DeleteEventServiceRequest deleteEventServiceRequest);

	//모든 광역 이벤트 목록을 가져온다.
	Page<GetEventsResponse> getGlobalEvents(Long restaurantId, Pageable pageable);

	//선택한 매장의 이벤트목록을 가져온다.
	Page<GetEventsResponse> getRestaurantEvents(Long restaurantId, Pageable pageable);

}