package team.waitingcatch.app.event.dto.couponcreator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.waitingcatch.app.event.enums.CouponTypeEnum;

@Getter
@Setter
@NoArgsConstructor
public class CreateCouponCreatorControllerRequest {
	@NotNull
	@Size(min = 2, max = 20, message = "쿠폰생성자 이름은 최소 2글자에서 5글자 사이어야합니다.")
	private String name;

	@NotNull
	private int discountPrice;

	@NotNull
	private CouponTypeEnum discountType;

	@NotNull
	private int quantity;

	@NotNull
	@Future
	@DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
	//@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2} [0-9]{2}:[0-9]{2}$", message = "YYYY-MM-DD HH:MM 형식으로 입력해주세요")
	private LocalDateTime expireDate;



}
