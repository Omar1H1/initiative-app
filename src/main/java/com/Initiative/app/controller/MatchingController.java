package com.Initiative.app.controller;

import com.Initiative.app.enums.MatchStatus;
import com.Initiative.app.model.Match;
import com.Initiative.app.model.MatchRequest;
import com.Initiative.app.model.User;
import com.Initiative.app.service.MatchingServiceImpl;
import com.Initiative.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingServiceImpl matchingService;
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

    /**
     * Accepts a match request.
     *
     * @param matchId the ID of the match to accept
     * @param auth the authentication object containing the requester's details
     * @return the updated match object
     */
    @PutMapping("/api/v1/match/accept/{matchId}")
    @Operation(
            summary = "Accept a Match request",
            description = "This endpoint allows a user to accept a match request. " +
                    "Upon successful acceptance, the match status will be updated.",
            parameters = {
                    @Parameter(name = "matchId", description = "The ID of the match to accept", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Match request accepted successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Match.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - Match request does not exist",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"error\": \"Match not found\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - User is not authorized to accept this match",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"error\": \"User  is not authorized to accept this match\"}"
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
    public ResponseEntity<Match> acceptMatchRequest(@PathVariable Long matchId, Authentication auth) {
        User user = (User ) auth.getPrincipal();

        Match match = matchingService.findMatchById(matchId)
                .orElseThrow(() -> new EntityNotFoundException("Match not found with ID: " + matchId));

        if (!(match.getReceiver().getId() == (user.getId()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null);
        }

        match.setStatus(MatchStatus.accepted);
        Match updatedMatch = matchingService.createMatch(match);

        notificationController.sendMatchHasBeenAccepted(updatedMatch);

        return ResponseEntity.ok(updatedMatch);
    }

    /**
     * Rejects a match request.
     *
     * @param matchId the ID of the match to reject
     * @param auth the authentication object containing the requester's details
     * @return the updated match object
     */
    @PutMapping("/api/v1/match/reject/{matchId}")
    @Operation(
            summary = "Reject a Match request",
            description = "This endpoint allows a user to reject a match request. " +
                    "Upon successful rejection, the match status will be updated.",
            parameters = {
                    @Parameter(name = "matchId", description = "The ID of the match to reject", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Match request rejected successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Match.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - Match request does not exist",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"error\": \"Match not found\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - User is not authorized to reject this match",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"error\": \"User  is not authorized to reject this match\"}"
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
    public ResponseEntity<Match> rejectMatchRequest(@PathVariable Long matchId, Authentication auth) {
        User user = (User ) auth.getPrincipal();

        Match match = matchingService.findMatchById(matchId)
                .orElseThrow(() -> new EntityNotFoundException("Match not found with ID: " + matchId));

        if (!(match.getReceiver().getId() == (user.getId()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(null);
        }

        match.setStatus(MatchStatus.rejected);
        Match updatedMatch = matchingService.createMatch(match);

        return ResponseEntity.ok(updatedMatch);
    }


    /**
     * Marks a match as seen.
     *
     * @param matchId the ID of the match to mark as seen
     * @return a response indicating the operation's success
     */
    @PutMapping("/api/v1/match/seen/{matchId}")
    @Operation(
            summary = "Mark a Match as Seen",
            description = "This endpoint allows a user to mark a match as seen. " +
                    "Upon successful operation, the match's seen status will be updated.",
            parameters = {
                    @Parameter(name = "matchId", description = "The ID of the match to mark as seen", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Match marked as seen successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Match.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - Match request does not exist",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"error\": \"Match not found\"}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Forbidden - User is not authorized to mark this match",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"error\": \"User  is not authorized to mark this match\"}"
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
    public ResponseEntity<Void> markMatchAsSeen(@PathVariable Long matchId) {

        Match match = matchingService.findMatchById(matchId)
                .orElseThrow(() -> new EntityNotFoundException("Match not found with ID: " + matchId));

        matchingService.markAsSeen(matchId);

        return ResponseEntity.ok().build();
    }


    /**
     * Checks if two users are matched.
     *
     * @param userIdA the ID of the first user
     * @param userIdB the ID of the second user
     * @return a response indicating whether the users are matched
     */
    @GetMapping("/api/v1/match/check/{userIdA}/{userIdB}")
    @Operation(
            summary = "Check if two users are matched",
            description = "This endpoint checks if two users are already matched.",
            parameters = {
                    @Parameter(name = "userIdA", description = "The ID of the first user", required = true),
                    @Parameter(name = "userIdB", description = "The ID of the second user", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully checked if users are matched",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"matched\": true}"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - One or both users do not exist",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(
                                            example = "{\"error\": \"User  not found\"}"
                                    )
                            )
                    )
            }
    )
    public ResponseEntity<?> checkIfUsersMatched(@PathVariable Long userIdA, @PathVariable Long userIdB) {
        User userA = userService.getUserById(userIdA)
                .orElseThrow(() -> new EntityNotFoundException("User  not found with ID: " + userIdA));
        User userB = userService.getUserById(userIdB)
                .orElseThrow(() -> new EntityNotFoundException("User  not found with ID: " + userIdB));

        boolean matched = matchingService.areUsersMatched(userA, userB);
        return ResponseEntity.ok().body("{\"matched\": " + matched + "}");
    }

}
