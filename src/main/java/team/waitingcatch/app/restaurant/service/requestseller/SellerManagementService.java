package team.waitingcatch.app.restaurant.service.requestseller;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.DemandSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.GetDemandSignUpSellerResponse;

public interface SellerManagementService {
	void demandSignUpSeller(DemandSignUpSellerServiceRequest demandSignupSellerServiceRequest);

	List<GetDemandSignUpSellerResponse> getDemandSignUpSellers();
}
