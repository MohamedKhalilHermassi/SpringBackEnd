package tn.esprit.com.foyer.services;

import tn.esprit.com.foyer.entities.User;

import java.util.List;

public interface IUserService {
    List<User> retrieveAllUsers();
    User updateUser(Long id, User u);
    User retrieveUser(Long id);
    void deleteUser(Long id);
}
