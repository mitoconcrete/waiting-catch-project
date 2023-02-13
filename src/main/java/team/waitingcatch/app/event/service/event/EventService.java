package team.waitingcatch.app.event.service.event;

import team.waitingcatch.app.event.dto.event.CreateEventControllerRequest;
import team.waitingcatch.app.event.dto.event.CreateEventServiceRequest;
import team.waitingcatch.app.event.dto.event.GetEventServiceResponse;
import team.waitingcatch.app.event.dto.event.GetGlobalEventsServiceResponse;
import team.waitingcatch.app.event.dto.event.GetRestaurantEventControllerRequest;
import team.waitingcatch.app.event.dto.event.GetRestaurantEventsServiceResponse;
import team.waitingcatch.app.event.dto.event.UpdateEventServiceRequest;

public interface EventService {

	//새로운 광역 이벤트를 생성한다.
	public String createAdminEvent(CreateEventControllerRequest createEventControllerRequest);

	//새로운 레스토랑 이벤트를 생성한다.
	public String createSellerEvent(CreateEventServiceRequest createEventServiceRequest);

	//광역 이벤트를 수정한다
	public String updateAdminEvent(UpdateEventServiceRequest updateEventServiceRequest);

	//레스토랑 이벤트를 수정한다

	//광역 이벤트를 삭제한다.
	public String deleteAdminEvent(Long eventId);

	//모든 이벤트 목록을 가져온다.
	public GetGlobalEventsServiceResponse getGlobalEvents();

	//선택한 매장의 이벤트목록을 가져온다.
	public GetRestaurantEventsServiceResponse getRestaurantEvents(Long id);

	//이벤트 목록 중 특정 이벤트를 가져온다.
	public GetEventServiceResponse getEvent(
		GetRestaurantEventControllerRequest getRestaurantEventControllerRequest);

}
