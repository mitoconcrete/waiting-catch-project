package team.waitingcatch.app.restaurant.service.requestblacklist;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.blacklist.ApproveBlackListServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CancelRequestUserBlackListByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetRequestBlacklistResponse;
import team.waitingcatch.app.restaurant.dto.blacklist.RequestUserBlacklistByRestaurantServiceRequest;

public interface BlackListRequestService {
	void requestUserBlackList(
		RequestUserBlacklistByRestaurantServiceRequest requestUserBlackListByRestaurantServiceRequest);

	void cancelRequestUserBlackList(
		CancelRequestUserBlackListByRestaurantServiceRequest cancelRequestUserBlackListByRestaurantServiceRequest);

	List<GetRequestBlacklistResponse> getRequestBlackLists();

	void approveBlackListRequest(ApproveBlackListServiceRequest approveBlackListServiceRequest);

}