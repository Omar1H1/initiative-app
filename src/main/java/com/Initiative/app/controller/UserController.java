package com.Initiative.app.controller;

import com.Initiative.app.auth.*;
import com.Initiative.app.dto.*;
import com.Initiative.app.model.User;
import com.Initiative.app.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Registers a new user after receiving a confirmation email. " +
                    "Upon successful registration, an authentication token is returned.",
            parameters = {
                    @Parameter(name = "request", description = "User  registration details including email, password, and any additional required fields.", required = true,
                            schema = @Schema(implementation = RegisterInfo.class))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User  registered successfully. An authentication token is returned.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class,
                                    example = "{\"token\": \"your-authentication-token\"}"))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request. The request body is missing required fields or contains invalid data.",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    example = "{\"error\": \"Email is required\"}"))
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict. A user with the provided email already exists.",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    example = "{\"error\": \"User  already exists with this email\"}"))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error occurred while processing the request.",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    example = "{\"error\": \"Internal server error\"}"))
                    )
            }
    )
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterInfo request) {
        User existingUser  = userService.getUserByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User  not found with email: " + request.getEmail()));
        User userToRegister = buildUserFromRequest(existingUser , request);
        return ResponseEntity.ok(authenticationService.register(userToRegister));
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @PostMapping("/create")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @Operation(
            summary = "Pre-register a new user (Admin only)",
            description = "This endpoint allows an admin or supervisor to pre-register a new user account. " +
                    "Upon successful registration, an email will be sent to the user with a registration link. " +
                    "This registration link will allow the user to complete their account setup.",
            parameters = {
                    @Parameter(name = "request", description = "Pre-registration details including user information such as name, email, and role.", required = true,
                            schema = @Schema(implementation = PreRegister.class))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User  created successfully. An email with a registration link has been sent to the user.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PreRegisterCode.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input. The request body is missing required fields or contains invalid data.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Conflict. A user with the provided email already exists.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error occurred while processing the request.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<PreRegisterCode> preRegister(@RequestBody PreRegister request) {
        PreRegisterCode preRegisterCode = authenticationService.preRegistration(request);
        return ResponseEntity.ok(preRegisterCode);
    }



    @PostMapping("/authenticate")
    @Operation(
            summary = "Authenticate a user",
            description = "Authenticates a user using their email and password. " +
                    "Upon successful authentication, a JWT token is returned, which can be used for subsequent requests to access protected resources.",
            parameters = {
                    @Parameter(name = "request", description = "Authentication request details including user's email and password.", required = true,
                            schema = @Schema(implementation = AuthenticationRequest.class))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User  authenticated successfully. A JWT token is returned.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class)),
                            headers = @Header(name = "Authorization", description = "JWT token for authentication", required = true)
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User  not found. The provided email is not registered.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized. The email or password provided is incorrect.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request. The request body is missing required fields or contains invalid data.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error occurred while processing the request.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
        return ResponseEntity.ok(authenticationResponse);
    }

    @PostMapping("/forgetpassword")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @Operation(
            summary = "Handle forgotten password requests",
            description = "Processes forgotten password requests by sending a password reset link to the user's registered email address. " +
                    "If the email is found, a confirmation message is sent; otherwise, an error response is provided.",
            parameters = {
                    @Parameter(name = "passwordRecoveryInfo", description = "Details for password recovery, including the user's email address.", required = true,
                            schema = @Schema(implementation = PasswordRecoveryInfo.class))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Password reset request processed successfully. An email has been sent with instructions.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PasswordRecoveryInfo.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Email not found. No user associated with the provided email address.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error occurred while processing the request.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<?> forgetPassword(@RequestBody PasswordRecoveryInfo passwordRecoveryInfo) {
        User user = authenticationService.passwordRecovery(passwordRecoveryInfo.getEmail());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/resetpassword")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = RequestMethod.POST)
    @Operation(
            summary = "Reset user password",
            description = "Handles password reset requests by updating the user's password. " +
                    "The user must provide a valid activation code and a new password to complete the reset process.",
            parameters = {
                    @Parameter(name = "passwordResetInfo", description = "Details for password reset including the reset token and new password.", required = true,
                            schema = @Schema(implementation = PasswordResetInfo.class))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Password reset successfully. The user's password has been updated.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = PasswordResetInfo.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request. The request body is missing required fields or contains invalid data.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found. The provided reset activation code is invalid or expired.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error occurred while processing the request.",
                            content = @Content(mediaType = "application/json")
                    )
            }
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