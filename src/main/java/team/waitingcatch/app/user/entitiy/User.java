package team.waitingcatch.app.user.entitiy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.common.entity.TimeStamped;
import team.waitingcatch.app.user.enums.UserRoleEnum;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	// 아이디는 고유해야하므로, unique 값으로 둡니다.
	@Column(nullable = false, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String nickname;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserRoleEnum role;

	@Column(nullable = false)
	private Boolean isBanned;

	@Column(nullable = false)
	private Boolean isDeleted;

	public User(UserRoleEnum role, String name, String email, String username, String password, String nickname) {
		this.role = role;
		this.name = name;
		this.email = email;
		this.username = username;
		this.nickname = nickname;

		// 패스워드를 변환하여 저장합니다. 패스워드의 저장과 검증은 엔티티의 책임이라고 생각하여 이곳에 배치하였습니다.
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		this.password = passwordEncoder.encode(password);
	}

	public Boolean hasSameRole(UserRoleEnum role) {
		return this.role.equals(role);
	}
}
