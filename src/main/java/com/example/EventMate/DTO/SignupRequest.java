package com.example.EventMate.DTO;

import com.example.EventMate.Model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    private String username;
    private String email;
    private String password;
    private Integer age;
    private User.Gender gender;
}
