package tn.esprit.com.foyer.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.com.foyer.entities.User;
import tn.esprit.com.foyer.services.UserServices;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private UserServices userServices;

    @GetMapping("/retrieve-all-users")
    public List<User> retrieveAllUsers(){
        return userServices.retrieveAllUsers();
    }

    @PostMapping("/retrieve-user/{user-id}")
    public User retrieveUser(@PathVariable(name = "user-id") Long userId){
        return userServices.retrieveUser(userId);
    }

    @PutMapping("/update-user")
    public User updateUser(@RequestBody User u){
        return userServices.updateUser(u);
    }

    @DeleteMapping("/delete-user/{user-id}")
    public void deleteUser(@PathVariable(name = "user-id") Long userId){
        userServices.deleteUser(userId);
    }
}
