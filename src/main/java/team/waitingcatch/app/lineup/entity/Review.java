package team.waitingcatch.app.lineup.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import team.waitingcatch.app.common.entity.TimeStamped;
import team.waitingcatch.app.common.util.StringListConverter;
import team.waitingcatch.app.lineup.dto.CreateReviewEntityRequest;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.user.entitiy.User;

@Entity
@Table(indexes = {
	@Index(name = "pk_review_review_id", columnList = "review_id", unique = true),
	@Index(name = "ix_review_user_id", columnList = "user_id"),
	@Index(name = "ix_review_restaurant_id", columnList = "restaurant_id")})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	@Column(nullable = false)
	private int rate;

	@Convert(converter = StringListConverter.class)
	private final List<String> imagePaths = new ArrayList<>();

	@Column(nullable = false, length = 500)
	private String content;

	@Column(nullable = false)
	private boolean isDeleted;

	public static Review of(CreateReviewEntityRequest entityRequest) {
		return new Review(entityRequest);
	}

	public void softDelete() {
		isDeleted = true;
	}

	private Review(CreateReviewEntityRequest entityRequest) {
		this.user = entityRequest.getUser();
		this.restaurant = entityRequest.getRestaurant();
		this.rate = entityRequest.getRate();
		this.imagePaths.addAll(entityRequest.getImagePaths());
		this.content = entityRequest.getContent();
		this.isDeleted = false;
	}
}