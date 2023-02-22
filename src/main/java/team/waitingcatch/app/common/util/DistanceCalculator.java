package team.waitingcatch.app.common.util;

import org.springframework.stereotype.Service;

@Service
public class DistanceCalculator {
	public double distanceInKilometerByHaversine(double x1, double y1, double x2, double y2) {
		double distance;
		double radius = 6371;// 지구 반지름(km)
		double toRadian = Math.PI / 180;
		double deltaLatitude = Math.abs(x1 - x2) * toRadian;
		double deltaLongitude = Math.abs(y1 - y2) * toRadian;
		double sinDeltaLat = Math.sin(deltaLatitude / 2);
		double sinDeltaLng = Math.sin(deltaLongitude / 2);
		double squareRoot = Math.sqrt(
			sinDeltaLat * sinDeltaLat + Math.cos(x1 * toRadian) * Math.cos(x2 * toRadian) * sinDeltaLng * sinDeltaLng);
		distance = 2 * radius * Math.asin(squareRoot);
		return distance;
	}
}
