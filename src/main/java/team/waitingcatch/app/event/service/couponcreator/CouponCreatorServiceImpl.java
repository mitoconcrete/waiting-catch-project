package team.waitingcatch.app.event.service.couponcreator;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.event.dto.couponcreator.createCouponCreatorControllerRequest;
import team.waitingcatch.app.event.dto.couponcreator.putCouponCreatorControllerRequest;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponCreatorServiceImpl implements CouponCreatorService, InternalCouponCreatorService {
	@Override
	public void createCouponCreator(createCouponCreatorControllerRequest createCouponCreatorControllerRequest) {

	}

	@Override
	public void putCouponCreator(putCouponCreatorControllerRequest putCouponCreatorControllerRequest) {

	}
}
