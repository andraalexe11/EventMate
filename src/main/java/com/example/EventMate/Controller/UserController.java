package com.example.EventMate.Controller;



import com.example.EventMate.Model.User;
import com.example.EventMate.Service.UserService;
import com.example.EventMate.Exceptions.*;
import com.example.EventMate.DTO.UserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Retrieves all user entries from the repository.
     *
     * @return a list of all users in the database
     */
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")

    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/getByName")
    public ResponseEntity<UserDTO> getByName(@RequestParam String name) {
        try{
            User user = userService.findUser(name);
            return ok(new UserDTO(user));
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Adds a new user after verifying if a user with the same username already exists.
     *
     * @param userDTO DTO object that contains the information of the user to be added.
     * @return the added user.
     * @throws UserAlreadyExistsException if a user with the same username already exists.
     */
    @PostMapping("/add")
    public ResponseEntity<User> add(@Valid @RequestBody UserDTO userDTO) {
        try {
            User addedUser = userService.add(userDTO);  // Aici ar trebui să adaugi utilizatorul
            return ResponseEntity.status(HttpStatus.CREATED).body(addedUser);  // Returnezi status 201 cu body
        } catch (UserAlreadyExistsException e) {
            // Dacă utilizatorul există deja, returnezi un Conflict (409)
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    /**
     * Updates a user entry in the repository.
     *
     * @param username the username of the user to be updated.
     * @param userDTO  DTO object that contains the information of the user to be updated.
     * @return the updated user.
     */
    @PutMapping("/update")
    public ResponseEntity<User> update(@RequestParam String username, @RequestBody UserDTO userDTO) {
        try {
            return ok(userService.update(username, userDTO));
        } catch (UserNotFoundException e) {
            return badRequest().build();
        }
    }

    /**
     * Deletes a user entry from the repository.
     *
     * @param username the username of the user to be deleted.
     * @return the deleted user.
     */
    @DeleteMapping("/delete")
    public ResponseEntity<User> delete(@RequestParam String username) {
        try {
            userService.delete(username);
            return ok().build();
        } catch (UserNotFoundException e) {
            return badRequest().build();
        }
    }


}
