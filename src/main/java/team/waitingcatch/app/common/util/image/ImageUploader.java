package team.waitingcatch.app.common.util.image;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {
	List<String> uploadList(List<MultipartFile> multipartFiles, String directory) throws IOException;

	String upload(MultipartFile multipartFile, String directory) throws IOException;

	void delete(String imageUrl);
}