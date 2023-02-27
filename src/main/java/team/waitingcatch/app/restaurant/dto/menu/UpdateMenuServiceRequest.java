package team.waitingcatch.app.restaurant.dto.menu;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;

@Getter
public class UpdateMenuServiceRequest {
	private final Long menuId;
	private final String name;
	private final int price;
	private final MultipartFile multipartFile;
	private final Long id;

	public UpdateMenuServiceRequest(Long menuId, UpdateMenuControllerRequest request, MultipartFile multipartFile,
		Long id) {
		this.menuId = menuId;
		this.name = request.getName();
		this.price = request.getPrice();
		this.multipartFile = multipartFile;
		this.id = id;
	}
}
