package com.Initiative.Initiative.app.auth;


import com.Initiative.Initiative.app.config.JwtService;
import com.Initiative.Initiative.app.enums.RoleEnum;
import com.Initiative.Initiative.app.model.User;
import com.Initiative.Initiative.app.service.MailSending;
import com.Initiative.Initiative.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MailSending mailSending;

    @Autowired
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(RoleEnum.PORTEUR)
                .build();

        repository.createUser(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public PreRegisterCode preRegistration(PreRegisterRequest request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .role(request.getRole())
                .build();

        repository.createUser(user);

        mailSending.sendEmail(user.getEmail(), "Activation code", "Hello " + user.getLastName() + " your activation code is " + user.getActivationCode());

        return  PreRegisterCode.builder()
                .activationCode(user.getActivationCode())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = repository.getUserByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
