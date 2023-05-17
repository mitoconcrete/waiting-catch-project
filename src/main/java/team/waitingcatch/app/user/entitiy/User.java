package team.waitingcatch.app.user.entitiy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.SQLDelete;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.common.entity.TimeStamped;
import team.waitingcatch.app.user.enums.UserRoleEnum;

@Entity
@SQLDelete(sql = "UPDATE user SET is_deleted = true WHERE user_id = ?")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(nullable = false, length = 20, unique = true)
	private String username;

	@Column(nullable = false)
	private String password;

	@Column(unique = true, length = 15)
	private String nickname;

	@Column(nullable = false, length = 30)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false, length = 13, unique = true)
	private String phoneNumber;

	@Column(nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private UserRoleEnum role;

	@Column(nullable = false)
	private boolean isBanned;

	@Column(nullable = false)
	private boolean isDeleted;

	public User(UserRoleEnum role, String name, String email, String username, String password, String nickname,
		String phoneNumber) {
		this.role = role;
		this.name = name;
		this.email = email;
		this.username = username;
		this.nickname = nickname;
		this.phoneNumber = phoneNumber;
		this.isBanned = false;
		this.isDeleted = false;

		// 패스워드를 변환하여 저장합니다. 패스워드의 저장과 검증은 엔티티의 책임이라고 생각하여 이곳에 배치하였습니다.
		updatePassword(password);
	}

	public boolean hasSameRole(UserRoleEnum role) {
		return this.role == role;
	}

	public void updateBasicInfo(String nickname, String name, String phoneNumber, String email) {
		if (nickname != null) {
			this.nickname = nickname;
		}

		if (name != null) {
			this.name = name;
		}

		if (phoneNumber != null) {
			this.phoneNumber = phoneNumber;
		}

		if (email != null) {
			this.email = email;
		}
	}

	public void updatePassword(String password) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		this.password = passwordEncoder.encode(password);
	}

	public boolean isPasswordMatch(String password) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.matches(password, this.password);
	}

	public boolean equals(final Object o) {
		if (o == this)
			return true;
		if (!(o instanceof User))
			return false;
		final User other = (User)o;
		if (!other.canEqual((Object)this))
			return false;
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id))
			return false;
		return true;
	}

	protected boolean canEqual(final Object other) {
		return other instanceof User;
	}

	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		return result;
	}
}