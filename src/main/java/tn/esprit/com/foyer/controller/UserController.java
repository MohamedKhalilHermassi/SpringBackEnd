package tn.esprit.com.foyer.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.com.foyer.dto.GenerateCodeRequest;
import tn.esprit.com.foyer.entities.User;
import tn.esprit.com.foyer.services.UserServices;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/user")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private UserServices userServices;

    @GetMapping("/retrieve-all-users")
    @PreAuthorize("hasAuthority('admin:read')")
    public List<User> retrieveAllUsers(){
        return userServices.retrieveAllUsers();
    }

    @GetMapping("/retrieve-user/{user-id}")
    @PreAuthorize("hasAuthority('admin:create')")
    public User retrieveUser(@PathVariable(name = "user-id") Long userId){
        return userServices.retrieveUser(userId);
    }



    @PutMapping("/update-user/{user-id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public User update(@PathVariable("user-id") Long userId, @RequestBody User u){
        return userServices.updateUser(userId,u);
    }

    @DeleteMapping("/delete-user/{user-id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public void deleteUser(@PathVariable(name = "user-id") Long userId){
        userServices.deleteUser(userId);
    }



}
