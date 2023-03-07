package team.waitingcatch.app.restaurant.dto.menu;

import lombok.Getter;

@Getter
public class DeleteMenuServiceRequest {

	private final Long sellerId;
	private final Long menuId;

	public DeleteMenuServiceRequest(Long sellerId, Long menuId) {
		this.sellerId = sellerId;
		this.menuId = menuId;
	}
}