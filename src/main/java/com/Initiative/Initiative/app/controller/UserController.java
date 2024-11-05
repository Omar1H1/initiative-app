package com.Initiative.Initiative.app.controller;

import com.Initiative.Initiative.app.auth.*;
import com.Initiative.Initiative.app.model.User;
import com.Initiative.Initiative.app.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    /**
     * Handles user registration requests.
     *
     * @param request the user registration details
     * @return a ResponseEntity containing the authentication response or an error response
     */
    @PostMapping("/api/v1/users/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterInfo request, HttpServletResponse response) {        Optional<User> existingUser = userService.getUserByEmail(request.getEmail());

        User userInfo = User.builder()
                .id(existingUser.get().getId())
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(request.getPassword())
                .username(request.getUsername())
                .role(existingUser.get().getRole())
                .build();

        return ResponseEntity.ok(authenticationService.register(userInfo));
    }

    /**
     * Handles pre-registration requests.
     *
     * @param request the pre-registration details
     * @return a ResponseEntity containing the pre-registration code
     */
    @PostMapping("/api/v1/users/create")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    public ResponseEntity<PreRegisterCode> preRegister(@RequestBody PreRegister request) {
        return ResponseEntity.ok(authenticationService.preRegistration(request));
    }

    /**
     * Handles user authentication requests.
     *
     * @param request the user authentication details
     * @return a ResponseEntity containing the authentication response or an error response
     */
    @PostMapping("/api/v1/users/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    /**
     * Validates a pre-registration code.
     *
     * @param preRegisterCode The pre-registration code
     * @return A ResponseEntity containing the user information or an error response
     */
    @PostMapping("/api/v1/users/preauthenticate")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    public ResponseEntity<PreRegister> preAuthenticate(@RequestBody PreRegisterCode preRegisterCode) {

        log.warn(preRegisterCode.getActivationCode());
        return ResponseEntity.ok(authenticationService.validateCode(preRegisterCode));
    }
}
