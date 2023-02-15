package team.waitingcatch.app.restaurant.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.menu.CreateMenuControllerRequest;
import team.waitingcatch.app.restaurant.dto.menu.CreateMenuServiceRequest;
import team.waitingcatch.app.restaurant.service.menu.MenuService;

@RestController
@RequiredArgsConstructor
public class MenuController {
	private final MenuService menuService;

	// customer

	// seller
	@PostMapping("/seller/restaurants/{restaurantId}/menus")
	@ResponseStatus(value = HttpStatus.CREATED)
	public void createMenu(@PathVariable Long restaurantId,
		@RequestPart("images") MultipartFile multipartFile,
		@RequestPart("requestDto") @Valid CreateMenuControllerRequest request) {

		CreateMenuServiceRequest serviceRequest = new CreateMenuServiceRequest(restaurantId, multipartFile, request);
		menuService.createMenu(serviceRequest);
	}
}
