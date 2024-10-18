package com.Initiative.Initiative.app.controller;


import com.Initiative.Initiative.app.auth.AuthenticationRequest;
import com.Initiative.Initiative.app.auth.AuthenticationResponse;
import com.Initiative.Initiative.app.auth.AuthenticationService;
import com.Initiative.Initiative.app.auth.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationService service;

    @PostMapping("/api/v1/users")
    public ResponseEntity<AuthenticationResponse> register (@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/api/v1/users/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
