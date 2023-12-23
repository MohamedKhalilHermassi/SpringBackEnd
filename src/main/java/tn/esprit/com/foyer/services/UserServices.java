package tn.esprit.com.foyer.services;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import tn.esprit.com.foyer.dto.ChangePasswordRequest;
import tn.esprit.com.foyer.entities.User;
import tn.esprit.com.foyer.repositories.UserRepository;

import java.util.*;

@Service
@AllArgsConstructor
public class UserServices implements IUserService{
    private UserRepository userRepository;
    private EmailService emailService;
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> retrieveAllUsers() {
        return userRepository.findAll();
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
    @Override
    public User updateUser(Long id, User u){
        User user = userRepository.findById(id).get();
        user.setFirstname(u.getFirstname());
        user.setLastname(u.getLastname());
        user.setEcole(u.getEcole());
        user.setEmail(u.getEmail());
        userRepository.save(user);
        return user;
    }

    public String generateUserCode(String email){
        Random random = new Random();
        int randomCode = random.nextInt(900000)+100000;
        String code = String.valueOf(randomCode);
        User user = userRepository.findByEmail(email).orElseThrow();
        Context context = new Context();
        context.setVariable("userName", user.getFirstname());
        context.setVariable("code", code);

        emailService.sendEmailWithHtmlTemplate(user.getEmail(), "Reset Password", "email-template", context);
        user.setCode(code);
        userRepository.save(user);
        return code;
    }
    public Boolean verifCode(String sentCode, String email){
        User user = userRepository.findByEmail(email).orElseThrow();
        Boolean isCorrect = Objects.equals(user.getCode(), sentCode);
        if(isCorrect) {
            user.setCode(null);
            userRepository.save(user);
        }
        return isCorrect;
    }

    public void changePassword(ChangePasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())){
            throw new IllegalStateException("Wrong Exception");
        }
        if(!request.getNewPassword().equals(request.getConfirmationPassword())){
            throw new IllegalStateException("Password are not the same");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
    public void forgotPassword(ChangePasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        if(passwordEncoder.matches(request.getNewPassword(), user.getPassword())){
            throw new IllegalStateException("Wrong Exception");
        }
        if(!request.getNewPassword().equals(request.getConfirmationPassword())){
            throw new IllegalStateException("Password are not the same");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
