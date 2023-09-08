package com.example.ohchallbe.global.handler;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
@Service

public class S3Uploader {


    private final AmazonS3 amazonS3Client;


    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public List<String> upload(List<MultipartFile> multipartFiles, String dirName) throws IOException {
        List<String> uploadImageUrls = new ArrayList<>();

        for (MultipartFile multipartFile:multipartFiles) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());   // contentType 지정
//            objectMetadata.setContentDisposition("inline");
            File uploadFile = convert(multipartFile).orElseThrow(
                    ()-> new IllegalArgumentException("MultipartFile -> File 전환 실패"));
           String imageUrl = upload(uploadFile,dirName,objectMetadata);
           uploadImageUrls.add(imageUrl);
        }
       return uploadImageUrls;
    }

    private String upload(File uploadFile,String dirName,ObjectMetadata objectMetadata){

        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile,fileName,objectMetadata);

        removeNewFile(uploadFile);

        return uploadImageUrl;
    }

    private void removeNewFile(File uploadFile) {
        if(uploadFile.delete()){
            log.info("파일이 삭제되었습니다.");

        }else{
            log.info("파일이 삭제되지 못했습니다.");

        }
    }

    private String putS3(File uploadFile, String fileName,ObjectMetadata objectMetadata) {
        amazonS3Client.putObject(
                new PutObjectRequest(bucket,fileName,uploadFile)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
                        .withMetadata(objectMetadata)
        );
        return "https://dsg5ipn7v1etf.cloudfront.net/"+fileName.toString().trim();
    }

    private Optional<File> convert(MultipartFile file) throws IOException{
        System.out.println(file.getOriginalFilename());
        File convertFile = new File(file.getOriginalFilename());
        boolean a = convertFile.createNewFile();
        System.out.println(a);
        if(a){

            try (FileOutputStream fos =new FileOutputStream(convertFile)){
                fos.write(file.getBytes());

            }
            return Optional.of(convertFile);
        }
        System.out.println(2);

        return Optional.empty();
    }

}