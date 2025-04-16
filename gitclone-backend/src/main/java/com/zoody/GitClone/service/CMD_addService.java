package com.zoody.GitClone.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class CMD_addService {

    public String add(String fileName)throws IOException{
        Path projectDir = Paths.get(System.getProperty("user.dir"));
        Path desPath = projectDir.resolve(".GitBucket").resolve("staged");
        Path srcFile = projectDir.resolve(fileName);
        Path desFile = desPath.resolve(fileName);
        try {
            Files.createDirectories(desPath);
            if(! Files.exists(srcFile)){
                return fileName + " does not exist";
            }
            if(fileName.trim().equals(".")){
                Files.walk(projectDir)
                        .filter(path -> !Files.isDirectory(path))
                        .filter(path -> !path.toString().contains(".GitBucket") && !path.toString().contains(".idea") && !path.toString().contains("commits") && !path.toString().contains(".git"))
                        .forEach(source ->
                        {
                            try{
                                Path relativePath = projectDir.relativize(source);
                                Path target = desPath.resolve(relativePath);
                                Files.createDirectories(target.getParent());
                                Files.copy(source , target , StandardCopyOption.REPLACE_EXISTING);
                            }catch (IOException e){
                                throw new RuntimeException(e);
                            }
                        });
                return "All files staged sucessfully";
            }
            else{
                Files.copy(srcFile , desFile , StandardCopyOption.REPLACE_EXISTING);
                return fileName + "staged sucessfully";
            }

        }catch (IOException e)
        {
            return "Error : " + e;
        }

    }
}
