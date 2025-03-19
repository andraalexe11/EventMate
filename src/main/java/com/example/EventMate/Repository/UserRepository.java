package com.example.EventMate.Repository;

import com.example.EventMate.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(int id);
    Optional<User> findByEmail(String email);
}
