package team.waitingcatch.app.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetCustomerPageableRequest {
	private int size;
	private int page;
}
