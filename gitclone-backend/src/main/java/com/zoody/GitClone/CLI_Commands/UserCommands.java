package com.zoody.GitClone.CLI_Commands;

import com.zoody.GitClone.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.*;

import java.io.Console;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@ShellComponent
@ShellCommandGroup("User Commands")
public class UserCommands {

    @Autowired
    private CMD_InitService initService;

    @Autowired
    private CMD_addService addService;

    @Autowired
    private CMD_commitService commitService;

    @Autowired
    private CMD_pullService pullService;

    @Autowired
    private CMD_revertService revertService;

    @Autowired
    private CMD_pushService pushService;

    @ShellMethod(key = "git init", value = "Initializes empty custom git repo locally")
    public String gitInit() {
        return initService.init();
    }

    @ShellMethod(key = "git config" , value = "Git configuration Locally")
    public String gitConfig(){

        return this.acceptCredentials();
    }

    @ShellMethod(key = "git add", value = "Add file to staging area")
    public String gitAdd(@ShellOption("--filename.ext") String fileName) {
        try{
            return addService.add(fileName);
        }catch (IOException e){
            return e.toString();
        }
    }

    @ShellMethod(key = "git commit", value = "Commits files of staged area")
    public String gitCommit(@ShellOption("-m") String message) {

        return commitService.commit(message);
    }

    @ShellMethod(key = "git pull", value = "pull repo  from remote location")
    public String gitPull()//@ShellOption("--repo") String repoUri) {
    {
        return pullService.pullCommits();
    }

    @ShellMethod(key = "git push", value = "pushes local version repo to remote location [Authenticated]")
    public String gitPush()//@ShellOption("--repo") String repo) {
    {
        if(!fetchUserDetails.isUserLoggedIn()){
            return "Please login first , Run 'git config'";
        }
        Path path = Paths.get(System.getProperty("user.dir")).resolve(".GitBucket")
                .resolve("commits");
        if(!Files.exists(path)){
            return "First commit your changes";
        }
        return "Pushed Successfully ";// + repo;
    }

    @ShellMethod(key = "git revert", value = "Revert to specific commit")
    public String gitRevert(@ShellOption("--id") String id) {
        return revertService.revert(id);
    }


    public Availability gitPushAvailability(){
        return fetchUserDetails.isUserLoggedIn()?
                Availability.available()
                :
                Availability.unavailable("User Previliges Declined , `Run 'git config'  ");
    }

    public Availability gitCommitAvailability(){
        return fetchUserDetails.isUserLoggedIn()?
                Availability.available()
                :
                Availability.unavailable("User Previliges Declined , `Run 'git config'  ");
    }



    @ShellMethod(key ="git log" , value = "Logs all commit")
    public List<String> getAllCommits(){
        if(!fetchUserDetails.isUserLoggedIn()){
            throw new RuntimeException("User not logged in");
        }
        String username = fetchUserDetails.getUsername();
        return userService.getUserCommits(username);
    }











    @Autowired
    private CMD_loginService loginService ;

    @Autowired
    private UserService userService;

    @Autowired
    private FetchUserDetails fetchUserDetails;

    private String acceptCredentials(){
        Console console = System.console();

        if(console == null){
            return "Terminal not running";
        }

        String username = console.readLine("Enter username: ");
        char[] passwordChars = console.readPassword("Enter password: ");
        String password = new String(passwordChars);
        String repo = console.readLine("Enter repo name :");

        userService.addUser(username , password , repo);
        loginService.logger(username , password , repo);
        fetchUserDetails.init();

        return "successfull";
    }
}
