package team.waitingcatch.app.restaurant.service.requestblacklist;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.blacklist.ApproveBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CancelBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CreateBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlackListDemandByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistDemandCustomerInfoResponse;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistDemandResponse;

public interface BlacklistDemandService {
	GetBlacklistDemandCustomerInfoResponse getCustomerIdByLineupId(long lineupId);

	void submitBlacklistDemand(CreateBlacklistDemandServiceRequest serviceRequest);

	void cancelBlacklistDemand(CancelBlacklistDemandServiceRequest serviceRequest);

	List<GetBlacklistDemandResponse> getBlacklistDemands();

	void approveBlacklistDemand(ApproveBlacklistDemandServiceRequest serviceRequest);

	void rejectBlacklistDemand(Long blacklistRequestId);

	List<GetBlacklistDemandResponse> getBlackListDemandsByRestaurant(
		GetBlackListDemandByRestaurantServiceRequest getBlackListDemandByRestaurantControllerRequest);
}