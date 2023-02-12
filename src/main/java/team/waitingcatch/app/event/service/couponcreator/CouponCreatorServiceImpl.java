package team.waitingcatch.app.event.service.couponcreator;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponCreatorServiceImpl implements CouponCreatorService, InternalCouponCreatorService {
}
