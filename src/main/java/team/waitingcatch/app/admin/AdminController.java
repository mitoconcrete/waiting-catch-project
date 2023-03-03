package team.waitingcatch.app.admin;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.category.CreateCategoryRequest;
import team.waitingcatch.app.restaurant.dto.category.DeleteCategoryControllerRequest;
import team.waitingcatch.app.restaurant.dto.category.DeleteCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.category.GetChildCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.category.UpdateCategoryControllerRequest;
import team.waitingcatch.app.restaurant.dto.category.UpdateCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.GetDemandSignUpSellerResponse;
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

@RestController
@RequiredArgsConstructor
public class AdminController {
	private final UserService userService;
	private final RestaurantService restaurantService;
	private final SellerManagementService sellerManagementService;
	private final CategoryService categoryService;

	@GetMapping("/general/templates/admin/login")
	public ModelAndView loginPage() {
		return new ModelAndView("/admin/login");
	}

	@GetMapping("/general/templates/admin/register")
	public ModelAndView registerPage(Model model) {
		model.addAttribute("CreateUserControllerRequest", new CreateUserControllerRequest());
		return new ModelAndView("/admin/register");
	}

	@GetMapping("/admin/templates/main")
	public ModelAndView adminMainPage() {
		return new ModelAndView("/admin/index");
	}

	@PostMapping("/general/templates/admin/register")
	@ResponseStatus(HttpStatus.CREATED)
	public String createAdmin(
		@ModelAttribute("CreateUserControllerRequest") @Valid CreateUserControllerRequest controllerRequest,
		RedirectAttributes redirectAttributes) {
		_createUserService(UserRoleEnum.ADMIN, controllerRequest);
		return "redirect:/general/";
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
		@RequestParam(value = "searchVal", required = false) String searchVal) {
		Page<GetDemandSignUpSellerResponse> demandSignUpSellerResponses = null;

		if (searchVal == null) {
			demandSignUpSellerResponses = sellerManagementService.getDemandSignUpSellers(pageable);
		} else {
			demandSignUpSellerResponses = sellerManagementService.getDemandSignUpSellersById(searchVal,
				pageable);
		}

		model.addAttribute("requestSeller", demandSignUpSellerResponses);

		return new ModelAndView("/admin/seller-management");
	}
	//유저

	@GetMapping("/admin/templates/customers")
	public ModelAndView getCustomers(Model model, @PageableDefault(size = 10, page = 0) Pageable pageable,
		@RequestParam(value = "searchVal", required = false) String searchVal) {
		Page<UserInfoResponse> userInfoResponses = null;

		if (searchVal == null) {
			userInfoResponses = userService.getCustomers(pageable);
		} else {
			userInfoResponses = userService.getCustomersByUserName(searchVal, pageable);
		}

		model.addAttribute("customers", userInfoResponses);
		return new ModelAndView("/admin/user-list");
	}

	@GetMapping("/admin/templates/customers/{customerId}")
	public ModelAndView getCustomer(@PathVariable Long customerId, Model model) {

		GetCustomerByIdAndRoleServiceRequest servicePayload =
			new GetCustomerByIdAndRoleServiceRequest(
				customerId,
				UserRoleEnum.USER
			);
		model.addAttribute("customer", userService.getByUserIdAndRole(servicePayload));
		return new ModelAndView("/admin/user-view");
	}

	//레스토랑, 셀러
	@GetMapping("/admin/templates/restaurants")
	public ModelAndView restaurantListPage(Model model, @PageableDefault(size = 10, page = 0) Pageable pageable,
		@RequestParam(value = "searchVal", required = false) String searchVal) {
		Page<RestaurantResponse> restaurants = null;

		if (searchVal == null) {
			restaurants = restaurantService.getRestaurants(pageable);
		} else {
			restaurants = restaurantService.getRestaurantsByRestaurantName(searchVal,
				pageable);
		}

		model.addAttribute("restaurants", restaurants);
		return new ModelAndView("/admin/restaurant-list");
	}

	@GetMapping("/admin/templates/sellers/{sellerId}")
	public ModelAndView getSeller(@PathVariable Long sellerId, Model model) {
		GetCustomerByIdAndRoleServiceRequest servicePayload =
			new GetCustomerByIdAndRoleServiceRequest(
				sellerId,
				UserRoleEnum.SELLER
			);
		model.addAttribute("seller", userService.getByUserIdAndRole(servicePayload));
		return new ModelAndView("/admin/seller-view");
	}

	@DeleteMapping("/admin/templates/restaurants/{restaurant_id}")
	public void deleteRestaurantByAdmin(@PathVariable Long restaurant_id) {
		DeleteRestaurantByAdminServiceRequest deleteRestaurantByAdminServiceRequest
			= new DeleteRestaurantByAdminServiceRequest(restaurant_id);
		restaurantService.deleteRestaurantByAdmin(deleteRestaurantByAdminServiceRequest);
	}

	@GetMapping("/admin/templates/event")
	public ModelAndView eventPage() {
		return new ModelAndView("/admin/event");
	}

	@GetMapping("/admin/templates/blacklist")
	public ModelAndView blacklistPage() {
		return new ModelAndView("/admin/blacklist");
	}

	@GetMapping("/admin/templates/review")
	public ModelAndView censorReview() {
		return new ModelAndView("/admin/review");
	}

	//카테고리
	@GetMapping("/admin/templates/category")
	public ModelAndView categoryPage(Model model) {
		model.addAttribute("categories", categoryService.getParentCategories());
		model.addAttribute("CreateCategoryRequest", new CreateCategoryRequest());
		model.addAttribute("DeleteCategoryControllerRequest", new DeleteCategoryControllerRequest());
		model.addAttribute("UpdateCategoryControllerRequest", new UpdateCategoryControllerRequest());
		return new ModelAndView("/admin/category");
	}

	@PostMapping("/admin/templates/category")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createCategory(@ModelAttribute @Valid CreateCategoryRequest request) {
		categoryService.createCategory(request);
	}

	@PutMapping("/admin/templates/categories-update")
	public void updateCategory(@ModelAttribute @Valid UpdateCategoryControllerRequest controllerRequest) {
		UpdateCategoryServiceRequest serviceRequest =
			new UpdateCategoryServiceRequest(controllerRequest.getCategoryId(), controllerRequest.getName());
		categoryService.updateCategory(serviceRequest);
	}

	@DeleteMapping("/admin/templates/categories-form-delete")
	public void deleteCategory(@ModelAttribute DeleteCategoryControllerRequest deleteCategoryControllerRequest) {
		DeleteCategoryServiceRequest request = new DeleteCategoryServiceRequest(
			deleteCategoryControllerRequest.getCategoryId());
		categoryService.deleteCategory(request);
	}

	@DeleteMapping("/admin/templates/categories-direct-delete/{categoryId}")
	public void deleteCategory(@PathVariable Long categoryId) {
		DeleteCategoryServiceRequest request = new DeleteCategoryServiceRequest(
			categoryId);
		categoryService.deleteCategory(request);
	}

	@GetMapping("/admin/templates/categories/{categoryId}")
	public ModelAndView getChildCategories(@PathVariable Long categoryId, Model model) {
		GetChildCategoryServiceRequest request = new GetChildCategoryServiceRequest(categoryId);
		model.addAttribute("abc", categoryService.getChildCategories(request).getChildCategories());
		model.addAttribute("categoryAll", categoryService.getAllCategories());
		model.addAttribute("category", categoryService.getChildCategories(request));
		model.addAttribute("DeleteCategoryControllerRequest", new DeleteCategoryControllerRequest());
		model.addAttribute("UpdateCategoryControllerRequest", new UpdateCategoryControllerRequest());
		return new ModelAndView("/admin/category-view");
	}

}
