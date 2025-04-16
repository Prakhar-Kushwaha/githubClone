package com.zoody.GitClone.CLI_Commands;

import org.springframework.shell.Availability;
import org.springframework.shell.standard.*;

@ShellComponent
@ShellCommandGroup("Admin Commands")
public class AdminCommands {

    private boolean isAdminLogin;

    @ShellMethodAvailability({"deleteUser"})
    public Availability deleteUserAvailability(){
        return isAdminLogin?
                Availability.available()
                :
                Availability.unavailable("Admin Previlizes Denied , Please Login with admin Credentials");
    }

    @ShellMethod(key = "git delete" , value = "Delete git user [Admin Permission Required]")
    public String deleteUser(@ShellOption("--id") String userId){
        //Api called here with authentication
        return userId + " deleted sucessfully";
    }
}
