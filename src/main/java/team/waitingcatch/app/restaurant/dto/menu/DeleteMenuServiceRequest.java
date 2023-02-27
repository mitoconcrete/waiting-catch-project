package team.waitingcatch.app.restaurant.dto.menu;

import lombok.Getter;

@Getter
public class DeleteMenuServiceRequest {

	private final Long id;
	private final Long menuId;

	public DeleteMenuServiceRequest(Long id, Long menuId) {
		this.id = id;
		this.menuId = menuId;
	}
}
