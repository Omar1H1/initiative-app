package com.Initiative.app.controller;

import com.Initiative.app.model.Match;
import com.Initiative.app.model.MatchRequest;
import com.Initiative.app.model.User;
import com.Initiative.app.service.MatchingService;
import com.Initiative.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;
    private final UserService userService;
    private final NotificationController notificationController;

    /**
     * Handles a match request.
     *
     * @param matchRequest the match request containing the receiver's ID
     * @param auth the authentication object containing the requester's details
     * @return the created match object
     */
    @PostMapping("/api/v1/match")
    @Operation(
            summary = "Create a Match request",
            description = "This endpoint allows a user to send a match request to another user. " +
                    "Upon successful creation, the match request will be added to the user's list of requests. " +
                    "The requester must be authenticated, and both users must exist in the system.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "MatchRequest object containing the ID of the user to match with.",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = MatchRequest.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Match request created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Match.class,
                                            example = "{\"demanderId\": \"1\", \"receiverId\": \"2\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request - Invalid match request or user data",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"error\": \"Invalid receiver ID\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - Either the demander or receiver does not exist",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"error\": \"User  not found\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - User is not authenticated",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"error\": \"User  is not authenticated\"}"
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<Match> matchRequest(@RequestBody MatchRequest matchRequest, Authentication auth) {
//        User user = (User ) auth.getDetails();
        User user = (User) auth.getPrincipal();
        User demander = userService.getUserById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Demander not found"));
        User receiver = userService.getUserById(matchRequest.getReceiverId())
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        Match match = Match.builder()
                .demander(demander)
                .receiver(receiver)
                .build();

        Match createdMatch = matchingService.createMatch(match);
        notificationController.sendMatchNotification(createdMatch);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMatch);
    }

}
