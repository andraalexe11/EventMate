package com.example.EventMate.Controller;

import com.example.EventMate.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.example.EventMate.Service.UserService;
import com.example.EventMate.DTO.LoginRequest;
import com.example.EventMate.DTO.SignupRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtTokenUtil;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    // Endpoint pentru înregistrare (sign up)
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signupRequest) {
        signupRequest.
        userService.registerUser(signupRequest);
        return ResponseEntity.ok("User registered successfully");
    }

    // Endpoint pentru login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Încearcă autentificarea utilizatorului
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        // Generarea tokenului JWT
        String token = jwtTokenUtil.generateToken(String.valueOf(authentication));
        return ResponseEntity.ok(new JwtResponse(token));
    }
}
