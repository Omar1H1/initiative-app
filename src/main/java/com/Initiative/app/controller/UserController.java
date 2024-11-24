package com.Initiative.app.controller;

import com.Initiative.app.auth.*;
import com.Initiative.app.model.User;
import com.Initiative.app.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing user-related actions such as registration, authentication,
 * pre-registration, and password recovery.
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    /**
     * Register a new user.
     *
     * @param request The user registration details.
     * @return Response containing the authentication token.
     */
    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Registers a new user after receiving confirmation email. Returns an authentication token.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "User registered successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))
            )
    )
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterInfo request) {
        User existingUser = userService.getUserByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + request.getEmail()));

        User userToRegister = buildUserFromRequest(existingUser, request);
        return ResponseEntity.ok(authenticationService.register(userToRegister));
    }

    /**
     * Pre-register a new user by admin.
     *
     * @param request The pre-registration details.
     * @return Response containing the pre-registration code.
     */
    @PostMapping("/create")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @Operation(
            summary = "Create a new user (Admin only)",
            description = "Creates a new user account and sends an email with a registration link.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "User created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PreRegister.class))
            )
    )
    public ResponseEntity<PreRegisterCode> preRegister(@RequestBody PreRegister request) {
        return ResponseEntity.ok(authenticationService.preRegistration(request));
    }

    /**
     * Authenticate a user.
     *
     * @param request The authentication request details (email and password).
     * @return Response containing the authentication token.
     */
    @PostMapping("/authenticate")
    @Operation(
            summary = "Authenticate a user",
            description = "Authenticates a user by email and password, returning a JWT token upon success.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "User authenticated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))
            )
    )
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    /**
     * Validate a pre-registration code.
     *
     * @param preRegisterCode The pre-registration code.
     * @return Response containing the user information associated with the code.
     */
    @PostMapping("/preauthenticate")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @Operation(
            summary = "Validate user activation code",
            description = "Validates the activation code sent to the user during registration.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Activation code is valid",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PreRegister.class))
            )
    )
    public ResponseEntity<PreRegister> preAuthenticate(@RequestBody PreRegisterCode preRegisterCode) {
        return ResponseEntity.ok(authenticationService.validateCode(preRegisterCode));
    }

    /**
     * Handle forgotten password requests.
     *
     * @param passwordRecoveryInfo The password recovery details.
     * @return Response indicating success or failure of email lookup.
     */
    @PostMapping("/forgetpassword")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @Operation(
            summary = "Reset user password",
            description = "Handles forgotten password requests and sends a reset link to the user's email.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Password reset request processed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PasswordRecoveryInfo.class))
            )
    )
    public ResponseEntity<?> forgetPassword(@RequestBody PasswordRecoveryInfo passwordRecoveryInfo) {
        User user = authenticationService.passwordRecovery(passwordRecoveryInfo.getEmail());

        return ResponseEntity.ok(user);
    }

    @PostMapping("/resetpassword")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @Operation(
            summary = "Reset user password",
            description = "Handles password reset requests and updates the user's password.",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "Password reset request processed",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = PasswordRecoveryInfo.class))
            )
    )
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetInfo passwordResetInfo) {
        return ResponseEntity.ok(authenticationService.resetPassword(passwordResetInfo));
    }

    /**
     * Helper method to build a user from existing data and registration request.
     *
     * @param existingUser Existing user from the database.
     * @param request Registration details from the client.
     * @return A complete User object ready for registration.
     */
    private User buildUserFromRequest(User existingUser, RegisterInfo request) {
        return User.builder()
                .id(existingUser.getId())
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .email(request.getEmail())
                .password(request.getPassword())
                .username(request.getUsername())
                .role(existingUser.getRole())
                .build();
    }
}
