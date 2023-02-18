package team.waitingcatch.app.restaurant.repository.querydsl;

import static com.querydsl.core.types.dsl.Expressions.asNumber;
import static com.querydsl.core.types.dsl.MathExpressions.acos;
import static com.querydsl.core.types.dsl.MathExpressions.cos;
import static com.querydsl.core.types.dsl.MathExpressions.radians;
import static com.querydsl.core.types.dsl.MathExpressions.sin;
import static team.waitingcatch.app.restaurant.entity.QRestaurant.restaurant;
import static team.waitingcatch.app.restaurant.entity.QRestaurantInfo.restaurantInfo;

import java.util.List;

import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.restaurant.QRestaurantsWithin3kmRadiusJpaResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.QSearchRestaurantJpaResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantsWithin3kmRadiusJpaResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantJpaResponse;

@RequiredArgsConstructor
public class RestaurantInfoRepositoryCustomImpl implements RestaurantInfoRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<SearchRestaurantJpaResponse> findRestaurantsBySearchKeywordsContaining(String keyword) {
		return jpaQueryFactory
			.select(new QSearchRestaurantJpaResponse(restaurant.name, restaurant.images, restaurantInfo.rate,
				restaurant.searchKeywords, restaurant.position, restaurantInfo.currentWaitingNumber,
				restaurantInfo.isLineupActiveStatus))
			.from(restaurantInfo)
			.join(restaurantInfo.restaurant, restaurant)
			.where(restaurant.searchKeywords.contains(keyword)
				.and(restaurant.isDeleted.isFalse()))
			.fetch();
	}

	@Override
	public List<RestaurantsWithin3kmRadiusJpaResponse> findRestaurantsByDistance(double latitude, double longitude) {
		NumberExpression<Double> radiansCurrentLat = radians(asNumber(latitude));
		NumberExpression<Double> radiansCurrentLot = radians(asNumber(longitude));
		NumberExpression<Double> radiansLat = radians(restaurant.position.latitude);
		NumberExpression<Double> radiansLot = radians(restaurant.position.longitude);

		return jpaQueryFactory
			.select(new QRestaurantsWithin3kmRadiusJpaResponse(restaurant.name, restaurant.images, restaurantInfo.rate,
				restaurant.searchKeywords, restaurant.position, restaurantInfo.currentWaitingNumber,
				restaurantInfo.isLineupActiveStatus))
			.from(restaurantInfo)
			.join(restaurantInfo.restaurant, restaurant)
			.where(acos(cos(radiansCurrentLat)
				.multiply(cos(radiansLat))
				.multiply(cos(radiansLot.subtract(radiansCurrentLot)))
				.add(sin(radiansCurrentLat).multiply(sin(radiansLat))))
				.multiply(asNumber(6371))
				.lt(asNumber(3))
				.and(restaurant.isDeleted.isFalse()))
			.fetch();

	}
}
