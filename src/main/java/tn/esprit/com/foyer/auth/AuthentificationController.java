package tn.esprit.com.foyer.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.com.foyer.dto.ChangePasswordRequest;
import tn.esprit.com.foyer.dto.GenerateCodeRequest;
import tn.esprit.com.foyer.services.UserServices;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthentificationController {

    private final AuthenticationService service;
    private final UserServices userServices;
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ){
        if (userServices.userExists(request.getEmail())){
            ErrorResponse errorResponse = new ErrorResponse("Email Exists", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(
            @RequestBody AuthenticationRequest request
    ){
        if (!userServices.userExists(request.getEmail())){
            ErrorResponse errorResponse = new ErrorResponse("Email Doesn't Exist", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        return ResponseEntity.ok(service.authenticate(request));
    }
    @PutMapping("/generatecode")
    public ResponseEntity<?> generateUserCode(@RequestBody GenerateCodeRequest request){
        if (!userServices.userExists(request.getEmail())){
            ErrorResponse errorResponse = new ErrorResponse("Email Doesn't Exist", LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        return ResponseEntity.ok(userServices.generateUserCode(request.getEmail()));
    }

    @PostMapping("/verif-code/{sent-code}")
    public Boolean verifCode(@PathVariable("sent-code") String sentCode, @RequestBody GenerateCodeRequest request){
        return userServices.verifCode(sentCode, request.getEmail());
    }

    @PutMapping("/reset-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request){
        userServices.forgotPassword(request);
        return ResponseEntity.ok().build();
    }
}
