package team.waitingcatch.app.restaurant.repository.querydsl;

import static com.querydsl.core.types.dsl.Expressions.asNumber;
import static com.querydsl.core.types.dsl.MathExpressions.acos;
import static com.querydsl.core.types.dsl.MathExpressions.cos;
import static com.querydsl.core.types.dsl.MathExpressions.radians;
import static com.querydsl.core.types.dsl.MathExpressions.sin;
import static team.waitingcatch.app.restaurant.entity.QRestaurant.restaurant;
import static team.waitingcatch.app.restaurant.entity.QRestaurantInfo.restaurantInfo;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.StringTemplate;
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
	public Slice<SearchRestaurantJpaResponse> findRestaurantsBySearchKeywordsContaining(Long id, String keyword,
		Pageable pageable) {
		StringTemplate st = Expressions.stringTemplate("cast({0} as string)", restaurant.searchKeywords);
		List<SearchRestaurantJpaResponse> fetch = jpaQueryFactory
			.select(
				new QSearchRestaurantJpaResponse(
					restaurant.id,
					restaurant.name,
					restaurant.imagePaths,
					restaurantInfo.rate,
					restaurant.searchKeywords,
					restaurant.position,
					restaurantInfo.currentWaitingNumber,
					restaurantInfo.isLineupActive
				))
			.from(restaurantInfo)
			.join(restaurantInfo.restaurant, restaurant)
			.where(
				ltId(id),
				st.contains(keyword)
					.or(restaurant.name.contains(keyword))
					.and(restaurant.isDeleted.isFalse()))
			.orderBy(restaurant.id.desc())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		boolean hasNext = false;

		if (fetch.size() == pageable.getPageSize() + 1) {
			fetch.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(fetch, pageable, hasNext);
	}

	// @Override
	// public Slice<RestaurantsWithinRadiusJpaResponse> findRestaurantsByDistance(Long id, double latitude,
	// 	double longitude,
	// 	int distance, Pageable pageable) {
	// 	NumberExpression<Double> radiansCurrentLat = radians(asNumber(latitude));
	// 	NumberExpression<Double> radiansCurrentLot = radians(asNumber(longitude));
	// 	NumberExpression<Double> radiansLat = radians(restaurant.position.latitude);
	// 	NumberExpression<Double> radiansLot = radians(restaurant.position.longitude);
	//
	// 	List<RestaurantsWithinRadiusJpaResponse> fetch = jpaQueryFactory
	// 		.select(new QRestaurantsWithinRadiusJpaResponse(
	// 			restaurant.id,
	// 			restaurant.name,
	// 			restaurant.imagePaths,
	// 			restaurantInfo.rate,
	// 			restaurant.searchKeywords,
	// 			restaurant.position,
	// 			restaurantInfo.currentWaitingNumber,
	// 			restaurantInfo.isLineupActive
	// 		))
	// 		.from(restaurantInfo)
	// 		.join(restaurantInfo.restaurant, restaurant)
	// 		.where(
	// 			ltId(id),
	// 			acos(cos(radiansCurrentLat)
	// 				.multiply(cos(radiansLat))
	// 				.multiply(cos(radiansLot.subtract(radiansCurrentLot)))
	// 				.add(sin(radiansCurrentLat).multiply(sin(radiansLat))))
	// 				.multiply(asNumber(6371))
	// 				.lt(asNumber(distance))
	// 				.and(restaurant.isDeleted.isFalse()))
	// 		.orderBy(restaurant.id.desc())
	// 		.limit(pageable.getPageSize() + 1)
	// 		.fetch();
	//
	// 	boolean hasNext = false;
	//
	// 	if (fetch.size() == pageable.getPageSize() + 1) {
	// 		fetch.remove(pageable.getPageSize());
	// 		hasNext = true;
	// 	}
	// 	return new SliceImpl<>(fetch, pageable, hasNext);
	// }

	@Override
	public Slice<RestaurantsWithinRadiusJpaResponse> findRestaurantsByLatitudeAndLongitude(
		Double lastDistance, double latitude, double longitude, double maxLatitude, double maxLongitude,
		double minLatitude, double minLongitude, Pageable pageable) {
		NumberExpression<Double> radiansCurrentLat = radians(asNumber(latitude));
		NumberExpression<Double> radiansCurrentLot = radians(asNumber(longitude));
		NumberExpression<Double> radiansLat = radians(restaurant.position.latitude);
		NumberExpression<Double> radiansLot = radians(restaurant.position.longitude);
		// NumberExpression<Double> distanceBetween = Expressions.numberPath(Double.class, "distanceBetween");
		NumberExpression<Double> distanceBetween = (acos(cos(radiansCurrentLat)
			.multiply(cos(radiansLat))
			.multiply(cos(radiansLot.subtract(radiansCurrentLot)))
			.add(sin(radiansCurrentLat).multiply(sin(radiansLat))))
			.multiply(asNumber(6371)));

		BooleanBuilder gtDistance = new BooleanBuilder();

		if (lastDistance != null) {
			gtDistance.and(distanceBetween.gt(lastDistance));
		}

		List<RestaurantsWithinRadiusJpaResponse> content = jpaQueryFactory
			.select(new QRestaurantsWithinRadiusJpaResponse(
				restaurant.id,
				restaurant.name,
				restaurant.imagePaths,
				restaurantInfo.rate,
				restaurant.searchKeywords,
				restaurant.position,
				restaurantInfo.currentWaitingNumber,
				restaurantInfo.isLineupActive,
				// (acos(cos(radiansCurrentLat)
				// 	.multiply(cos(radiansLat))
				// 	.multiply(cos(radiansLot.subtract(radiansCurrentLot)))
				// 	.add(sin(radiansCurrentLat).multiply(sin(radiansLat))))
				// 	.multiply(asNumber(6371))).as("distanceBetween")
				distanceBetween
			))
			.from(restaurantInfo)
			.join(restaurantInfo.restaurant, restaurant)
			.where(
				// ltId(id),
				restaurant.position.latitude.loe(maxLatitude),
				restaurant.position.latitude.goe(minLatitude),
				restaurant.position.longitude.loe(maxLongitude),
				restaurant.position.longitude.goe(minLongitude),
				gtDistance)
			// .orderBy(restaurant.id.desc())
			.orderBy(distanceBetween.asc())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		boolean hasNext = false;

		if (content.size() == pageable.getPageSize() + 1) {
			content.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(content, pageable, hasNext);
	}

	private BooleanExpression ltId(Long id) {
		if (id == null) {
			return null;
		}
		return restaurant.id.lt(id);
	}

	// private BooleanExpression gtDistance(Double lastDistance, NumberPath<Double> distanceBetween) {
	// 	if (lastDistance == null) {
	// 		return null;
	// 	}
	// 	return distanceBetween.gt(lastDistance);
	// }
}