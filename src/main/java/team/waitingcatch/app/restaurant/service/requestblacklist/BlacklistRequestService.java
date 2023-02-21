package team.waitingcatch.app.restaurant.service.requestblacklist;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.blacklist.ApproveBlacklistServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CancelBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistResponse;
import team.waitingcatch.app.restaurant.dto.blacklist.DemandBlacklistByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetRequestBlacklistResponse;

public interface BlacklistRequestService {
	void requestUserBlackList(DemandBlacklistByRestaurantServiceRequest serviceRequest);

	void cancelRequestUserBlacklist(CancelBlacklistByRestaurantServiceRequest serviceRequest);

	List<GetRequestBlacklistResponse> getRequestBlacklist();

	void approveBlacklistRequest(ApproveBlacklistServiceRequest serviceRequest);

}