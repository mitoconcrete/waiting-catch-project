package team.waitingcatch.app.restaurant.dto.requestseller;

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
	@NotEmpty(message = "사용자 아이디는 필수 입력값입니다.")
	@Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])[a-z0-9]{4,10}$",
		message = "4자 이상 10자 이내여야하며, 하나 이상의 알파벳 소문자와 숫자의 조합으로 이뤄져야 합니다.")
	private String username;

	@NotEmpty(message = "사용자 이름은 필수 입력값입니다.")
	@Size(min = 2, max = 5, message = "최소 2자 이상 5자 이하여야 함")
	@Pattern(regexp = "^[가-힣a-zA-Z]*$", message = "알파벳 또는 한글로 구성되야 함")
	private String name;
	@NotEmpty(message = "이메일은 필수 입력값입니다.")
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$",
		message = "이메일 형식을 맞춰주세요.")
	private String email;
	@NotEmpty(message = "전화번호는 필수 입력값입니다.")
	@Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$")
	private String phoneNumber;
	@NotEmpty(message = "레스토랑 이름은 필수 입력값입니다.")
	@Pattern(regexp = "[가-힣a-zA-Z1-9]{1,12}", message = "레스토랑 이름은 한글,영어,숫자 포함해서 12자리 이내로 입력해주세요.")
	private String restaurantName;
	@NotEmpty(message = "카테고리는 필수 입력값입니다.")
	private String categories;
	@NotEmpty(message = "레스토랑 설명은 필수 입력값입니다.")
	private String description;
	@NotNull
	private double latitude;
	@NotNull
	private double longitude;
	@NotNull
	private String province;
	@NotNull
	private String city;
	@NotNull
	private String street;
	@NotEmpty(message = "사업자번호는 필수 입력값입니다.")
	private String businessLicenseNo;
	@NotNull
	private String searchKeyWords;

}
