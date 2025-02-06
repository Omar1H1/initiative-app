package com.Initiative.app.service;

import com.Initiative.app.dto.RegisterInfo;
import com.Initiative.app.dto.UserDTO;
import com.Initiative.app.enums.RoleEnum;
import com.Initiative.app.model.User;
import com.Initiative.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        log.info("Saving user: {}", user.getSectorOfActivity());
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

    public Boolean accountHasBeenInitiated(RegisterInfo registerRequest) {
        Optional<User> user = userRepository.findByEmail(registerRequest.getEmail());

        return  user.isPresent();
    }

    public Optional<User> getUserByActivationCode(String activationCode) {
        return userRepository.findByActivationCode(activationCode);

    }


    public User prepareUserForRegistration(User request, byte[] profileImage) {
        if (profileImage == null || profileImage.length == 0) {
            throw new IllegalArgumentException("Profile image must not be empty.");
        }

        User userToSave = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("User with email " + request.getEmail() + " does not exist."));

        userToSave.setUsername(request.getUsername());
        userToSave.setPassword(request.getPassword());
        userToSave.setSectorOfActivity(request.getSectorOfActivity());
        userToSave.setIsActive(true);
        userToSave.setProjectDescription(request.getProjectDescription());
        userToSave.setProfileImage(profileImage);

        return userToSave;
    }


    public List<UserDTO> getUsersByRole(RoleEnum role) {
        return userRepository.findByRole(role).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .isActive(user.getIsActive())
                .sectorOfActivity(user.getSectorOfActivity())
                .projectDescription(user.getProjectDescription())
                .matchList(user.getMatchList())
                .build();

    }
}
