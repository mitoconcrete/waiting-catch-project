package team.waitingcatch.app.restaurant.repository.querydsl;

import static com.querydsl.core.types.dsl.Expressions.*;
import static com.querydsl.core.types.dsl.MathExpressions.*;
import static team.waitingcatch.app.restaurant.entity.QRestaurant.*;
import static team.waitingcatch.app.restaurant.entity.QRestaurantInfo.*;

import java.util.List;

import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.restaurant.QRestaurantsWithinRadiusJpaResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.QSearchRestaurantJpaResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.RestaurantsWithinRadiusJpaResponse;
import team.waitingcatch.app.restaurant.dto.restaurant.SearchRestaurantJpaResponse;

@RequiredArgsConstructor
public class RestaurantInfoRepositoryCustomImpl implements RestaurantInfoRepositoryCustom {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<SearchRestaurantJpaResponse> findRestaurantsBySearchKeywordsContaining(String keyword) {
		return jpaQueryFactory
			.select(
				new QSearchRestaurantJpaResponse(restaurant.id, restaurant.name, restaurant.images, restaurantInfo.rate,
					restaurant.searchKeywords, restaurant.position, restaurantInfo.currentWaitingNumber,
					restaurantInfo.isLineupActive))
			.from(restaurantInfo)
			.join(restaurantInfo.restaurant, restaurant)
			.where(restaurant.searchKeywords.contains(keyword).or(restaurant.name.contains(keyword))
				.and(restaurant.isDeleted.isFalse()))
			.fetch();
	}

	@Override
	public List<RestaurantsWithinRadiusJpaResponse> findRestaurantsByDistance(double latitude, double longitude,
		int distance) {
		NumberExpression<Double> radiansCurrentLat = radians(asNumber(latitude));
		NumberExpression<Double> radiansCurrentLot = radians(asNumber(longitude));
		NumberExpression<Double> radiansLat = radians(restaurant.position.latitude);
		NumberExpression<Double> radiansLot = radians(restaurant.position.longitude);

		return jpaQueryFactory
			.select(new QRestaurantsWithinRadiusJpaResponse(restaurant.id, restaurant.name, restaurant.images,
				restaurantInfo.rate,
				restaurant.searchKeywords, restaurant.position, restaurantInfo.currentWaitingNumber,
				restaurantInfo.isLineupActive))
			.from(restaurantInfo)
			.join(restaurantInfo.restaurant, restaurant)
			.where(acos(cos(radiansCurrentLat)
				.multiply(cos(radiansLat))
				.multiply(cos(radiansLot.subtract(radiansCurrentLot)))
				.add(sin(radiansCurrentLat).multiply(sin(radiansLat))))
				.multiply(asNumber(6371))
				.lt(asNumber(distance))
				.and(restaurant.isDeleted.isFalse()))
			.fetch();
	}
}