package team.waitingcatch.app.restaurant.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.restaurant.DeleteRestaurantByAdminServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.UpdateRestaurantControllerRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.UpdateRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.service.restaurant.RestaurantService;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class RestaurantController {
	private final RestaurantService restaurantService;

	// Customer
	@GetMapping("/restaurants/{restaurantId}")
	public RestaurantBasicInfoResponse getRestaurantBasicInfo(@PathVariable Long restaurantId) {
		RestaurantBasicInfoServiceRequest request = new RestaurantBasicInfoServiceRequest(restaurantId);
		return restaurantService.getRestaurantBasicInfo(request);
	}

	@GetMapping("/restaurants/{restaurantId}/details")
	public RestaurantDetailedInfoResponse getRestaurantDetailedInfo(@PathVariable Long restaurantId) {
		RestaurantDetailedInfoServiceRequest request = new RestaurantDetailedInfoServiceRequest(restaurantId);
		return restaurantService.getRestaurantDetailedInfo(request);
	}

	// Seller

	//판매자가 자신의 레스토랑 정보를 수정한다.
	@PutMapping("/restaurant/info")
	public void updateRestaurant(
		@RequestPart("updateRestaurantRequest") UpdateRestaurantControllerRequest updateRestaurantControllerRequest,
		@RequestPart("images") List<MultipartFile> multipartFile,
		@AuthenticationPrincipal UserDetails userDetails) throws IOException {
		UpdateRestaurantServiceRequest updateRestaurantServiceRequest =
			new UpdateRestaurantServiceRequest(updateRestaurantControllerRequest, multipartFile,
				userDetails.getUsername());

		restaurantService.updateRestaurant(updateRestaurantServiceRequest);
	}

	// Admin
	@GetMapping("/admin/restaurants")
	public List<RestaurantResponse> getRestaurants() {
		return restaurantService.getRestaurants();
	}

	//관리자가 레스토랑을 삭제 한다
	@DeleteMapping("/admin/restaurants/{restaurant_id}")
	public void deleteRestaurantByAdmin(@PathVariable Long restaurant_id) {
		DeleteRestaurantByAdminServiceRequest deleteRestaurantByAdminServiceRequest
			= new DeleteRestaurantByAdminServiceRequest(restaurant_id);
		restaurantService.deleteRestaurantByAdmin(deleteRestaurantByAdminServiceRequest);
	}

}
