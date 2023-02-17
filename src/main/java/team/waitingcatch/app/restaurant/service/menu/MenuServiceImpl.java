package team.waitingcatch.app.restaurant.service.menu;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.util.S3Uploader;
import team.waitingcatch.app.restaurant.dto.menu.CreateMenuEntityRequest;
import team.waitingcatch.app.restaurant.dto.menu.CreateMenuServiceRequest;
import team.waitingcatch.app.restaurant.dto.menu.CustomerMenuResponse;
import team.waitingcatch.app.restaurant.dto.menu.MenuResponse;
import team.waitingcatch.app.restaurant.dto.menu.UpdateMenuEntityRequest;
import team.waitingcatch.app.restaurant.dto.menu.UpdateMenuServiceRequest;
import team.waitingcatch.app.restaurant.entity.Menu;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.repository.MenuRepository;
import team.waitingcatch.app.restaurant.service.restaurant.InternalRestaurantService;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuServiceImpl implements MenuService, InternalMenuService {
	private final MenuRepository menuRepository;
	private final InternalRestaurantService restaurantService;
	private final S3Uploader s3Uploader;

	@Override
	@Transactional(readOnly = true)
	public List<CustomerMenuResponse> getRestaurantMenus(Long restaurantId) {
		return _getMenusByRestaurantId(restaurantId).stream()
			.map(CustomerMenuResponse::new)
			.collect(Collectors.toList());
	}

	@Override
	public void createMenu(CreateMenuServiceRequest serviceRequest) {
		Restaurant restaurant = restaurantService._getRestaurant(serviceRequest.getRestaurantId());
		String name = serviceRequest.getName();
		int price = serviceRequest.getPrice();
		String imageUrl = "기본 메뉴 이미지 URL";

		if (!serviceRequest.getMultipartFile().isEmpty()) {
			try {
				imageUrl = s3Uploader.upload(serviceRequest.getMultipartFile(), "menu");
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
	public List<MenuResponse> getMyRestaurantMenus(Long restaurantId) {
		return _getMenusByRestaurantId(restaurantId).stream()
			.map(MenuResponse::new)
			.collect(Collectors.toList());
	}

	@Override
	public void updateMenu(UpdateMenuServiceRequest serviceRequest) {
		Menu menu = _getMenuById(serviceRequest.getMenuId());
		String name = serviceRequest.getName();
		int price = serviceRequest.getPrice();
		String imageUrl = menu.getImages();

		if (!imageUrl.equals("기본 메뉴 이미지 URL")) {
			s3Uploader.deleteS3(imageUrl);
		}

		if (!serviceRequest.getMultipartFile().isEmpty()) {
			try {
				imageUrl = s3Uploader.upload(serviceRequest.getMultipartFile(), "menu");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

		UpdateMenuEntityRequest entityRequest = new UpdateMenuEntityRequest(name, price, imageUrl);
		menu.update(entityRequest);
	}

	@Override
	public void deleteMenu(Long menuId) {
		Menu menu = _getMenuById(menuId);
		s3Uploader.deleteS3(menu.getImages());
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

}
