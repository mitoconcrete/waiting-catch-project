package team.waitingcatch.app.event.service.event;

import team.waitingcatch.app.event.dto.event.createEventControllerRequest;
import team.waitingcatch.app.event.dto.event.deleteEventControllerRequest;
import team.waitingcatch.app.event.dto.event.getEventServiceResponse;
import team.waitingcatch.app.event.dto.event.getGlobalEventsServiceResponse;
import team.waitingcatch.app.event.dto.event.getRestaurantEventControllerRequest;
import team.waitingcatch.app.event.dto.event.getRestaurantEventsServiceResponse;
import team.waitingcatch.app.event.dto.event.putEventControllerRequest;

public interface EventService {

	//새로운 이벤트를 생성한다.
	public void createEvent(createEventControllerRequest createEventControllerRequest);

	//이벤트를 수정한다
	public void putEvent(putEventControllerRequest putEventControllerRequest);

	//이벤트를 삭제한다.
	public void deleteEvent(deleteEventControllerRequest deleteEventControllerRequest);

	//모든 이벤트 목록을 가져온다.
	public getGlobalEventsServiceResponse getGlobalEvents();

	//선택한 매장의 이벤트목록을 가져온다.
	public getRestaurantEventsServiceResponse getRestaurantEvents(Long id);

	//이벤트 목록 중 특정 이벤트를 가져온다.
	public getEventServiceResponse getEvent(
		getRestaurantEventControllerRequest getRestaurantEventControllerRequest);

}
