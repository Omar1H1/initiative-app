package com.Initiative.Initiative.app.controller;

import com.Initiative.Initiative.app.auth.*;
import com.Initiative.Initiative.app.model.User;
import com.Initiative.Initiative.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "Register a new user by User",
            description = "After the user get his/her/they email of confirmation they can send a register request " +
                    "On successful registration, an authentication response containing an access token will be returned.",
            responses = {@ApiResponse(responseCode = "200", description = "User registered successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation
                    = AuthenticationResponse.class)))}
    )
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterInfo request) {
        User existingUser = userService.getUserByEmail(request.getEmail()).orElseThrow();

        User userInfo = User.builder()
                .id(existingUser.getId())
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(request.getPassword())
                .username(request.getUsername())
                .role(existingUser.getRole())
                .build();

        return ResponseEntity.ok(authenticationService.register(userInfo));
    }

    /**
     * Handles pre-registration requests.
     *
     * @param request the pre-registration details
     * @return a ResponseEntity containing the pre-registration code
     */
    @Operation(summary = "Create a new user By Admin",
            description = "Create a new user by an Admin  " +
                    "On successful creation, an email is sent to the user email containing a link to register his/her/they account",
            responses = {@ApiResponse(responseCode = "200", description = "User created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation
                    = PreRegister.class)))}
    )
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
    @Operation(summary = "Authenticate user",
            description = "A User can send a json object containing email and password  " +
                    "if the email and password are related to a valid user then this endpoint will return a JWT token ",
            responses = {@ApiResponse(responseCode = "200", description = "User Info exists in DB", content = @Content(mediaType = "application/json", schema = @Schema(implementation
                    = AuthenticationResponse.class)))}
    )
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
    @Operation(summary = "Validate User activation Code",
            description = "After the user get his/her/they email of confirmation they are sent to an input form " +
                    "On successful registration (valid Activation Code), an authentication response containing user info that has been given when the admin " +
                     "initialized the account for more info check  '/api/v1/users/create' ",
            responses = {@ApiResponse(responseCode = "200", description = "User has a valid Activation code", content = @Content(mediaType = "application/json", schema = @Schema(implementation
                    = PreRegister.class)))}
    )
    public ResponseEntity<PreRegister> preAuthenticate(@RequestBody PreRegisterCode preRegisterCode) {

        log.warn(preRegisterCode.getActivationCode());
        return ResponseEntity.ok(authenticationService.validateCode(preRegisterCode));
    }

    @PostMapping("/api/v1/users/forgetpassword")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @Operation(summary = "Send an email to reset password",
            description = "After the user get his/her/they email of confirmation they are sent to an input form " +
                    "On successful registration (valid Activation Code), an authentication response containing user info that has been given when the admin " +
                     "initialized the account for more info check  '/api/v1/users/create' ",
            responses = {@ApiResponse(responseCode = "200", description = "User has a valid Activation code", content = @Content(mediaType = "application/json", schema = @Schema(implementation =
                    PasswordRecoveryInfo.class)))}
    )
    public ResponseEntity<?> forgetPassword(@RequestBody PasswordRecoveryInfo passwordRecoveryInfo) {
        Optional<User> expectedUser = authenticationService.getUserByEmail(passwordRecoveryInfo);
        if (expectedUser.isEmpty()) {
            return ResponseEntity.status(404).body("No user with provided email " + passwordRecoveryInfo.getEmail() + " found");
        }
        return ResponseEntity.status(200).body("User with provided email exists");
    }
}
