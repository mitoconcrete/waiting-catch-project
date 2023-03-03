package team.waitingcatch.app.restaurant.controller;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.RequiredArgsConstructor;
import team.waitingcatch.app.restaurant.dto.restaurant.DummyApiRequest1;
import team.waitingcatch.app.restaurant.service.restaurant.MapApiService;

@RestController
@RequestMapping("/api")
@JsonInclude(JsonInclude.Include.NON_NULL)
@RequiredArgsConstructor
public class RestaurantDummyDataApiController {
	private final MapApiService mapApiService;

	@Value("${kakao.key}")
	private String kakao;

	@GetMapping("/kakao")
	public String kakao() throws IOException {
		DummyApiRequest1 dummyApiRequest1 = null;
		try {
			FileInputStream file = new FileInputStream("src/main/resources/static/dummydata.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			int rowNo = 0;
			int cellIndex = 0;
			List<String> x = new ArrayList<>();
			List<String> y = new ArrayList<>();

			XSSFSheet sheet = workbook.getSheetAt(0); // 0 번째 시트를 가져온다
			// 만약 시트가 여러개 인 경우 for 문을 이용하여 각각의 시트를 가져온다
			int rows = sheet.getPhysicalNumberOfRows(); // 사용자가 입력한 엑셀 Row수를 가져온다
			for (rowNo = 0; rowNo < rows; rowNo++) {
				XSSFRow row = sheet.getRow(rowNo);
				if (row != null) {
					int cells = row.getPhysicalNumberOfCells(); // 해당 Row에 사용자가 입력한 셀의 수를 가져온다
					for (cellIndex = 0; cellIndex <= cells; cellIndex++) {
						XSSFCell cell = row.getCell(cellIndex); // 셀의 값을 가져온다
						String value = "";
						if (cell == null) { // 빈 셀 체크
							continue;
						} else {
							// 타입 별로 내용을 읽는다
							switch (cell.getCellType()) {
								case XSSFCell.CELL_TYPE_NUMERIC:
									value = cell.getNumericCellValue() + "";
									break;
							}
						}

						if (cellIndex == 0) {
							x.add(value);
						} else if (cellIndex == 1) {
							y.add(value);
						}
					}
				}
			}
			dummyApiRequest1 = new DummyApiRequest1(x, y);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String jsonString;
		List<String> y = dummyApiRequest1.getY();
		List<String> x = dummyApiRequest1.getX();

		for (int i = 0; i < x.size(); i++) {
			URL url = new URL(
				"https://dapi.kakao.com/v2/local/search/category.json?y=" + y.get(i) + "&x="
					+ x.get(i) + "&category_group_code=FD6&radius=10000&page=1" + "&size=1");

			HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setRequestProperty("Content-type", "application/json");
			urlConnection.setRequestProperty("Authorization", "KakaoAK " + kakao);
			BufferedReader br = new BufferedReader(
				new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
			String line;
			StringBuilder result = new StringBuilder();
			while ((line = br.readLine()) != null) {
				result.append(line);
			}
			jsonString = result.toString();
			br.close();
			String username = "sel" + i;
			String name = "se" + i;
			String email = "se" + i + "@cmail.com";
			int a = 1000000 + i;
			String s = Integer.toString(a);
			String s1 = Integer.toString(a);
			s = s.substring(0, 4);
			s1 = s1.substring(3);
			String phone = "010-" + s + "-" + s1;
			mapApiService.getXYMapFromJson(jsonString, username, name, email, phone);

		}
		throw new IllegalArgumentException("error");
	}
}
