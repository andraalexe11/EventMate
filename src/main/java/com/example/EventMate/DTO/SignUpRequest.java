package com.example.EventMate.DTO;

import com.example.EventMate.Model.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public  class SignUpRequest {
    public String username;
    public String email;
    public String password;
    public Integer age;
    public User.Gender gender;

}