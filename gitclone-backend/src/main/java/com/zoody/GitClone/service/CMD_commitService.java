package com.zoody.GitClone.service;

import com.zoody.GitClone.model.User;
import com.zoody.GitClone.repositary.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

@Service
public class CMD_commitService {



    @Autowired
    private s3UploadService service;




    @Autowired
    private FetchUserDetails fetchUserDetails;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserService userService;



    public String commit(String message) {


        String repo = fetchUserDetails.getRepo();

        if(!fetchUserDetails.isUserLoggedIn()){
            return "User is not logged In \n Run : git config";
        }
        String pass = fetchUserDetails.getPassword();
        String username = fetchUserDetails.getUsername();
        Optional<User> user = userRepo.findById(username);
        if(!user.isPresent()){
            return "Username is incorrect";
        }

        if(! userService.matchPassword(username , pass , user.get().getPassword())){
            return "Password is wrong \nRerun git config";
        }


        Path srcPath = Paths.get(System.getProperty("user.dir")).resolve(".GitBucket").resolve("staged");
        UUID uuid = UUID.randomUUID();
        Path desPath = Paths.get(System.getProperty("user.dir")).resolve(".GitBucket")
                //.resolve(repo)//Added repo as a directory
                .resolve("commits")
                .resolve(uuid.toString());


        if (!Files.exists(srcPath)) {
            return "Please run command 'git add .'";
        }

        try (Stream<Path> stream = Files.walk(srcPath)) {
            stream
                    .filter(Files::isRegularFile)
                    .forEach(
                    path -> {
                        try {
                            Path srcRelativePath = srcPath.relativize(path).resolve("commits");
                            Path des = desPath.resolve(srcRelativePath);//main relative order
                            Files.createDirectories(des.getParent());
                            String objectKey = ( repo +"/commits/" +uuid+"/"+srcRelativePath).replace(File.separatorChar, '/');
                            service.upload(path, objectKey);
                            Files.createDirectories(desPath);

                            Files.move(path, des, StandardCopyOption.REPLACE_EXISTING);//Doing this to move files from staged folder to commits folder
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                    }
            );

            updateLog(uuid.toString(), message);
            userService.addCommit(username , message+"            --   "+uuid );


            return "Comitted Sucessfully";
        } catch (IOException e) {
            return "Error :>" + e.getMessage();
        }

    }


    private void updateLog(String uuid, String mess) throws IOException {
        try {
            Path path = Paths.get(System.getProperty("user.dir")).resolve(".GitBucket");
            Path headPath = path.resolve("HEAD");
            String currentBranchRef = getCurrentBranch(headPath);
            if (currentBranchRef.isEmpty()) {
                throw new FileNotFoundException("HEAD file is empty or missing.");
            }

            Path refPath = path.resolve(currentBranchRef);

            //Refs is updated with latest commit
            Files.write(refPath, uuid.getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            //Updating its details in Commits Log
            Map<String, Object> commitEntry = new LinkedHashMap<>();
            commitEntry.put("id", uuid);
            commitEntry.put("date", new Date());
            commitEntry.put("message", mess);
            commitEntry.put("os name " , System.getProperty("os.name"));
            commitEntry.put("os architecture " , System.getProperty("os.arch"));
            commitEntry.put("os version " , System.getProperty("os.version"));


            Path logPath = path.resolve("commitsLog.json");

            Files.write(logPath , commitEntry.toString().getBytes(), StandardOpenOption.APPEND);

        } catch (IOException e) {
            throw new IOException(e);
        }


    }

    private String getCurrentBranch(Path headPath){

        try {
            return Files.readString(headPath).trim();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
