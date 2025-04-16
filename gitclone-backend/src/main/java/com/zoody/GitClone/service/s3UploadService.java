package com.zoody.GitClone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import java.nio.file.Path;

@Service
public class s3UploadService {
    @Autowired private S3Client s3Client;

    private final static String bucket = "gitbucket";

    public void upload(Path filePath , String objectKey){

       try {
            PutObjectRequest putObjectRequest =
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(objectKey)
                            .build();

            //Upload the file
            s3Client.putObject(putObjectRequest , filePath);
           System.out.println("Uploaded sucessfully : "+ filePath);

       }catch (Exception e){
           e.printStackTrace();
           System.out.println("Filed to Upload" + filePath);
       }
    }
}
