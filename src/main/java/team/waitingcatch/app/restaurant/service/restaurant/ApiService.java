package team.waitingcatch.app.restaurant.service.restaurant;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.common.Address;
import team.waitingcatch.app.common.Position;
import team.waitingcatch.app.restaurant.dto.restaurant.SaveDummyRestaurantRequest;
import team.waitingcatch.app.restaurant.entity.Restaurant;
import team.waitingcatch.app.restaurant.entity.RestaurantInfo;
import team.waitingcatch.app.restaurant.repository.RestaurantInfoRepository;
import team.waitingcatch.app.restaurant.repository.RestaurantRepository;
import team.waitingcatch.app.user.entitiy.User;
import team.waitingcatch.app.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ApiService {
	private final RestaurantRepository restaurantRepository;
	private final RestaurantInfoRepository restaurantInfoRepository;
	private final UserRepository userRepository;

	public HashMap<String, String> getXYMapfromJson(String jsonString) {
		User user = userRepository.findByUsernameAndIsDeletedFalse("seller15").orElseThrow();
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, String> XYMap = new HashMap<>();

		try {
			TypeReference<Map<String, Object>> typeRef
				= new TypeReference<>() {
			};
			Map<String, Object> jsonMap = mapper.readValue(jsonString, typeRef);

			@SuppressWarnings("unchecked")
			List<Map<String, String>> docList
				= (List<Map<String, String>>)jsonMap.get("documents");
			int i = 0;
			for (i = 0; i <= docList.size() - 1; i++) {
				Map<String, String> adList = docList.get(i);
				String placeName = adList.get("place_name");

				String phone = adList.get("phone");

				double latitude = Double.parseDouble(adList.get("y"));
				double longitude = Double.parseDouble(adList.get("x"));

				String addressName = adList.get("road_address_name");
				String province = addressName.substring(0, 2);
				String city = addressName.substring(3, 7);
				String street = addressName.substring(8);
				String reCity = city.replace(" ", "");

				String category = adList.get("category_name");
				String subCategory = category.substring(6, 8);
				String replaceCategory = subCategory.replace(" ", "");

				Position position = new Position(latitude, longitude);
				Address address = new Address(province, reCity, street);

				SaveDummyRestaurantRequest saveDummyRestaurantRequest = new SaveDummyRestaurantRequest(placeName,
					address, position, phone, replaceCategory, user);
				Restaurant restaurant = new Restaurant(saveDummyRestaurantRequest);
				restaurantRepository.save(restaurant);
				RestaurantInfo restaurantInfo = new RestaurantInfo(restaurant, "08:00", "22:00");
				restaurantInfoRepository.save(restaurantInfo);
			}

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return XYMap;
	}
}
