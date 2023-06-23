package com.hanghae7.alcoholcommunity.domain.common.entity;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {
	private static final String S3_BUCKET_PREFIX = "S3";
	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;
	private final AmazonS3 amazonS3;

	public String upload(MultipartFile image) throws IOException {
		String newFileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
		String imageName = S3_BUCKET_PREFIX + newFileName;

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(image.getContentType());
		objectMetadata.setContentLength(image.getSize());

		InputStream inputStream = image.getInputStream();

		amazonS3.putObject(new PutObjectRequest(bucketName, imageName, inputStream, objectMetadata)
			.withCannedAcl(CannedAccessControlList.PublicRead)); // 이미지에 대한 접근 권한 '공개' 로 설정
		String imageUrl = amazonS3.getUrl(bucketName, imageName).toString();

		return amazonS3.getUrl(bucketName, newFileName).toString();
	}


}
