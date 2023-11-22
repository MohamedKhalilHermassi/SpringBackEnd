package tn.esprit.com.foyer.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/retrieve-user/{user-id}")
    @PreAuthorize("hasAuthority('admin:create')")
    public User retrieveUser(@PathVariable(name = "user-id") Long userId){
        return userServices.retrieveUser(userId);
    }

    @PutMapping("/update-user")
    @PreAuthorize("hasAuthority('admin:update')")
    public User updateUser(@RequestBody User u){
        return userServices.updateUser(u);
    }

    @DeleteMapping("/delete-user/{user-id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public void deleteUser(@PathVariable(name = "user-id") Long userId){
        userServices.deleteUser(userId);
    }
}
