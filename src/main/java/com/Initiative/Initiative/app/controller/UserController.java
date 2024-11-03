package com.Initiative.Initiative.app.controller;

import com.Initiative.Initiative.app.auth.*;
import com.Initiative.Initiative.app.model.User;
import com.Initiative.Initiative.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
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
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        Optional<User> existingUser = userService.getUserByEmail(request.getEmail());

        return ResponseEntity.ok(authenticationService.register(request));
    }

    /**
     * Handles pre-registration requests.
     *
     * @param request the pre-registration details
     * @return a ResponseEntity containing the pre-registration code
     */
    @PostMapping("/api/v1/users/create")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    public ResponseEntity<PreRegisterCode> preRegister(@RequestBody PreRegisterRequest request) {
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
    public ResponseEntity<User> preAuthenticate(@RequestBody PreRegisterCode preRegisterCode) {
        Optional<User> user = userService.getUserByActivationCode(preRegisterCode.getActivationCode());

        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
