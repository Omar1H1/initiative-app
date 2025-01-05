package com.Initiative.app.controller;

import com.Initiative.app.dto.UserDTO;
import com.Initiative.app.enums.RoleEnum;
import com.Initiative.app.model.User;
import com.Initiative.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class HomeController {

    private final UserService userService;


    @GetMapping("/profiles")
    @Operation(
            summary = "Get profiles based on user role",
            description = "Retrieves a list of users based on the role of the authenticated user. "
                    + "If the authenticated user is a PORTEUR, the endpoint returns a list of PARRAIN users. "
                    + "If the authenticated user has any other role, it returns a list of PORTEUR users.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of users retrieved successfully based on the role.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDTO.class,
                                    example = "[{\"id\": \"1\", \"firstName\": \"John\", \"lastName\": \"Doe\", \"email\": \"john.doe@example.com\"}, {\"id\": \"2\", \"firstName\": \"Jane\", \"lastName\": \"Smith\", \"email\": \"jane.smith@example.com\"}]"))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No users found for the given role.",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    example = "{\"error\": \"No users found\"}"))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error occurred while processing the request.",
                            content = @Content(mediaType = "application/json", schema = @Schema(
                                    example = "{\"error\": \"Internal server error\"}"))
                    )
            }
    )
    public ResponseEntity<List<UserDTO>> getAllProfiles(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (userDetails instanceof User user && Objects.equals(user.getRole(), RoleEnum.PORTEUR)) {
            List<UserDTO> parrains = userService.getUsersByRole(RoleEnum.PARRAIN);
            return ResponseEntity.ok(parrains);
        }

        List<UserDTO> porteurs = userService.getUsersByRole(RoleEnum.PORTEUR);
        return ResponseEntity.ok(porteurs);
    }


}
