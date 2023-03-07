package team.waitingcatch.app.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class StringListConverter implements AttributeConverter<List<String>, String> {
	@Override
	public String convertToDatabaseColumn(List<String> attribute) {
		if (attribute.isEmpty()) {
			return null;
		}
		return String.join(",", attribute);
	}

	@Override
	public List<String> convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return new ArrayList<>();
		}
		return Arrays.stream(dbData.split(",")).collect(Collectors.toList());
	}
}