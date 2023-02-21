package team.waitingcatch.app.common.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DistanceCalculatorTest {
	@Autowired
	private DistanceCalculator distanceCalculator;

	@Test
	@DisplayName("위도 경도를 사용한 거리계산")
	void distanceInKilometerByHaversine() {
		double latitude1 = 37.353481;
		double longitude1 = 127.108859;
		double latitude2 = 37.359874;
		double longitude2 = 127.108055;
		double distance = distanceCalculator.distanceInKilometerByHaversine(latitude1, longitude1, latitude2,
			longitude2);

		assertEquals(0.7144122188127299, distance);
	}

}