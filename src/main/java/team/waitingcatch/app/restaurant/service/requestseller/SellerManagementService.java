package team.waitingcatch.app.restaurant.service.requestseller;

import java.util.List;

import team.waitingcatch.app.restaurant.dto.ApproveSignUpSellerResponse;
import team.waitingcatch.app.restaurant.dto.ApproveSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.DemandSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.GetDemandSignUpSellerResponse;
import team.waitingcatch.app.restaurant.dto.RejectSignUpSellerServiceRequest;

public interface SellerManagementService {
	void demandSignUpSeller(DemandSignUpSellerServiceRequest demandSignupSellerServiceRequest);

	List<GetDemandSignUpSellerResponse> getDemandSignUpSellers();

	ApproveSignUpSellerResponse approveSignUpSeller(
		ApproveSignUpSellerServiceRequest approveSignUpSellerServiceRequest);

	void rejectSignUpSeller(RejectSignUpSellerServiceRequest rejectSignUpSellerServiceRequest);

}
