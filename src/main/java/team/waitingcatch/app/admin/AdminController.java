package team.waitingcatch.app.admin;

import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.category.CreateCategoryRequest;
import team.waitingcatch.app.restaurant.dto.category.DeleteCategoryControllerRequest;
import team.waitingcatch.app.restaurant.dto.category.DeleteCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.category.GetChildCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.category.UpdateCategoryControllerRequest;
import team.waitingcatch.app.restaurant.dto.category.UpdateCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.ApproveSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.GetDemandSignUpSellerResponse;
import team.waitingcatch.app.restaurant.dto.requestseller.RejectSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.DeleteRestaurantByAdminServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantResponse;
import team.waitingcatch.app.restaurant.service.category.CategoryService;
import team.waitingcatch.app.restaurant.service.requestseller.SellerManagementService;
import team.waitingcatch.app.restaurant.service.restaurant.RestaurantService;
import team.waitingcatch.app.user.dto.CreateUserControllerRequest;
import team.waitingcatch.app.user.dto.CreateUserServiceRequest;
import team.waitingcatch.app.user.dto.GetCustomerByIdAndRoleServiceRequest;
import team.waitingcatch.app.user.dto.UserInfoResponse;
import team.waitingcatch.app.user.enums.UserRoleEnum;
import team.waitingcatch.app.user.service.UserService;

@Controller
@RequiredArgsConstructor
public class AdminController {
	private final UserService userService;
	private final RestaurantService restaurantService;
	private final SellerManagementService sellerManagementService;
	private final CategoryService categoryService;

	@GetMapping("/general/templates/admin/login")
	public ModelAndView loginPage() {
		return new ModelAndView("admin/login");
	}

	@GetMapping("/general/templates/admin/register")
	public ModelAndView registerPage(Model model, HttpServletResponse response) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		model.addAttribute("CreateUserControllerRequest", new CreateUserControllerRequest());
		return new ModelAndView("admin/register");
	}

	@GetMapping("/admin/templates/main")
	public ModelAndView adminMainPage(Model model, HttpServletResponse response) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		return new ModelAndView("admin/index");
	}

	@PostMapping("/general/templates/admin/register")
	@ResponseStatus(HttpStatus.CREATED)
	public String createAdmin(
		@ModelAttribute("CreateUserControllerRequest") @Valid CreateUserControllerRequest controllerRequest
		, HttpServletResponse response, Model model) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		_createUserService(UserRoleEnum.ADMIN, controllerRequest);
		model.addAttribute("message", "회원가입이 완료되었습니다.");
		model.addAttribute("searchUrl", "/general/templates/admin/login");
		return "admin/message";
	}

	private void _createUserService(UserRoleEnum role, CreateUserControllerRequest controllerRequest) {
		// 컨트롤러에서 전달받은 데이터를 기반으로 role을 할당하여 서비스에 전달한다.
		CreateUserServiceRequest servicePayload =
			new CreateUserServiceRequest(
				role,
				controllerRequest.getName(),
				controllerRequest.getEmail(),
				controllerRequest.getUsername(),
				controllerRequest.getPassword(),
				controllerRequest.getNickname(),
				controllerRequest.getPhoneNumber()
			);

		userService.createUser(servicePayload);
	}

	//판매자요청
	@GetMapping("/admin/templates/seller-management")
	public ModelAndView sellerManagementPage(Model model, @PageableDefault(size = 10, page = 0) Pageable pageable,
		@RequestParam(value = "searchVal", required = false) String searchVal, HttpServletResponse response) {
		Page<GetDemandSignUpSellerResponse> demandSignUpSellerResponses = null;

		if (searchVal == null) {
			demandSignUpSellerResponses = sellerManagementService.getDemandSignUpSellers(pageable);
		} else {
			demandSignUpSellerResponses = sellerManagementService.getDemandSignUpSellersById(searchVal,
				pageable);
		}
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}

		model.addAttribute("requestSeller", demandSignUpSellerResponses);

		return new ModelAndView("admin/seller-management");
	}
	//유저

	@GetMapping("/admin/templates/customers")
	public ModelAndView getCustomers(Model model, @PageableDefault(size = 10, page = 0) Pageable pageable,
		@RequestParam(value = "searchVal", required = false) String searchVal, HttpServletResponse response) {
		Page<UserInfoResponse> userInfoResponses = null;

		if (searchVal == null) {
			userInfoResponses = userService.getCustomers(pageable);
		} else {
			userInfoResponses = userService.getCustomersByUserName(searchVal, pageable);
		}

		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}

		model.addAttribute("customers", userInfoResponses);
		return new ModelAndView("admin/user-list");
	}

	@GetMapping("/admin/templates/customers/{customerId}")
	public ModelAndView getCustomer(@PathVariable Long customerId, Model model, HttpServletResponse response) {

		GetCustomerByIdAndRoleServiceRequest servicePayload =
			new GetCustomerByIdAndRoleServiceRequest(
				customerId,
				UserRoleEnum.USER
			);

		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		model.addAttribute("customer", userService.getByUserIdAndRole(servicePayload));
		return new ModelAndView("admin/user-view");
	}

	//레스토랑, 셀러
	@GetMapping("/admin/templates/restaurants")
	public ModelAndView restaurantListPage(Model model, @PageableDefault(size = 10, page = 0) Pageable pageable,
		@RequestParam(value = "searchVal", required = false) String searchVal, HttpServletResponse response) {
		Page<RestaurantResponse> restaurants = null;

		if (searchVal == null) {
			restaurants = restaurantService.getRestaurants(pageable);
		} else {
			restaurants = restaurantService.getRestaurantsByRestaurantName(searchVal,
				pageable);
		}

		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		model.addAttribute("restaurants", restaurants);
		return new ModelAndView("admin/restaurant-list");
	}

	@GetMapping("/admin/templates/sellers/{sellerId}")
	public ModelAndView getSeller(@PathVariable Long sellerId, Model model, HttpServletResponse response) {
		GetCustomerByIdAndRoleServiceRequest servicePayload =
			new GetCustomerByIdAndRoleServiceRequest(
				sellerId,
				UserRoleEnum.SELLER
			);

		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		model.addAttribute("seller", userService.getByUserIdAndRole(servicePayload));
		return new ModelAndView("admin/seller-view");
	}

	@DeleteMapping("/admin/templates/restaurants/{restaurant_id}")
	public String deleteRestaurantByAdmin(@PathVariable Long restaurant_id, Model model) {
		DeleteRestaurantByAdminServiceRequest deleteRestaurantByAdminServiceRequest
			= new DeleteRestaurantByAdminServiceRequest(restaurant_id);
		boolean deleteRestaurant = restaurantService.deleteRestaurantByAdmin(deleteRestaurantByAdminServiceRequest);
		if (!deleteRestaurant) {
			model.addAttribute("message", "레스토랑이 이미 삭제 되어 있습니다.");
		} else {
			model.addAttribute("message", "레스토랑 삭제가 완료되었습니다.");
		}
		model.addAttribute("searchUrl", "/admin/templates/restaurants");
		return "admin/message";
	}

	@GetMapping("/admin/templates/event")
	public ModelAndView eventPage(HttpServletResponse response, Model model) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		return new ModelAndView("admin/event");
	}

	@GetMapping("/admin/templates/blacklist")
	public ModelAndView blacklistPage(HttpServletResponse response, Model model) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		return new ModelAndView("admin/blacklist");
	}

	@GetMapping("/admin/templates/review")
	public ModelAndView censorReview(HttpServletResponse response, Model model) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		return new ModelAndView("admin/review");
	}

	//카테고리
	@GetMapping("/admin/templates/category")
	public ModelAndView categoryPage(Model model, HttpServletResponse response) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		model.addAttribute("categories", categoryService.getParentCategories());
		model.addAttribute("CreateCategoryRequest", new CreateCategoryRequest());
		model.addAttribute("DeleteCategoryControllerRequest", new DeleteCategoryControllerRequest());
		model.addAttribute("UpdateCategoryControllerRequest", new UpdateCategoryControllerRequest());
		return new ModelAndView("admin/category");
	}

	@PostMapping("/admin/templates/category")
	@ResponseStatus(value = HttpStatus.CREATED)
	public String createCategory(@ModelAttribute @Valid CreateCategoryRequest request, Model model) {
		categoryService.createCategory(request);
		model.addAttribute("message", "카테고리 작성이 완료되었습니다.");
		model.addAttribute("searchUrl", "/admin/templates/category");
		return "admin/message";
	}

	@PutMapping("/admin/templates/categories-update")
	public String updateCategory(@ModelAttribute @Valid UpdateCategoryControllerRequest controllerRequest,
		Model model) {
		UpdateCategoryServiceRequest serviceRequest =
			new UpdateCategoryServiceRequest(controllerRequest.getCategoryId(), controllerRequest.getName());
		categoryService.updateCategory(serviceRequest);
		model.addAttribute("message", "카테고리 수정이 완료 되었습니다.");
		model.addAttribute("searchUrl", "/admin/templates/category");
		return "admin/message";
	}

	@DeleteMapping("/admin/templates/categories-form-delete")
	public String deleteCategory(@ModelAttribute DeleteCategoryControllerRequest deleteCategoryControllerRequest,
		Model model) {
		DeleteCategoryServiceRequest request = new DeleteCategoryServiceRequest(
			deleteCategoryControllerRequest.getCategoryId());
		categoryService.deleteCategory(request);
		model.addAttribute("message", "카테고리 삭제가 완료 되었습니다.");
		model.addAttribute("searchUrl", "/admin/templates/category");
		return "admin/message";
	}

	@DeleteMapping("/admin/templates/categories-direct-delete/{categoryId}")
	public String deleteCategory(@PathVariable Long categoryId, Model model) {
		DeleteCategoryServiceRequest request = new DeleteCategoryServiceRequest(
			categoryId);
		categoryService.deleteCategory(request);
		model.addAttribute("message", "카테고리 삭제가 완료 되었습니다.");
		model.addAttribute("searchUrl", "/admin/templates/category");
		return "admin/message";
	}

	@GetMapping("/admin/templates/categories/{categoryId}")
	public ModelAndView getChildCategories(@PathVariable Long categoryId, Model model, HttpServletResponse response) {
		Collection<String> headerNames = response.getHeaderNames();
		if (headerNames.contains("Authorization")) {
			String token = response.getHeader("Authorization");
			model.addAttribute("accessToken", token);
		}
		GetChildCategoryServiceRequest request = new GetChildCategoryServiceRequest(categoryId);
		model.addAttribute("abc", categoryService.getChildCategories(request).getChildCategories());
		model.addAttribute("categoryAll", categoryService.getAllCategories());
		model.addAttribute("category", categoryService.getChildCategories(request));
		model.addAttribute("DeleteCategoryControllerRequest", new DeleteCategoryControllerRequest());
		model.addAttribute("UpdateCategoryControllerRequest", new UpdateCategoryControllerRequest());
		return new ModelAndView("admin/category-view");
	}

	@PostMapping("/admin/templates/seller-managements/{sellerManagementId}")
	public String approveSignUpSeller(@PathVariable Long sellerManagementId, Model model) {
		ApproveSignUpSellerServiceRequest approveSignUpSellerServiceRequest = new ApproveSignUpSellerServiceRequest(
			sellerManagementId);
		boolean approveSignUpSeller = sellerManagementService.approveSignUpSeller(approveSignUpSellerServiceRequest);
		if (!approveSignUpSeller) {
			model.addAttribute("message", "판매자 요청이 이미 승인되었거나 이미 거절 되어있습니다.");
		} else {
			model.addAttribute("message", "판매자 요청이 승인 되었습니다.");
		}
		model.addAttribute("searchUrl", "/admin/templates/seller-management");
		return "admin/message";
	}

	@PutMapping("/admin/templates/seller-managements/{sellerManagementId}")
	public String rejectSignUpSeller(@PathVariable Long sellerManagementId, Model model) {
		RejectSignUpSellerServiceRequest rejectSignUpSellerServiceRequest = new RejectSignUpSellerServiceRequest(
			sellerManagementId);
		boolean rejectSignUpSeller = sellerManagementService.rejectSignUpSeller(rejectSignUpSellerServiceRequest);
		if (!rejectSignUpSeller) {
			model.addAttribute("message", "판매자 요청이 이미 승인되었거나 이미 거절 되어있습니다.");
		} else {
			model.addAttribute("message", "판매자 요청이 거절 되었습니다.");
		}
		model.addAttribute("searchUrl", "/admin/templates/seller-management");
		return "admin/message";
	}
}