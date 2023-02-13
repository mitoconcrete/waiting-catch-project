package team.waitingcatch.app.event.service.couponcreator;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.dto.couponcreator.CreateCouponCreatorControllerRequest;
import team.waitingcatch.app.event.dto.couponcreator.PutCouponCreatorControllerRequest;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponCreatorServiceImpl implements CouponCreatorService, InternalCouponCreatorService {
	@Override
	public String createCouponCreator(CreateCouponCreatorControllerRequest createCouponCreatorControllerRequest) {

		return "";
	}

	@Override
	public String putCouponCreator(PutCouponCreatorControllerRequest putCouponCreatorControllerRequest) {
		return "";
	}
}
