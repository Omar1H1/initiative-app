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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class MatchingController {


    private final MatchingService matchingService;
    private final UserService userService;



    /**
     * Handles a match request.
     *
     * @param matchRequest the match request
     * @return the created match
     */
    @PostMapping("/api/v1/match")
    @Operation(summary = "Create a Match request",
            description = "a user send a match request  " +
                    "On successful creation, the match request will be added to the User List of requests",
            responses = {@ApiResponse(responseCode = "200", description = "Match request created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation
                    = Match.class)))}

    )
    public Match matchRequest(@RequestBody MatchRequest matchRequest, Authentication auth) {
        User user = (User)auth.getDetails();
        User demander = userService.getUserById(user.getId())
                .orElseThrow(() -> new IllegalArgumentException("Demander not found"));
        User receiver = userService.getUserById(matchRequest.getReceiverId())
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));

        Match match = Match.builder()
                .demander(demander)
                .receiver(receiver)
                .build();

        return matchingService.createMatch(match);
    }
}
