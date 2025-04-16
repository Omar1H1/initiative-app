package com.Initiative.app.controller;

import com.Initiative.app.auth.*;
import com.Initiative.app.dto.*;
import com.Initiative.app.model.User;
import com.Initiative.app.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

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


    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Register a new user",
            description = "Registers a new user along with a mandatory profile image. The image must be of type 'image/jpeg', 'image/png', or 'image/jpg' and the size should not exceed 5MB.",
            parameters = {
                    @Parameter(name = "request", description = "Registration details including user information such as name, email, and password.", required = true,
                            schema = @Schema(implementation = User.class)),
                    @Parameter(name = "profileImage", description = "Profile image of the user. The image must be of type 'image/jpeg', 'image/png', or 'image/jpg' and not exceed 5MB in size.", required = true,
                            schema = @Schema(type = "string", format = "binary"))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "User registered successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input. The file is not an image or exceeds the size limit of 5MB.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error occurred while processing the request.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<?> register(
            @RequestPart("request") String requestJson,
            @RequestPart("profileImage") MultipartFile profileImage
    ) {
        if (profileImage == null || profileImage.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Profile image is required.");
        }

        String contentType = profileImage.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only JPEG, PNG, or JPG images are allowed.");
        }

        long maxFileSize = 5 * 1024 * 1024;
        if (profileImage.getSize() > maxFileSize) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image size exceeds the maximum limit of 5MB.");
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            User request = objectMapper.readValue(requestJson, User.class);

            byte[] profileImageData = profileImage.getBytes();
            User userToRegister = userService.prepareUserForRegistration(request, profileImageData);
            AuthenticationResponse response = authenticationService.register(userToRegister);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> getUserInfo(@PathVariable Long id) {
    
    User user = userService.getUserById(id).orElseThrow();

    return ResponseEntity.ok(user);
  }


    @GetMapping("/{id}/profile-image")
    @Operation(
            summary = "Get user's profile image",
            description = "Retrieves the profile image of a user by their ID. Only JPEG, PNG, and JPG images are accepted.",
            parameters = {
                    @Parameter(name = "id", description = "ID of the user whose profile image is to be retrieved.", required = true,
                            schema = @Schema(type = "integer", format = "int64"))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Profile image retrieved successfully.",
                            content = @Content(mediaType = "image/jpeg", schema = @Schema(type = "string", format = "binary"))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found or no profile image available.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error occurred while processing the request.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<byte[]> getProfileImage(@PathVariable Long id) {
        Optional<User> userOptional = userService.getUserById(id);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        byte[] profileImage = user.getProfileImage();

        if (profileImage == null || profileImage.length == 0) {
            return ResponseEntity.notFound().build();
        }

        String contentType = "image/jpeg";
        if (profileImage[0] == (byte) 0x89 && profileImage[1] == (byte) 0x50) {
            contentType = "image/png";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(profileImage);
    }



    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
    @PostMapping("/create")
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

    @PostMapping("/preauthenticate")
    public ResponseEntity<PreRegister> preAuthenticate(@RequestBody PreRegisterCode preRegisterCode) {
        PreRegister preRegister = authenticationService.validateCode(preRegisterCode);
        return ResponseEntity.ok(preRegister);
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
