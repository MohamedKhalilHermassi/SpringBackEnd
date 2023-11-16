package tn.esprit.com.foyer.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import tn.esprit.com.foyer.entities.User;
import tn.esprit.com.foyer.repositories.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServices implements IUserService{
    private UserRepository userRepository;

    @Override
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(User u) {
        return userRepository.save(u);
    }

    @Override
    public User retrieveUser(Long id) {
        return userRepository.findById(id).get();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Boolean userExists(String email){
        if(userRepository.findByEmail(email).isPresent()){
            return true;
        }
        else {
            return false;
        }
    }
}
