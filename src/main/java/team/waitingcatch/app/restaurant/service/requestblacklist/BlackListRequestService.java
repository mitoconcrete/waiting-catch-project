package team.waitingcatch.app.restaurant.service.requestblacklist;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.blacklist.ApproveBlackListServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.CancelRequestBlackListBySellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.blacklist.GetRequestBlackListResponse;
import team.waitingcatch.app.restaurant.dto.blacklist.RequestUserBlackListByRestaurantServiceRequest;

public interface BlackListRequestService {
	void requestUserBlackList(
		RequestUserBlackListByRestaurantServiceRequest requestUserBlackListByRestaurantServiceRequest);

	void cancelRequestUserBlackList(
		CancelRequestBlackListBySellerServiceRequest cancelRequestBlackListBySellerServiceRequest);

	List<GetRequestBlackListResponse> getRequestBlackLists();

	void approveBlackListRequest(ApproveBlackListServiceRequest approveBlackListServiceRequest);

}
