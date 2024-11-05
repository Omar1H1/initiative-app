package com.Initiative.Initiative.app.service;

import com.Initiative.Initiative.app.auth.RegisterInfo;
import com.Initiative.Initiative.app.model.User;
import com.Initiative.Initiative.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
            return userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Boolean accountHasBeenInitilized(RegisterInfo registerRequest) {
        Optional<User> user = userRepository.findByEmail(registerRequest.getEmail());

        return  user.isPresent();
    }

    public Optional<User> getUserByActivationCode(String activationCode) {
        return userRepository.findByActivationCode(activationCode);

    }

}
