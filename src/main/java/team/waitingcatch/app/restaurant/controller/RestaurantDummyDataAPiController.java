package team.waitingcatch.app.restaurant.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.restaurant.DummyApiRequest;
import team.waitingcatch.app.restaurant.service.restaurant.ApiService;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantDummyDataAPiController {

	private final ApiService apiService;

	@Value("${kakao.key}")
	private String kakao;

	@GetMapping("/kakao")
	public String kakao(@RequestBody DummyApiRequest dummyApiRequest) throws IOException {
		String jsonString;
		URL url = new URL("https://dapi.kakao.com/v2/local/search/category.json?y=" + dummyApiRequest.getY() + "&x="
			+ dummyApiRequest.getX() + "&category_group_code=FD6&radius=20000&page=" + dummyApiRequest.getPage());
		HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
		urlConnection.setRequestMethod("GET");
		urlConnection.setRequestProperty("Content-type", "application/json");
		urlConnection.setRequestProperty("Authorization", "KakaoAK " + kakao);
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
		String line;
		StringBuilder result = new StringBuilder();
		while ((line = br.readLine()) != null) {
			result.append(line);
		}
		jsonString = result.toString();
		br.close();
		apiService.getXYMapfromJson(jsonString);
		return jsonString;
	}

}
