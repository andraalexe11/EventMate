package com.example.EventMate.Exceptions;

public class UserNotFoundException  extends Exception{
    public UserNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
