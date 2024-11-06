package com.Initiative.Initiative.app.controller;

import com.Initiative.Initiative.app.model.Match;
import com.Initiative.Initiative.app.model.MatchRequest;
import com.Initiative.Initiative.app.model.User;
import com.Initiative.Initiative.app.service.MatchingService;
import com.Initiative.Initiative.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class MatchingController {

    @Autowired
    private MatchingService matchingService;

    @Autowired
    private UserService userService;

    @PostMapping("/api/v1/match")
    public Match matchRequest(@RequestBody MatchRequest matchRequest) {

        User demander = userService.getUserById(matchRequest.getDemanderId())
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
