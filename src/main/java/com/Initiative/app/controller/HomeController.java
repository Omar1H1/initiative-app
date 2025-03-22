package com.Initiative.app.controller;

import com.Initiative.app.dto.UserDTO;
import com.Initiative.app.enums.RoleEnum;
import com.Initiative.app.enums.SectorsOfActivity;
import com.Initiative.app.model.Match;
import com.Initiative.app.model.User;
import com.Initiative.app.service.MatchingServiceImpl;
import com.Initiative.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

import org.apache.el.stream.Stream;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class HomeController {

    private final UserService userService;
    private final MatchingServiceImpl matchingService; 

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
    User authenticatedUser  = (User ) userDetails;

    List<UserDTO> potentialMatches;

    if (Objects.equals(authenticatedUser.getRole(), RoleEnum.PORTEUR)) {
      potentialMatches = userService.getUsersByRole(RoleEnum.PARRAIN);
    } else {
      potentialMatches = userService.getUsersByRole(RoleEnum.PORTEUR);
    }

    List<Match> matchedWithUser  = matchingService.receiverHasMatches(authenticatedUser );
    matchedWithUser .addAll(matchingService.demanderHasMatches(authenticatedUser ));

    Set<Long> matchedUserIds = new HashSet<>();
    for (Match match : matchedWithUser ) {
      if (match.getDemanderId() != null) {
        matchedUserIds.add(match.getDemanderId());
      }
      if (match.getReceiverId() != null) {
        matchedUserIds.add(match.getReceiverId());
      }
    }

    List<UserDTO> filteredMatches = potentialMatches.stream()
    .filter(userDTO -> !matchedUserIds.contains(userDTO.getId()))
    .collect(Collectors.toList());

    return ResponseEntity.ok(filteredMatches);
  }



  @GetMapping("/sectors")
  public List<SectorsOfActivity> getSectors() {
    return Arrays.asList(SectorsOfActivity.values());
  }



}
