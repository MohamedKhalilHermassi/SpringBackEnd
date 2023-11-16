package tn.esprit.com.foyer.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
