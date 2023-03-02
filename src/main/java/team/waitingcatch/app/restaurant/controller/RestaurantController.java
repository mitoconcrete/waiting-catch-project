package team.waitingcatch.app.restaurant.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.dto.GenericResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.DeleteRestaurantByAdminServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantBasicInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantDetailedInfoServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantsWithinRadiusResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantsWithinRadiusServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantsResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.UpdateRestaurantControllerRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.UpdateRestaurantServiceRequest;
import team.waitingcatch.app.restaurant.service.restaurant.RestaurantService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

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

	@GetMapping("/restaurants/search")
	public List<SearchRestaurantsResponse> searchRestaurantsByKeyword(@RequestParam String keyword,
		@RequestParam double latitude, @RequestParam double longitude) {
		SearchRestaurantServiceRequest request = new SearchRestaurantServiceRequest(keyword, latitude, longitude);
		return restaurantService.searchRestaurantsByKeyword(request);
	}

	@GetMapping("/restaurants")
	public List<RestaurantsWithinRadiusResponse> getRestaurantsWithinRadius(
		@RequestParam double latitude, @RequestParam double longitude) {
		RestaurantsWithinRadiusServiceRequest request = new RestaurantsWithinRadiusServiceRequest(latitude,
			longitude, 3);
		return restaurantService.getRestaurantsWithinRadius(request);
	}

	// Seller

	//판매자가 자신의 레스토랑 정보를 수정한다.
	@PutMapping("/restaurant/info")
	public void updateRestaurant(
		@RequestPart("updateRestaurantRequest") UpdateRestaurantControllerRequest updateRestaurantControllerRequest,
		@RequestPart(value = "images", required = false) List<MultipartFile> multipartFile,
		@AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
		UpdateRestaurantServiceRequest updateRestaurantServiceRequest =
			new UpdateRestaurantServiceRequest(updateRestaurantControllerRequest, multipartFile,
				userDetails.getId());

		restaurantService.updateRestaurant(updateRestaurantServiceRequest);
	}

	// Admin
	@GetMapping("/admin/restaurants")
	public GenericResponse<Page<RestaurantResponse>> getRestaurants(@PageableDefault Pageable pageable) {
		return new GenericResponse(restaurantService.getRestaurants(pageable));
	}

	//관리자가 레스토랑을 삭제 한다
	@DeleteMapping("/admin/restaurants/{restaurant_id}")
	public void deleteRestaurantByAdmin(@PathVariable Long restaurant_id) {
		DeleteRestaurantByAdminServiceRequest deleteRestaurantByAdminServiceRequest
			= new DeleteRestaurantByAdminServiceRequest(restaurant_id);
		restaurantService.deleteRestaurantByAdmin(deleteRestaurantByAdminServiceRequest);
	}

}