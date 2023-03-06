package team.waitingcatch.app.restaurant.dto.blacklist;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.user.entitiy.User;

@Getter
@RequiredArgsConstructor
public class GetBlacklistDemandCustomerInfoResponse {
	private final long userId;
	private final String nickname;

	public static GetBlacklistDemandCustomerInfoResponse of(User user) {
		return new GetBlacklistDemandCustomerInfoResponse(user.getId(), user.getNickname());
	}
}