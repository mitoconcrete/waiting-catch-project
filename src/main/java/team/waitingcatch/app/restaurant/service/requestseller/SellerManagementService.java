package team.waitingcatch.app.restaurant.service.requestseller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import team.waitingcatch.app.restaurant.dto.requestseller.ApproveSignUpSellerResponse;
import team.waitingcatch.app.restaurant.dto.requestseller.ApproveSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.DemandSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.GetDemandSignUpSellerResponse;
import team.waitingcatch.app.restaurant.dto.requestseller.GetRequestSellerByRestaurantRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.RejectSignUpSellerServiceRequest;

public interface SellerManagementService {
	void demandSignUpSeller(DemandSignUpSellerServiceRequest demandSignupSellerServiceRequest);

	Page<GetDemandSignUpSellerResponse> getDemandSignUpSellers(Pageable pageable);

	ApproveSignUpSellerResponse approveSignUpSeller(
		ApproveSignUpSellerServiceRequest approveSignUpSellerServiceRequest);

	void rejectSignUpSeller(RejectSignUpSellerServiceRequest rejectSignUpSellerServiceRequest);

	GetDemandSignUpSellerResponse getRequestSellerByRestaurant(
		GetRequestSellerByRestaurantRequest getRequestSellerByRestaurantRequest);

	Page<GetDemandSignUpSellerResponse> getDemandSignUpSellersById(String searchVal, Pageable pageable);
}
