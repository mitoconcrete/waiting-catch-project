package team.waitingcatch.app.restaurant.service.requestseller;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.requestseller.ApproveSignUpSellerResponse;
import team.waitingcatch.app.restaurant.dto.requestseller.ApproveSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.DemandSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.GetDemandSignUpSellerResponse;
import team.waitingcatch.app.restaurant.dto.requestseller.GetRequestSellerByRestaurantRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.RejectSignUpSellerServiceRequest;

public interface SellerManagementService {
	void demandSignUpSeller(DemandSignUpSellerServiceRequest demandSignupSellerServiceRequest);

	List<GetDemandSignUpSellerResponse> getDemandSignUpSellers();

	ApproveSignUpSellerResponse approveSignUpSeller(
		ApproveSignUpSellerServiceRequest approveSignUpSellerServiceRequest);

	void rejectSignUpSeller(RejectSignUpSellerServiceRequest rejectSignUpSellerServiceRequest);

	GetDemandSignUpSellerResponse getRequestSellerByRestaurant(
		GetRequestSellerByRestaurantRequest getRequestSellerByRestaurantRequest);
}
