package team.waitingcatch.app.restaurant.service.requestseller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;

import team.waitingcatch.app.common.Address;
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.lineup.repository.WaitingNumberRepository;
import team.waitingcatch.app.restaurant.dto.requestseller.DemandSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.GetDemandSignUpSellerResponse;
import team.waitingcatch.app.restaurant.dto.requestseller.GetRequestSellerByRestaurantRequest;
import team.waitingcatch.app.restaurant.dto.requestseller.RejectSignUpSellerServiceRequest;
import team.waitingcatch.app.restaurant.entity.SellerManagement;
import team.waitingcatch.app.restaurant.enums.AcceptedStatusEnum;
import team.waitingcatch.app.restaurant.repository.RestaurantInfoRepository;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;
import team.waitingcatch.app.restaurant.repository.SellerManagementRepository;
import team.waitingcatch.app.user.repository.UserRepository;
import team.waitingcatch.app.user.service.InternalUserService;
import team.waitingcatch.app.user.service.UserService;

@ExtendWith(MockitoExtension.class)
@Nested
class SellerManagementServiceImplTest {

	@Mock
	private SellerManagementRepository sellerManagementRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private RestaurantRepository restaurantRepository;
	@Mock
	private RestaurantInfoRepository restaurantInfoRepository;
	@Mock
	private WaitingNumberRepository waitingNumberRepository;
	@InjectMocks
	private SellerManagementServiceImpl sellerManagementService;
	@Mock
	private InternalUserService internalUserService;
	@Mock
	private UserService userService;
	@Mock
	private JavaMailSender emailSender;

	@Test
	@DisplayName("Request SignUp seller")
	void demandSignUpSeller() {
		DemandSignUpSellerServiceRequest request = mock(DemandSignUpSellerServiceRequest.class);
		when(request.getUsername()).thenReturn("korean");
		when(userRepository.existsByUsername(anyString())).thenReturn(false);
		//when
		sellerManagementService.demandSignUpSeller(request);
		SellerManagement sellerManagement = new SellerManagement(request);
		//then
		verify(sellerManagementRepository, times(1)).save(any(SellerManagement.class));
		assertEquals(sellerManagement.getUsername(), request.getUsername());

	}

	@Test
	@DisplayName("Get Signup seller requested")
	void getDemandSignUpSellers() {
		Pageable pageable = mock(Pageable.class);

		List<SellerManagement> sellerManagementList = new ArrayList<>();
		Page<SellerManagement> pagedResponse = new PageImpl<>(sellerManagementList);

		when(sellerManagementRepository.findAll(pageable)).thenReturn(pagedResponse);
		//when
		Page<GetDemandSignUpSellerResponse> responses = sellerManagementService.getDemandSignUpSellers(
			pageable);
		//then
		assertEquals(1, responses.getTotalPages());
	}

	@Test
	@DisplayName("Get demand signup seller by id")
	void getDemandSignUpSellersById() {
		String any = "kkk";
		Pageable pageable = mock(Pageable.class);
		List<SellerManagement> sellerManagements = new ArrayList<>();
		Page<SellerManagement> page = new PageImpl<>(sellerManagements);
		when(sellerManagementRepository.findByUsernameContaining(any, pageable)).thenReturn(page);

		Page<GetDemandSignUpSellerResponse> responses = sellerManagementService.getDemandSignUpSellersById(any,
			pageable);
		assertEquals(1, responses.getTotalPages());
	}

	@Test
	void approveSignUpSeller() {
	}

	@Test
	@DisplayName("Reject SignUp Seller")
	void rejectSignUpSeller() {
		RejectSignUpSellerServiceRequest request = mock(RejectSignUpSellerServiceRequest.class);
		DemandSignUpSellerServiceRequest demandSignupSellerServiceRequest = mock(
			DemandSignUpSellerServiceRequest.class);

		when(demandSignupSellerServiceRequest.getEmail()).thenReturn("aaa@nate.com");
		when(request.getId()).thenReturn(1L);

		SellerManagement sellerManagement = new SellerManagement(demandSignupSellerServiceRequest);
		when(sellerManagementRepository.findById(anyLong())).thenReturn(Optional.ofNullable(sellerManagement));

		//when
		sellerManagementService.rejectSignUpSeller(request);
		//then
		verify(sellerManagementRepository, times(1)).findById(anyLong());
		assertEquals(sellerManagement.getEmail(), "aaa@nate.com");
		assertEquals(sellerManagement.getStatus(), AcceptedStatusEnum.REJECT);
	}

	@Test
	@DisplayName("Get request seller by restaurant")
	void getRequestSellerByRestaurant() {
		GetRequestSellerByRestaurantRequest request = mock(GetRequestSellerByRestaurantRequest.class);
		Position position = mock(Position.class);
		Address address = mock(Address.class);
		SellerManagement sellerManagement1 = mock(SellerManagement.class);
		when(sellerManagement1.getUsername()).thenReturn("korean");
		when(sellerManagement1.getEmail()).thenReturn("aaa@nate.com");
		when(sellerManagement1.getPosition()).thenReturn(position);
		when(sellerManagement1.getAddress()).thenReturn(address);

		when(sellerManagementRepository.findTopByUsernameAndEmailOrderByCreatedDateDesc(
			request.getRequestSellerName(), request.getEmail()
		))
			.thenReturn(sellerManagement1);
		//when
		GetDemandSignUpSellerResponse response = sellerManagementService.getRequestSellerByRestaurant(request);
		//then
		assertEquals(response.getEmail(), "aaa@nate.com");
		assertEquals(response.getUsername(), "korean");
	}
}