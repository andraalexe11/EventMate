package com.example.EventMate.Controller;

import com.example.EventMate.Repository.UserRepository;
import com.example.EventMate.Service.KeycloakService;
import com.example.EventMate.DTO.SignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.EventMate.Model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/signup")
public class SignupController {

    @Autowired
    private KeycloakService keycloakService; // Injectăm serviciul Keycloak pentru gestionarea utilizatorilor

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<String> signup(@RequestBody SignUpRequest signupRequest) {
        try {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());
            keycloakService.createUser(signupRequest.getUsername(), signupRequest.getEmail(), signupRequest.getPassword());
            User user = new User();
            user.setUsername(signupRequest.getUsername());
            user.setEmail(signupRequest.getEmail());
            user.setPassword(encodedPassword);
            user.setGender(signupRequest.getGender());
            user.setAge(signupRequest.getAge());
            userRepository.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Utilizator creat cu succes!");
        } catch (RuntimeException ex) {
            // În cazul în care apare o eroare, returnăm răspuns cu cod 400
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Eroare: " + ex.getMessage());
        }
    }
}