package team.waitingcatch.app.restaurant.service.restaurant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
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
public class MapApiService {
	private final RestaurantRepository restaurantRepository;
	private final RestaurantInfoRepository restaurantInfoRepository;
	private final UserRepository userRepository;

	@Value("${kakao.key}")
	private String apiKey;

	public void getXYMapFromJson(String jsonString) {
		User user = userRepository.findByUsernameAndIsDeletedFalse("seller15").orElseThrow();
		ObjectMapper mapper = new ObjectMapper();

		try {
			TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
			};
			Map<String, Object> jsonMap = mapper.readValue(jsonString, typeRef);

			@SuppressWarnings("unchecked")
			List<Map<String, String>> docList
				= (List<Map<String, String>>)jsonMap.get("documents");

			for (Map<String, String> adList : docList) {
				String placeName = adList.get("place_name");

				String phone = adList.get("phone");

				double latitude = Double.parseDouble(adList.get("y"));
				double longitude = Double.parseDouble(adList.get("x"));

				String zipCode = "";
				String address = adList.get("road_address_name");
				String detailAddress = "detailAddress";
				// String province = addressName.substring(0, 2);
				// String city = addressName.substring(3, 7);
				// String street = addressName.substring(8);
				// String reCity = city.replace(" ", "");

				String category = adList.get("category_name");
				String subCategory = category.substring(6, 8);
				String replaceCategory = subCategory.replace(" ", "");

				Position position = new Position(latitude, longitude);
				// Address address = new Address(province, reCity, street);

				List<String> categoryList = new ArrayList<>();
				categoryList.add(replaceCategory);

				SaveDummyRestaurantRequest saveDummyRestaurantRequest = new SaveDummyRestaurantRequest(placeName,
					zipCode, address, detailAddress, position, phone, replaceCategory, user, categoryList);
				// DemandSignUpSellerControllerRequest corequest = new DemandSignUpSellerControllerRequest();
				// corequest.setEmail("");
				// corequest.setPhoneNumber(phone);
				// corequest.getCategories().add(replaceCategory);
				// corequest.setRestaurantName(placeName);
				//
				// DemandSignUpSellerServiceRequest request = new DemandSignUpSellerServiceRequest(corequest,
				// 	position);
				// SellerManagement sellerManagement = new SellerManagement(request);
				Restaurant restaurant = new Restaurant(saveDummyRestaurantRequest);
				restaurantRepository.save(restaurant);
				RestaurantInfo restaurantInfo = new RestaurantInfo(restaurant);

				restaurantInfoRepository.save(restaurantInfo);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Position getPosition(String query) {
		String apiUrl = "http://dapi.kakao.com/v2/local/search/address.json";
		String jsonString = null;

		ObjectMapper mapper = new ObjectMapper();

		try {
			query = URLEncoder.encode(query, "UTF-8");
			String addr = apiUrl + "?query=" + query;

			URL url = new URL(addr);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-type", "application/json");
			conn.setRequestProperty("Authorization", "KakaoAK " + apiKey);

			BufferedReader rd = null;
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
			StringBuilder docJson = new StringBuilder();

			String line;

			while ((line = rd.readLine()) != null) {
				docJson.append(line);
			}

			jsonString = docJson.toString();
			rd.close();

			TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
			};
			Map<String, Object> jsonMap = mapper.readValue(jsonString, typeRef);

			List<Map<String, String>> docList = (List<Map<String, String>>) jsonMap.get("documents");
			Map<String, String> adList = docList.get(0);

			double latitude = Double.parseDouble(adList.get("y"));
			double longitude = Double.parseDouble(adList.get("x"));

			return new Position(latitude, longitude);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}