package team.waitingcatch.app.restaurant.dto.menu;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;

@Getter
public class CreateMenuServiceRequest {
	private final Long id;
	private final String name;
	private final int price;
	private final MultipartFile multipartFile;

	public CreateMenuServiceRequest(Long id, MultipartFile multipartFile,
		CreateMenuControllerRequest request) {
		this.id = id;
		this.name = request.getName();
		this.price = request.getPrice();
		this.multipartFile = multipartFile;
	}
}