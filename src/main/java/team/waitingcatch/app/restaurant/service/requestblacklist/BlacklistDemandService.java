package team.waitingcatch.app.restaurant.service.requestblacklist;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.blacklist.ApproveBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CancelBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CreateBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistDemandResponse;

public interface BlacklistDemandService {
	void submitBlacklistDemand(CreateBlacklistDemandServiceRequest serviceRequest);

	void cancelBlacklistDemand(CancelBlacklistDemandServiceRequest serviceRequest);

	List<GetBlacklistDemandResponse> getBlacklistDemands();

	void approveBlacklistDemand(ApproveBlacklistDemandServiceRequest serviceRequest);

	void rejectBlacklistDemand(Long blacklistRequestId);
}