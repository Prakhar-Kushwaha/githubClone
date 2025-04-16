package com.zoody.GitClone.service;

import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CMD_InitService {

    private static final String INIT_FOLDER = ".GitBucket";

    public String init() {
        Path currentDir = Paths.get(System.getProperty("user.dir"));
        Path hiddenDir  = currentDir.resolve(INIT_FOLDER);

        try{
            if(Files.exists(hiddenDir))//Checks for file existence
            {
                return "Already Initialized : "+ hiddenDir.toAbsolutePath();
            }

            Files.createDirectory(hiddenDir);
            Files.createFile(hiddenDir.resolve("config.json"));
            Files.createFile(hiddenDir.resolve("commitsLog.json"));


            //Creates the commit dir
            Path commitsDir = hiddenDir.resolve("commits");
            Files.createDirectory(commitsDir);


            //Creating refs directory structure
            Path refsDir = hiddenDir.resolve("refs");
            Files.createDirectory(refsDir);
            Files.createDirectory(refsDir.resolve("heads"));
            Files.createDirectory(refsDir.resolve("tags"));

            //Head File [Holding Current Branch]
            Path headFile = hiddenDir.resolve("HEAD");
            Files.createFile(headFile);
            Files.write(headFile , "refs/heads/main ".getBytes());

            //Adding head ref in refs/heads/...
            Path mainBranch = refsDir.resolve("heads").resolve("main");
            Files.createFile(mainBranch);


            return "Git Initialized :> " + hiddenDir;
        } catch (IOException e) {
            return "Error :> " + e.getMessage();
        }
    }


}
