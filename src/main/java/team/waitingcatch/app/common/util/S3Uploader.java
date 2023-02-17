package team.waitingcatch.app.common.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Uploader {
	private final AmazonS3Client amazonS3Client;

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	// 다수의 이미지 업로드시 사용
	public List<String> uploadList(List<MultipartFile> multipartFiles, String directionName) throws IOException {
		List<String> imageUrls = new ArrayList<>();
		for (MultipartFile multipartFile : multipartFiles) {
			imageUrls.add(upload(multipartFile, directionName));
		}
		return imageUrls;
	}

	// 1개의 이미지 업로드시 사용
	// directionName = restaurant, menu, review
	public String upload(MultipartFile multipartFile, String directionName) throws IOException {
		String originalName = multipartFile.getOriginalFilename();
		long size = multipartFile.getSize();

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(multipartFile.getContentType());
		objectMetadata.setContentLength(size);

		String fileName = directionName + "/" + UUID.randomUUID() + originalName;

		return uploadS3(fileName, multipartFile, objectMetadata);
	}

	// S3에 업로드 하는 메소드
	public String uploadS3(String fileName, MultipartFile multipartFile, ObjectMetadata metadata) throws IOException {
		amazonS3Client.putObject(
			new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata)
				.withCannedAcl(CannedAccessControlList.PublicRead)
		);

		return amazonS3Client.getUrl(bucket, fileName).toString();
		// https://버킷이름.s3.ap-northeast-2.amazonaws.com/menu/UUID+사진이름.확장자
	}

	public void deleteS3(String imageUrl) {
		imageUrl = imageUrl.replace("https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/", "");
		amazonS3Client.deleteObject(bucket, imageUrl);
	}
}
