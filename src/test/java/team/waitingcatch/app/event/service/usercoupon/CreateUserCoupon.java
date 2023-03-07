package team.waitingcatch.app.event.service.usercoupon;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import team.waitingcatch.app.event.dto.couponcreator.CreateAdminCouponCreatorRequest;
import team.waitingcatch.app.event.dto.couponcreator.CreateAdminCouponCreatorServiceRequest;
import team.waitingcatch.app.event.dto.couponcreator.CreateCouponCreatorControllerRequest;
import team.waitingcatch.app.event.dto.usercoupon.CreateUserCouponServiceRequest;
import team.waitingcatch.app.event.entity.CouponCreator;
import team.waitingcatch.app.event.entity.Event;
import team.waitingcatch.app.event.enums.CouponTypeEnum;
import team.waitingcatch.app.event.repository.CouponCreatorRepository;

@SpringBootTest
@Transactional
public class CreateUserCoupon {
	@Autowired
	UserCouponServiceImpl userCouponService;
	@Autowired
	CouponCreatorRepository couponCreatorRepository;

	@Test
	@Commit
	public void CreateUserCoupon() {
		Long id = 1L;
		Event event = null;
		String name = "aa";
		int discountPrice = 1000;
		CouponTypeEnum discountType = CouponTypeEnum.PRICE;
		int quantity = 10;
		LocalDateTime expireDate = LocalDateTime.now().plusDays(1);
		boolean isDeleted = false;
		CreateCouponCreatorControllerRequest request2 = new CreateCouponCreatorControllerRequest();
		request2.setName(name);
		request2.setDiscountPrice(discountPrice);
		request2.setDiscountType(discountType);
		request2.setQuantity(quantity);
		request2.setExpireDate(expireDate);
		CreateAdminCouponCreatorServiceRequest request1 = new CreateAdminCouponCreatorServiceRequest(request2, 1L);
		CreateAdminCouponCreatorRequest request = new CreateAdminCouponCreatorRequest(request1, event);
		CouponCreator couponCreator = new CouponCreator(request, id);
		Long creatorId = 1L;
		String username = "aa";
		CreateUserCouponServiceRequest request3 = new CreateUserCouponServiceRequest(creatorId, username);
		System.out.println(couponCreatorRepository.findById(id).get() + "아이디");
		userCouponService.createUserCoupon(request3);

	}
}

