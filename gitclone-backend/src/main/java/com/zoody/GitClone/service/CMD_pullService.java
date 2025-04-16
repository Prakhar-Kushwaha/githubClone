package com.zoody.GitClone.service;

import com.zoody.GitClone.config.S3_Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CMD_pullService {
    //Pull all commits from s3 bucket to ".Gitbucket/commits/uuid" ,

    //Cloudfare S3 pull service



    @Autowired
    private S3Client s3Client;

    @Autowired
    private FetchUserDetails fetchUserDetails;

    private static final String DEFAULT_BUCKET = "gitbucket";
    public String pullCommits(){

        Path desPath = Paths.get(System.getProperty("user.dir")).resolve(".GitBucket");
        if(!Files.exists(desPath)){
            return "Current dir , in not a git repo -- Run 'git init'";
        }
        desPath = desPath.resolve("commits");

        try{
            Files.createDirectories(desPath);
        }catch (IOException e){
            return "Error while pulling : " +e ;
        }

        String repo = fetchUserDetails.getRepo();

        //Pullying commits
        try{
            ListObjectsV2Request listReq = ListObjectsV2Request.builder()
                    .bucket(DEFAULT_BUCKET)
                    //.prefix(repo)//Repo will be here
                    .prefix(repo +"/commits")//Under commits folder , Later it will be nested under a repo , and then we also need to implement CMD_pushService, and remove upload logic from CMD_commitsService
                    .build();
            ListObjectsV2Response listRes = s3Client.listObjectsV2(listReq);
            for (S3Object obj : listRes.contents()) {
                String key = obj.key(); // e.g., commits/uuid/file.txt

                // Skip if it's just a "folder" placeholder in S3 (size 0 and ends with /)
                if (key.endsWith("/")) continue;

                // Strip "commits/" from key
                Path relativePath = Paths.get(key).subpath(2, Paths.get(key).getNameCount());//String repo name/commit/ from the desPath
                Path targetFile = desPath.resolve(relativePath);

                // Skip if the file already exists locally
                if (Files.exists(targetFile)) {
                    System.out.println("Skipping existing file: " + targetFile);
                    continue;
                }

                // Make sure parent directories exist
                Files.createDirectories(targetFile.getParent());

                System.out.println("Pulling : "+ targetFile);
                // Download the object
                s3Client.getObject(GetObjectRequest.builder()
                                .bucket(DEFAULT_BUCKET)
                                .key(key)
                                .build(),
                        ResponseTransformer.toFile(targetFile));
            }
            return "Pulled all commits sucessfully";

        }catch (IOException e){
            return "Error while pull request : "+e;
        }



    }

}
