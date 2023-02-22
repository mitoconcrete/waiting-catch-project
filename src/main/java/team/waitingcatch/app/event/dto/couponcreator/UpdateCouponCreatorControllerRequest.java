package team.waitingcatch.app.event.dto.couponcreator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.waitingcatch.app.event.enums.CouponTypeEnum;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCouponCreatorControllerRequest {
	private String name;
	private int discountPrice;
	private CouponTypeEnum discountType;
	private int quantity;
	private String expireDate;

	public LocalDateTime getExpireDate() {
		return LocalDateTime.parse(expireDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
	}

	public String getExpireDateString() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}
}

