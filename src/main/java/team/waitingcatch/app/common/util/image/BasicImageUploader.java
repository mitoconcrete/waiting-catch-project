package team.waitingcatch.app.common.util.image;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;

// @Service
@RequiredArgsConstructor
public class BasicImageUploader implements ImageUploader {
	@Value("${profile.image.dir}")
	private String imageDir;

	@Override
	public List<String> uploadList(List<MultipartFile> multipartFiles, String directory) throws IOException {
		List<String> imageUrls = new ArrayList<>();
		for (MultipartFile multipartFile : multipartFiles) {
			imageUrls.add(upload(multipartFile, directory));
		}
		return imageUrls;
	}

	@Override
	public String upload(MultipartFile multipartFile, String directory) throws IOException {
		String originalName = multipartFile.getOriginalFilename();
		long size = multipartFile.getSize();

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(multipartFile.getContentType());
		objectMetadata.setContentLength(size);

		String fileName = directory + "/" + UUID.randomUUID() + originalName;

		String storeFileName = createStoreFileName(fileName);

		String fullPath = getFullPath(storeFileName);

		multipartFile.transferTo(new File(fullPath));
		return fullPath;
	}

	@Override
	public void delete(String imageUrl) {
		File file = new File(imageDir);
		file.delete();
	}

	private String getFullPath(String filename) {
		return imageDir + filename;
	}

	private String createStoreFileName(String originalFilename) {
		String ext = extractExt(originalFilename);
		String uuid = UUID.randomUUID().toString();
		return uuid + "." + ext;
	}

	private String extractExt(String originalFilename) {
		int pos = originalFilename.lastIndexOf(".");
		return originalFilename.substring(pos + 1);
	}
}