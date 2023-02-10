package team.waitingcatch.app.user.entitiy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.common.entity.TimeStamped;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false)
	private Long id;

	@Column(nullable = false)
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
	private String businessLicenseNo;

	@Enumerated(EnumType.STRING)
	private UserRoleEnum role;

	@Column(nullable = false)
	private Boolean isBanned;

	@Column(nullable = false)
	private Boolean isDeleted;
}
