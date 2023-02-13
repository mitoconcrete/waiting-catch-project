package team.waitingcatch.app.user.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.user.enums.UserRoleEnum;

@Getter
@RequiredArgsConstructor
public class GetCustomerByIdAndRoleServiceRequest {
	private final Long userId;
	private final UserRoleEnum role;
}
