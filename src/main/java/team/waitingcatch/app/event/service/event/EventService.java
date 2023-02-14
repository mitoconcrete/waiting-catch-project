package team.waitingcatch.app.event.service.event;

import java.util.List;

import team.waitingcatch.app.event.dto.event.CreateEventControllerRequest;
import team.waitingcatch.app.event.dto.event.CreateEventServiceRequest;
import team.waitingcatch.app.event.dto.event.DeleteEventServiceRequest;
import team.waitingcatch.app.event.dto.event.GetEventServiceResponse;
import team.waitingcatch.app.event.dto.event.GetEventsResponse;
import team.waitingcatch.app.event.dto.event.GetRestaurantEventControllerRequest;
import team.waitingcatch.app.event.dto.event.UpdateEventServiceRequest;
import team.waitingcatch.app.event.dto.event.UpdateSellerEventServiceRequest;

public interface EventService {

	//새로운 광역 이벤트를 생성한다.
	public String createAdminEvent(CreateEventControllerRequest createEventControllerRequest);

	//새로운 레스토랑 이벤트를 생성한다.
	public String createSellerEvent(CreateEventServiceRequest createEventServiceRequest);

	//광역 이벤트를 수정한다
	public String updateAdminEvent(UpdateEventServiceRequest updateEventServiceRequest);

	//레스토랑 이벤트를 수정한다
	public String updateSellerEvent(UpdateSellerEventServiceRequest updateSellerEventServiceRequest);

	//광역 이벤트를 삭제한다.
	public String deleteAdminEvent(Long eventId);

	//레스토랑 이벤트를 삭제한다.
	public String deleteSellerEvent(DeleteEventServiceRequest deleteEventServiceRequest);

	//모든 광역 이벤트 목록을 가져온다.
	public List<GetEventsResponse> getGlobalEvents();

	//선택한 매장의 이벤트목록을 가져온다.
	public List<GetEventsResponse> getRestaurantEvents(Long restaurantId);

	//이벤트 목록 중 특정 이벤트를 가져온다.
	public GetEventServiceResponse getEvent(
		GetRestaurantEventControllerRequest getRestaurantEventControllerRequest);

}
