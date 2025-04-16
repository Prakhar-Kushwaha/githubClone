package com.zoody.GitClone.service;


import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.password4j.Password;

@Service
public class CMD_loginService {

//    public boolean checkCredentials(String username , String password){
//        boolean cred = username.equals("admin") && password.equals("admin");
//        boolean logger = logger(username , password , "admin-repo");
//        return cred && logger;
//    }
//
//
    public boolean logger(String username, String password, String repo) {
        Path path = Paths.get(System.getProperty("user.dir"))
                .resolve(".GitBucket")
                .resolve("configLog.json");

        try {
            Files.createDirectories(path.getParent());
           // String hashedPassword = Password.hash(password).withBcrypt().getResult();

            Map<String, String> logData = new HashMap<>();
            logData.put("username", username);
            logData.put("password", password);
            logData.put("repo", repo);

            String json = new com.fasterxml.jackson.databind.ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(logData);
            Files.writeString(path, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
