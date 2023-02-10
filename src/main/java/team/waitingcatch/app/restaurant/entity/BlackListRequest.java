package team.waitingcatch.app.restaurant.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.sun.istack.NotNull;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BlackListRequest extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "black_list_request_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@NotNull
	@Column(name = "is_accepted", nullable = false)
	private boolean isAccepted;
	@Column(nullable = false)
	private String description;

}