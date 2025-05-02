package com.example.EventMate.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Acesta este un endpoint public.";
    }

    @GetMapping("/protected")
    public String protectedEndpoint() {
        return "Acesta este un endpoint protejat. Ai acces pentru că ai furnizat un token valid!";
    }
}