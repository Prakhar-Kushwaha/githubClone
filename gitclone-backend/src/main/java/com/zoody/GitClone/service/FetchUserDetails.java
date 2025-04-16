package com.zoody.GitClone.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FetchUserDetails {
    //Here we will match credentials from our database;

    private Path path ;
    private boolean isUserLoggedIn ;

    private JsonNode configJson;

    @PostConstruct
    public void init() {
        path = Paths.get(System.getProperty("user.dir")).resolve(".GitBucket").resolve("configLog.json");
        try {
            if (!Files.exists(path) || Files.size(path) == 0) {
                isUserLoggedIn = false;
            }
            configJson = new ObjectMapper().readTree(Files.newBufferedReader(path));

        }catch (IOException e){
            System.out.println("User NOT logged in");
        }
        isUserLoggedIn = true;
    }

    public String getUsername() {
        if (configJson != null && configJson.has("username")) {
            return configJson.get("username").asText();
        }
        return null;
    }

    public String getRepo() {
        if (configJson != null && configJson.has("repo")) {
            return configJson.get("repo").asText();
        }
        return null;
    }

    public String getPassword() {
        if (configJson != null && configJson.has("password")) {
            return configJson.get("password").asText();
        }
        return null;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public boolean isUserLoggedIn() {
        return isUserLoggedIn;
    }

    public void setUserLoggedIn(boolean userLoggedIn) {
        isUserLoggedIn = userLoggedIn;
    }
}
