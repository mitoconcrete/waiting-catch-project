package team.waitingcatch.app.restaurant.service.menu;

import static team.waitingcatch.app.common.enums.ImageDirectoryEnum.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.util.image.ImageUploader;
import team.waitingcatch.app.restaurant.dto.menu.CreateMenuEntityRequest;
import team.waitingcatch.app.restaurant.dto.menu.CreateMenuServiceRequest;
import team.waitingcatch.app.restaurant.dto.menu.CustomerMenuResponse;
import team.waitingcatch.app.restaurant.dto.menu.DeleteMenuServiceRequest;
import team.waitingcatch.app.restaurant.dto.menu.MenuResponse;
import team.waitingcatch.app.restaurant.dto.menu.UpdateMenuEntityRequest;
import team.waitingcatch.app.restaurant.dto.menu.UpdateMenuServiceRequest;
import team.waitingcatch.app.restaurant.entity.Menu;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.repository.MenuRepository;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;
import team.waitingcatch.app.restaurant.service.restaurant.InternalRestaurantService;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuServiceImpl implements MenuService, InternalMenuService {
	private final MenuRepository menuRepository;
	private final InternalRestaurantService restaurantService;
	private final ImageUploader imageUploader;
	private final RestaurantRepository restaurantRepository;

	@Override
	@Transactional(readOnly = true)
	public List<CustomerMenuResponse> getRestaurantMenus(Long restaurantId) {
		return _getMenusByRestaurantId(restaurantId).stream()
			.map(CustomerMenuResponse::new)
			.collect(Collectors.toList());
	}

	@Override
	public void createMenu(CreateMenuServiceRequest serviceRequest) {
		Restaurant restaurant = restaurantService._getRestaurantByUserId(serviceRequest.getId());
		String name = serviceRequest.getName();
		int price = serviceRequest.getPrice();
		String imageUrl = null;

		if (!serviceRequest.getMultipartFile().isEmpty()) {
			try {
				imageUrl = imageUploader.upload(serviceRequest.getMultipartFile(), MENU.getValue());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		CreateMenuEntityRequest entityRequest = new CreateMenuEntityRequest(restaurant, name, price, imageUrl);
		Menu menu = Menu.create(entityRequest);

		menuRepository.save(menu);
	}

	@Override
	@Transactional(readOnly = true)
	public List<MenuResponse> getMyRestaurantMenus(Long id) {
		Restaurant restaurant = restaurantService._getRestaurantByUserId(id);
		return _getMenusByRestaurantId(restaurant.getId()).stream()
			.map(MenuResponse::new)
			.collect(Collectors.toList());
	}

	@Override
	public void updateMenu(UpdateMenuServiceRequest serviceRequest) {
		restaurantService._getRestaurantByUserId(serviceRequest.getId());
		Menu menu = _getMenuById(serviceRequest.getMenuId());
		String name = serviceRequest.getName();
		int price = serviceRequest.getPrice();
		String imageUrl = menu.getImagePath();

		if (!serviceRequest.getMultipartFile().isEmpty()) {
			try {
				if (!imageUrl.equals("기본 메뉴 이미지 URL")) {
					imageUploader.delete(imageUrl);
				}
				imageUrl = imageUploader.upload(serviceRequest.getMultipartFile(), MENU.getValue());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		UpdateMenuEntityRequest entityRequest = new UpdateMenuEntityRequest(name, price, imageUrl);
		menu.update(entityRequest);
	}

	@Override
	public void deleteMenu(DeleteMenuServiceRequest request) {
		restaurantService._getRestaurantByUserId(request.getSellerId());
		Menu menu = _getMenuById(request.getMenuId());
		imageUploader.delete(menu.getImagePath());
		menuRepository.delete(menu);
	}

	@Override
	public List<Menu> _getMenusByRestaurantId(Long restaurantId) {
		return menuRepository.findAllByRestaurantId(restaurantId);
	}

	@Override
	public Menu _getMenuById(Long menuId) {
		return menuRepository.findById(menuId).orElseThrow(
			() -> new IllegalArgumentException("해당 메뉴가 없습니다.")
		);
	}

	@Override
	public void _bulkSoftDeleteByRestaurantId(Long restaurantId) {
		menuRepository.bulkSoftDeleteByRestaurantId(restaurantId);
	}

}