package team.waitingcatch.app.restaurant.dto.requestseller;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DemandSignUpSellerControllerRequest {
	@NotNull
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])[a-z0-9]{4,10}$",
		message = "4자 이상 10자 이내여야하며, 하나 이상의 알파벳 소문자와 숫자의 조합으로 이뤄져야 합니다.")
	private String username;

	@NotNull
	@Size(min = 2, max = 5, message = "이름은 최소 2글자에서 5글자 사이어야합니다.")
	private String name;

	@NotNull
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식을 맞춰주세요.")
	private String email;

	@NotNull
	@Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$", message = "올바른 전화번호 형식을 입력하세요.")
	private String phoneNumber;

	@NotNull(message = "레스토랑 이름은 필수 입력값입니다.")
	@Pattern(regexp = "[가-힣a-zA-Z1-9]{1,12}", message = "레스토랑 이름은 한글,영어,숫자 포함해서 12자리 이내로 입력해주세요.")
	private String restaurantName;

	@NotBlank(message = "카테고리는 필수 입력값입니다.")
	private String categories;

	@NotBlank(message = "레스토랑 설명은 필수 입력값입니다.")
	private String description;

	// private double latitude;

	// private double longitude;

	// @NotBlank
	// private String province;
	//
	// @NotBlank
	// private String city;
	//
	// @NotBlank
	// private String street;

	@NotEmpty(message = "사업자번호는 필수 입력값입니다.")
	private String businessLicenseNo;

	@NotBlank(message = "주소는 필수 입력값입니다.")
	private String zipCode;

	@NotBlank(message = "주소는 필수 입력값입니다.")
	private String address;

	@NotBlank(message = "주소는 필수 입력값입니다.")
	private String detailAddress;

	// @NotBlank
	// private String searchKeyWords;
}