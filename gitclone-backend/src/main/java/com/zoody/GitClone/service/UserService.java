package com.zoody.GitClone.service;

import com.password4j.Password;
import com.zoody.GitClone.model.User;
import com.zoody.GitClone.repositary.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    public String  addUser(String username , String password , String repo){
        User user = new User();
        user.setId(username);
        user.setPassword(extractHash(Password.hash(password).withBcrypt().toString()));
        user.setRepo(repo);

        userRepo.save(user);

        return "User Added Sucessfully";
    }

    public String addCommit(String username , String uuid){
        Optional<User> user = userRepo.findById(username);
        if(! user.isPresent()){
            return "User not FOUND! ";
        }
        List<String> list = user.get().getCommits();
        list.add(uuid);

        userRepo.save(user.get());
        return "Commit updated Sucessfully";
    }

    public boolean isUserPresent(String username){
        Optional<User> user = userRepo.findById(username);
        if(! user.isPresent()){
            return false;
        }
        return true;
    }
    public List<String> getUserCommits(String userId) {
        return userRepo.findById(userId)
                .map(User::getCommits)
                .orElse(List.of());
    }

    public String getUserRepo(String userId) {
        return userRepo.findById(userId)
                .map(User::getRepo)
                .orElse(null);
    }


    public boolean matchPassword(String username , String localPass , String dbPass){
        Optional<User> user = userRepo.findById(username);
        if(!user.isPresent()){
            throw new RuntimeException("UserName /Password is wrong \n Rerun : git config");
        }
        User getUser = user.get();
        return Password.check(localPass , dbPass).withBcrypt();
    }


    private String extractHash(String fullString) {
        int start = fullString.indexOf("$2b$");
        if (start == -1) throw new RuntimeException("Invalid bcrypt format");
        return fullString.substring(start, fullString.length()-1);
    }

}
