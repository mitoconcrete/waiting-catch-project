package team.waitingcatch.app.restaurant.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.dto.GenericResponse;
import team.waitingcatch.app.restaurant.dto.menu.CreateMenuControllerRequest;
import team.waitingcatch.app.restaurant.dto.menu.CreateMenuServiceRequest;
import team.waitingcatch.app.restaurant.dto.menu.CustomerMenuResponse;
import team.waitingcatch.app.restaurant.dto.menu.DeleteMenuServiceRequest;
import team.waitingcatch.app.restaurant.dto.menu.MenuResponse;
import team.waitingcatch.app.restaurant.dto.menu.UpdateMenuControllerRequest;
import team.waitingcatch.app.restaurant.dto.menu.UpdateMenuServiceRequest;
import team.waitingcatch.app.restaurant.service.menu.MenuService;
import team.waitingcatch.app.user.entitiy.UserDetailsImpl;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class MenuController {
	private final MenuService menuService;

	// customer
	@GetMapping("/customer/restaurants/{restaurantId}/menus")
	public GenericResponse<List<CustomerMenuResponse>> getRestaurantMenus(@PathVariable Long restaurantId) {
		return new GenericResponse<>(menuService.getRestaurantMenus(restaurantId));
	}

	// seller
	@PostMapping("/seller/restaurants/{restaurantId}/menus")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createMenu(@PathVariable Long restaurantId,
		@RequestPart("images") MultipartFile multipartFile,
		@RequestPart("requestDto") @Valid CreateMenuControllerRequest request) {

		CreateMenuServiceRequest serviceRequest = new CreateMenuServiceRequest(restaurantId, multipartFile, request);
		menuService.createMenu(serviceRequest);
	}

	@GetMapping("/seller/restaurants/{restaurantId}/menus")
	public GenericResponse<List<MenuResponse>> getMyRestaurantMenus(@PathVariable Long restaurantId) {
		return new GenericResponse<>(menuService.getMyRestaurantMenus(restaurantId));
	}

	@PutMapping("/seller/restaurants/{restaurantId}/menus/{menuId}")
	public void updateMenu(@PathVariable Long menuId,
		@RequestPart("images") MultipartFile multipartFile,
		@RequestPart("requestDto") @Valid UpdateMenuControllerRequest request,
		@AuthenticationPrincipal UserDetailsImpl userDetails) {

		UpdateMenuServiceRequest serviceRequest = new UpdateMenuServiceRequest(menuId, request, multipartFile,
			userDetails.getId());
		menuService.updateMenu(serviceRequest);
	}

	@DeleteMapping("/seller/restaurants/{restaurantId}/menus/{menuId}")
	public void deleteMenu(@PathVariable Long menuId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		DeleteMenuServiceRequest deleteMenuServiceRequest = new DeleteMenuServiceRequest(userDetails.getId(), menuId);
		menuService.deleteMenu(deleteMenuServiceRequest);
	}
}