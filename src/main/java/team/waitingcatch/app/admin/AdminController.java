package team.waitingcatch.app.admin;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.category.CreateCategoryRequest;
import team.waitingcatch.app.restaurant.dto.category.DeleteCategoryControllerRequest;
import team.waitingcatch.app.restaurant.dto.category.DeleteCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.category.GetChildCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.category.UpdateCategoryControllerRequest;
import team.waitingcatch.app.restaurant.dto.category.UpdateCategoryServiceRequest;
import team.waitingcatch.app.restaurant.dto.restaurant.DeleteRestaurantByAdminServiceRequest;
import team.waitingcatch.app.restaurant.service.category.CategoryService;
import team.waitingcatch.app.restaurant.service.requestseller.SellerManagementService;
import team.waitingcatch.app.restaurant.service.restaurant.RestaurantService;
import team.waitingcatch.app.user.dto.CreateUserControllerRequest;
import team.waitingcatch.app.user.dto.CreateUserServiceRequest;
import team.waitingcatch.app.user.dto.GetCustomerByIdAndRoleServiceRequest;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;
import team.waitingcatch.app.user.enums.UserRoleEnum;
import team.waitingcatch.app.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/templates")
public class AdminController {
	private final UserService userService;
	private final RestaurantService restaurantService;
	private final SellerManagementService sellerManagementService;
	private final CategoryService categoryService;

	@GetMapping("/login")
	public ModelAndView loginPage() {
		return new ModelAndView("/admin/login");
	}

	@GetMapping("/register")
	public ModelAndView registerPage(Model model) {
		model.addAttribute("CreateUserControllerRequest", new CreateUserControllerRequest());
		return new ModelAndView("/admin/register");
	}

	@GetMapping("/main")
	public ModelAndView adminMainPage() {
		return new ModelAndView("/admin/index");
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public void createAdmin(
		@ModelAttribute("CreateUserControllerRequest") @Valid CreateUserControllerRequest controllerRequest) {
		_createUserService(UserRoleEnum.ADMIN, controllerRequest);
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
	@GetMapping("/seller-management")
	public ModelAndView sellerManagementPage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		model.addAttribute("requestSeller", sellerManagementService.getDemandSignUpSellers());
		return new ModelAndView("/admin/seller-management");
	}
	//유저

	@GetMapping("/customers")
	public ModelAndView getCustomers(Model model, Pageable pageable) {
		model.addAttribute("customers", userService.getCustomers(pageable));
		return new ModelAndView("/admin/user-list");
	}

	@GetMapping("/user-list")
	public ModelAndView userListPage(Model model) {
		model.addAttribute("requestSeller", sellerManagementService.getDemandSignUpSellers());
		return new ModelAndView("/admin/user-list");
	}

	@GetMapping("/customers/{customerId}")
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
	@GetMapping("/restaurants")
	public ModelAndView restaurantListPage(Model model) {
		model.addAttribute("restaurants", restaurantService.getRestaurants());
		return new ModelAndView("/admin/restaurant-list");
	}

	@GetMapping("/sellers/{sellerId}")
	public ModelAndView getSeller(@PathVariable Long sellerId, Model model) {
		GetCustomerByIdAndRoleServiceRequest servicePayload =
			new GetCustomerByIdAndRoleServiceRequest(
				sellerId,
				UserRoleEnum.SELLER
			);
		model.addAttribute("seller", userService.getByUserIdAndRole(servicePayload));
		return new ModelAndView("/admin/seller-view");
	}

	@DeleteMapping("/restaurants/{restaurant_id}")
	public void deleteRestaurantByAdmin(@PathVariable Long restaurant_id) {
		DeleteRestaurantByAdminServiceRequest deleteRestaurantByAdminServiceRequest
			= new DeleteRestaurantByAdminServiceRequest(restaurant_id);
		restaurantService.deleteRestaurantByAdmin(deleteRestaurantByAdminServiceRequest);
	}

	@GetMapping("/event")
	public ModelAndView eventPage() {
		return new ModelAndView("/admin/event");
	}

	@GetMapping("/blacklist")
	public ModelAndView blacklistPage() {
		return new ModelAndView("/admin/blacklist");
	}

	@GetMapping("/review")
	public ModelAndView censorReview() {
		return new ModelAndView("/admin/review");
	}

	//카테고리
	@GetMapping("/category")
	public ModelAndView categoryPage(Model model) {
		model.addAttribute("categories", categoryService.getParentCategories());
		model.addAttribute("CreateCategoryRequest", new CreateCategoryRequest());
		model.addAttribute("DeleteCategoryControllerRequest", new DeleteCategoryControllerRequest());
		model.addAttribute("UpdateCategoryControllerRequest", new UpdateCategoryControllerRequest());
		return new ModelAndView("/admin/category");
	}

	@PostMapping("/category")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createCategory(@ModelAttribute @Valid CreateCategoryRequest request) {
		categoryService.createCategory(request);
	}

	@PutMapping("/categories-update")
	public void updateCategory(@ModelAttribute @Valid UpdateCategoryControllerRequest controllerRequest) {
		UpdateCategoryServiceRequest serviceRequest =
			new UpdateCategoryServiceRequest(controllerRequest.getCategoryId(), controllerRequest.getName());
		categoryService.updateCategory(serviceRequest);
	}

	@DeleteMapping("/categories-form-delete")
	public void deleteCategory(@ModelAttribute DeleteCategoryControllerRequest deleteCategoryControllerRequest) {
		DeleteCategoryServiceRequest request = new DeleteCategoryServiceRequest(
			deleteCategoryControllerRequest.getCategoryId());
		categoryService.deleteCategory(request);
	}

	@DeleteMapping("/categories-direct-delete/{categoryId}")
	public void deleteCategory(@PathVariable Long categoryId) {
		DeleteCategoryServiceRequest request = new DeleteCategoryServiceRequest(
			categoryId);
		categoryService.deleteCategory(request);
	}

	@GetMapping("/categories/{categoryId}")
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
