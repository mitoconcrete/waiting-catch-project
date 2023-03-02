package team.waitingcatch.app.restaurant.service.requestblacklist;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import team.waitingcatch.app.restaurant.dto.blacklist.ApproveBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CancelBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CreateBlacklistDemandServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlackListDemandByRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetBlacklistDemandResponse;

public interface BlacklistDemandService {
	void submitBlacklistDemand(CreateBlacklistDemandServiceRequest serviceRequest);

	void cancelBlacklistDemand(CancelBlacklistDemandServiceRequest serviceRequest);

	Page<GetBlacklistDemandResponse> getBlacklistDemands(Pageable pageable);

	void approveBlacklistDemand(ApproveBlacklistDemandServiceRequest serviceRequest);

	void rejectBlacklistDemand(Long blacklistRequestId);

	List<GetBlacklistDemandResponse> getBlackListDemandsByRestaurant(
		GetBlackListDemandByRestaurantServiceRequest getBlackListDemandByRestaurantControllerRequest);
}