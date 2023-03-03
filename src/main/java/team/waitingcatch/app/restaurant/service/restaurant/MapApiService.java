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
import team.waitingcatch.app.restaurant.dto.requestseller.DummyAPIdata;
import team.waitingcatch.app.restaurant.entity.SellerManagement;
import team.waitingcatch.app.restaurant.repository.SellerManagementRepository;
import team.waitingcatch.app.restaurant.service.requestseller.SellerManagementService;

@Service
@RequiredArgsConstructor
public class MapApiService {
	private final SellerManagementRepository sellerManagementRepository;
	@Value("${kakao.key}")
	private String apiKey;

	private final SellerManagementService sellerManagementService;

	public void getXYMapFromJson(String jsonString, String username, String name, String email, String nullPhone) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {
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

				String zipCode = "";
				String address = adList.get("road_address_name");
				String detailAddress = "detailAddress";

				String category = adList.get("category_name");
				String subCategory = category.substring(6, 8);
				String replaceCategory = subCategory.replace(" ", "");

				Position position = new Position(latitude, longitude);
				List<String> categoryList = new ArrayList<>();
				categoryList.add(replaceCategory);
				//
				// SaveDummyRestaurantRequest saveDummyRestaurantRequest = new SaveDummyRestaurantRequest(placeName,
				// 	zipCode, address, detailAddress, position, phone, replaceCategory, user, categoryList);

				DummyAPIdata request = new DummyAPIdata(username, name, email, phone, placeName, categoryList, position,
					address, nullPhone);
				SellerManagement sellerManagement = new SellerManagement(request);
				sellerManagementRepository.save(sellerManagement);
				sellerManagementService.approveDummySignUpSeller();
				//				Restaurant restaurant = new Restaurant(saveDummyRestaurantRequest);
				//				restaurantRepository.save(restaurant);
				//				RestaurantInfo restaurantInfo = new RestaurantInfo(restaurant);
				//				restaurantInfoRepository.save(restaurantInfo);
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

			List<Map<String, String>> docList
				= (List<Map<String, String>>)jsonMap.get("documents");
			Map<String, String> adList = docList.get(0);

			double latitude = Double.parseDouble(adList.get("y"));
			double longitude = Double.parseDouble(adList.get("x"));

			return new Position(latitude, longitude);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}