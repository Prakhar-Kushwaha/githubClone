package com.zoody.GitClone.CLI_Commands;


import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
@ShellCommandGroup("Test Commands")
public class TestCLI {
    @ShellMethod(key = "greet -u" , value = "Greets user requires args")
    public String greet(@ShellOption("--args") String name){
        return "Hello, " + name + "!";
    }


    @ShellMethod(key = "greet" , value = "Greet user without args")
    public String greetWthoutArgs(){
        return "Welcome to our CLI based Version Control ";
    }

    @ShellMethod(key = "git help" , value = "Help to use git")
    public String showWorkFlow(){
        return "************************************************************************************************\n"
                +"WorkFlow : \n"
                +"git init                      -- initilize empty git named '.GitBucket' \n"
                +"git config                    -- config with correct username ,password & repo \n"
                +"git add .                     -- staged all files \n"
                +"git commit -m                 -- commits the changes [-message] , {Authenticated} \n"
                +"git push                      -- pushes remotely \n"
                +"*************************************************************************************************\n"
                +"git pull                      -- pulls all the commits {uses repo used during git config}\n"
                +"git revert --id               -- Creates a new folder in curr dir with the id provided \n"
                +"git log                       -- logs all commits made in current repo\n"
                +"*************************************************************************************************\n";
    }
}
