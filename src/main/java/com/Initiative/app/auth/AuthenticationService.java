package com.Initiative.app.auth;


import com.Initiative.app.config.JwtService;

import com.Initiative.app.model.User;
import com.Initiative.app.service.MailSending;
import com.Initiative.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserService repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final MailSending mailSending;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(User request) {
        var user = User.builder()
                .id(request.getId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .isActive(true)
                .build();

        repository.createUser(user);
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public PreRegisterCode preRegistration(PreRegister request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .role(request.getRole())
                .build();

        repository.createUser(user);

        mailSending.sendEmail(user.getEmail(), "Activation code", "Hello " + user.getFirstName() + " click on this url to active your account " + "http://localhost:5173/signup/submitcode?code="  + user.getActivationCode());

        return  PreRegisterCode.builder()
                .activationCode(user.getActivationCode())
                .build();
    }

    public PreRegister validateCode(PreRegisterCode preRegisterCode) {
        log.info("Code is : {}", preRegisterCode.getActivationCode());
        Optional<User> user = repository.getUserByActivationCode(preRegisterCode.getActivationCode().trim());

        return PreRegister.builder()
                .firstName(user.get().getFirstName())
                .lastName(user.get().getLastName())
                .email(user.get().getEmail())
                .role(user.get().getRole())
                .build();
    }

    public User passwordRecovery(String  email) {
        User user = repository.getUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        user.autoGenerateActivationCode();
        mailSending.sendEmail(user.getEmail(), "Password recovery", "Hello " + user.getFirstName() + " click on this url to reset your account password " + "http://localhost:5173/passwordreset/submitcode?code="  + user.getActivationCode());

        return user;
    }

    public User resetPassword(PasswordResetInfo passwordResetInfo) {
        log.info(String.valueOf(passwordResetInfo));
        User user = repository.getUserByActivationCode(passwordResetInfo.getActivationCode())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        user.setPassword(passwordEncoder.encode(passwordResetInfo.getPassword()));
        return repository.createUser(user);
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        var user = repository.getUserByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        request.getPassword(

                        )
                )
        );


        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public Optional<User> getUserByEmail(PasswordRecoveryInfo email) {
        return repository.getUserByEmail(email.getEmail());
    }
}
