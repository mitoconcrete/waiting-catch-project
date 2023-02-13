package team.waitingcatch.app.user.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.user.dto.CustomerResponse;
import team.waitingcatch.app.user.dto.DeleteUserRequest;
import team.waitingcatch.app.user.dto.FindPasswordRequest;
import team.waitingcatch.app.user.dto.GetCustomerByIdAndRoleServiceRequest;
import team.waitingcatch.app.user.dto.UpdateUserServiceRequest;
import team.waitingcatch.app.user.dto.UserCreateServiceRequest;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.repository.UserRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, InternalUserService {
	private final JavaMailSender emailSender;
	private final UserRepository userRepository;

	@Value("${spring.mail.username}")
	private String smtpSenderEmail;

	@Override
	public List<CustomerResponse> getCustomers() {
		return userRepository.findAll().stream().map(CustomerResponse::new).collect(Collectors.toList());
	}

	@Override
	public CustomerResponse getByUserIdAndRole(GetCustomerByIdAndRoleServiceRequest payload) {
		// 유저의 존재여부를 판단한다.
		User user = userRepository.findById(payload.getUserId()).orElseThrow(
			() -> new IllegalArgumentException("유저가 존재하지 않습니다.")
		);

		// 입력받은 롤과 동일한 롤을 지니고 있는지 확인한다.
		if (!user.hasSameRole(payload.getRole())) {
			throw new IllegalArgumentException("유저가 존재하지 않습니다.");
		}

		return new CustomerResponse(user);
	}

	@Override
	public void createUser(UserCreateServiceRequest payload) {
		// 이미 존재하는 유저인지 검증합니다.
		Boolean isExistUser = userRepository.existsByUsername(payload.getUsername());

		if (isExistUser) {
			throw new IllegalArgumentException("이미 존재하는 유저입니다.");
		}

		// 새로운 유저를 생성하고 저장합니다.
		User newUser =
			new User(
				payload.getRole(),
				payload.getName(),
				payload.getEmail(),
				payload.getUsername(),
				payload.getPassword(),
				payload.getNickname(),
				payload.getPhoneNumber()
			);

		userRepository.save(newUser);
	}

	@Override
	public void updateUser(UpdateUserServiceRequest payload) {
		// 중복되면 안되는 값(이메일, 전화번호, 닉네임)들을 체크해준다.
		User user = _getUserByUsername(payload.getUsername());
		user.updateBasicInfo(payload.getNickName(), payload.getName(), payload.getPhoneNumber(), payload.getEmail());
		userRepository.save(user);
	}

	@Override
	public void deleteUser(DeleteUserRequest payload) {
		User user = _getUserByUsername(payload.getUsername());
		user.remove();
		userRepository.save(user);
	}

	@Override
	public void findUserAndSendEmail(FindPasswordRequest payload) {
		User user = userRepository.findByUsernameAndEmail(payload.getUsername(), payload.getEmail()).orElseThrow(
			() -> new IllegalArgumentException("유저가 존재하지 않습니다.")
		);

		// 유저가 존재하면 임시 비밀번호를 생성하고, 저장한다.
		String temporaryPassword = UUID.randomUUID().toString().substring(0, 10);
		user.updatePassword(temporaryPassword);
		userRepository.save(user);

		// 저장된 번호를 유저에게 메일로 전달한다.
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(smtpSenderEmail);
		message.setTo(user.getEmail());
		message.setSubject("임시패스워드 안내");
		message.setText("안녕하세요. 임시비밀번호 안내 관련 이메일 입니다. 회원님의 임시 비밀번호는 " +
			temporaryPassword + "입니다." + "\n로그인 후에 비밀번호를 변경을 해주세요.");
		emailSender.send(message);
	}

	@Override
	public User _getUserByUsername(String username) {
		return userRepository.findByUsername(username).orElseThrow(
			() -> new IllegalArgumentException("유저가 존재하지 않습니다.")
		);
	}
}

