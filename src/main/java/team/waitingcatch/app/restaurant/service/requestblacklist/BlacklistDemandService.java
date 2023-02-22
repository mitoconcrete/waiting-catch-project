package team.waitingcatch.app.restaurant.service.requestblacklist;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.blacklist.ApproveBlackListServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.BlacklistRequestServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CancelBlacklistRequestServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetRequestBlacklistResponse;

public interface BlacklistRequestService {
	void requestUserBlacklist(BlacklistRequestServiceRequest serviceRequest);

	void cancelRequestUserBlacklist(CancelBlacklistRequestServiceRequest serviceRequest);

	List<GetRequestBlacklistResponse> getRequestBlacklists();

	void approveBlacklistRequest(ApproveBlackListServiceRequest serviceRequest);

	void rejectBlacklistRequest(Long blacklistRequestId);
}