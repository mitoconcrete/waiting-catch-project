package team.waitingcatch.app.common.util.image;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {
	List<String> uploadList(List<MultipartFile> multipartFiles, String directionName) throws IOException;

	String upload(MultipartFile multipartFile, String directionName) throws IOException;

	void delete(String imageUrl);
}