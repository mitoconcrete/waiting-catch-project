package team.waitingcatch.app.event.dto.couponcreator;

import java.time.LocalDateTime;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.waitingcatch.app.event.enums.CouponTypeEnum;

@Getter
@Setter
@NoArgsConstructor
public class UpdateCouponCreatorControllerRequest {
	@NotNull(message = "쿠폰 이름을 입력하세요.")
	@Size(min = 2, max = 20, message = "쿠폰생성자 이름은 최소 2글자에서 20글자 사이어야합니다.")
	private String name;

	@NotNull(message = "할인액(율)을 입력하세요.")
	private int discountPrice;

	@NotNull(message = "할인 종류를 선택하세요.")
	private CouponTypeEnum discountType;

	@NotNull(message = "수량을 입력하세요.")
	private int quantity;

	@NotNull(message = "만료일을 입력하세요.")
	@Future
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime expireDate;

}
