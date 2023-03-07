package team.waitingcatch.app.event.dto.couponcreator;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

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
	@NotNull
	@Future
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime expireDate;

}
