package team.waitingcatch.app.restaurant.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import team.waitingcatch.app.restaurant.dto.menu.CreateMenuEntityRequest;
import team.waitingcatch.app.restaurant.dto.menu.UpdateMenuEntityRequest;

@Entity
@Table(indexes = {
	@Index(name = "ix_menu_restaurant_id", columnList = "restaurant_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends TimeStamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "restaurant_id", nullable = false)
	private Restaurant restaurant;

	@Column(nullable = false, length = 50)
	private String name;

	@Column(nullable = false)
	private int price;

	private String imagePath;

	@Column(nullable = false)
	private boolean isDeleted;

	public Menu(Restaurant restaurant, String name, int price, String imagePath) {
		this.restaurant = restaurant;
		this.name = name;
		this.price = price;
		this.imagePath = imagePath;
		this.isDeleted = false;
	}

	public static Menu create(CreateMenuEntityRequest request) {
		return new Menu(request.getRestaurant(), request.getName(), request.getPrice(), request.getImage());
	}

	public void update(UpdateMenuEntityRequest request) {
		this.name = request.getName();
		this.price = request.getPrice();
		this.imagePath = request.getImageUrl();
	}
}