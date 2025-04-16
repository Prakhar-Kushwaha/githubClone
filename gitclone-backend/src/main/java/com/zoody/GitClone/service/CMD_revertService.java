package com.zoody.GitClone.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class CMD_revertService {

    @Autowired
    private S3Client s3Client;

    public String revert(String uuid){
        Path src = Paths.get(System.getProperty("user.dir")).resolve(".GitBucket");
        if(!Files.exists(src)){
            return "Not a git repo / Run -- git init";
        }
        src = src.resolve("commits");
        if(!Files.exists(src)){
            return "Files are not commited , might be in staged \n Run : \n git add . \n git commit 'message' \n git push \n And then try again";
        }
        src = src.resolve(uuid);
        if(!Files.exists(src)){

            return uuid + " commit is not present \n Try running -- git pull \n Then try again";
        }

        Path desPath = Paths.get(System.getProperty("user.dir")).resolve(uuid);
        if(copyFiles(src , desPath)){
            return "Reverted sucessfully " + uuid;
        }else{
            return "Revert failed";
        }

    }

    private boolean copyFiles(Path src, Path desPath)  {

        AtomicBoolean status = new AtomicBoolean(true);
        try {
            Files.createDirectories(desPath);
            Files.walk(src)
                    .forEach(
                            source -> {
                                try {
                                    Path sou = src.relativize(source);//Gives relative path from src path
                                    Path target = desPath.resolve(sou);//Adds that relative path
                                    Files.createDirectories(target.getParent());
                                    Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
                                } catch (IOException e) {
                                    status.set(false);
                                    return;//Skips the file
                                }
                            }
                    );
        }catch (IOException e){
            status.set(false);
        }
        return status.get();
    }
}
