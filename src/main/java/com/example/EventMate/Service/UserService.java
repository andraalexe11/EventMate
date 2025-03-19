package com.example.EventMate.Service;

import com.example.EventMate.DTO.SignupRequest;
import com.example.EventMate.Model.User;
import com.example.EventMate.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.EventMate.Exceptions.UserAlreadyExistsException;
import com.example.EventMate.Exceptions.UserNotFoundException;
import com.example.EventMate.DTO.UserDTO;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * Retrieves all users from the repository.
     *
     * @return a list of all available users.
     */
    public List<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * Adds a new user after verifying if a user with the same username already exists.
     *
     * @param userDTO DTO object that contains the information of the user to be added.
     * @return the added user.
     * @throws UserAlreadyExistsException if a user with the same username already exists.
     */
    public User add(final UserDTO userDTO) throws UserAlreadyExistsException {
        checkIfUserAlreadyExists(userDTO.getUsername());
        return add(buildUser(userDTO));
    }

    /**
     * Checks if a user with a specific username exists in the repository.
     *
     * @param username the username of the user to be checked.
     * @throws UserAlreadyExistsException if the user already exists.
     */
    public void checkIfUserAlreadyExists(final String username) throws UserAlreadyExistsException {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
    }

    /**
     * Builds a user object from a user DTO.
     *
     * @param userDTO the DTO object to build the user from.
     * @return the built user.
     */
    public User buildUser(final UserDTO userDTO) {
        var user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setAge(userDTO.getAge());
        user.setGender(User.Gender.valueOf(userDTO.getGender()));
        return user;
    }

    /**
     * Adds a new user to the repository.
     *
     * @param user the user to be added.
     * @return the added user.
     */
    public User add(User user) {
        return userRepository.save(user);
    }

    /**
     * Updates a user with the specified username.
     *
     * @param oldUsername the username of the user to be updated.
     * @param userDTO the DTO object containing the new information for the user.
     * @return the updated user.
     * @throws UserNotFoundException if the user is not found.
     */
    public User update(final String oldUsername, final UserDTO userDTO) throws UserNotFoundException {
        return updateUsername(findUser(oldUsername), userDTO);
    }

    /**
     * Finds a user by username.
     *
     * @param username the username of the user to be searched.
     * @return the found user.
     * @throws UserNotFoundException if no user with the specified username is found.
     */
    public User findUser(final String username) throws UserNotFoundException {
        return userRepository.findAll().stream()
                .filter(user -> user.getUsername().equals(username)).findFirst().orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    /**
     * Updates a user with the specified username.
     *
     * @param user the user to be updated.
     * @param userDTO the DTO object containing the new information for the user.
     * @return the updated user.
     */
    public User updateUsername(final User user, final UserDTO userDTO) {
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setAge(userDTO.getAge());
        user.setGender(User.Gender.valueOf(userDTO.getGender()));
        return userRepository.save(user);
    }

    /**
     * Deletes a user from the repository based on its username.
     *
     * @param username the username of the user to delete.
     * @throws UserNotFoundException if no user with the specified username is found.
     */
    public void delete(final String username) throws UserNotFoundException {
        findUser(username);
        userRepository.delete(findUser(username));
    }

    public User findById(final int id) throws UserNotFoundException{
        return userRepository.findAll().stream()
                .filter(user -> user.getId().equals(id)).findFirst().orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public User registerUser( SignupRequest signupRequest) {

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(signupRequest.getPassword());
        user.setAge(signupRequest.getAge());
        user.setGender(signupRequest.getGender());
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email-ul este deja folosit!");
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }
}

