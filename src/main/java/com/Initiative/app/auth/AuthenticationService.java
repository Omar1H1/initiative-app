package com.Initiative.app.auth;


import com.Initiative.app.config.core.JwtService;

import com.Initiative.app.dto.*;
import com.Initiative.app.model.User;
import com.Initiative.app.service.MailSending;
import com.Initiative.app.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Year;
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

    @Transactional
    public AuthenticationResponse register(User request) {
        User existingUser = repository.getUserByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
//        var user = User.builder()
//                .id(existingUser.getId())
//                .firstName(existingUser.getFirstName())
//                .lastName(existingUser.getLastName())
//                .username(request.getUsername())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(existingUser.getRole())
//                .isActive(true)
//                .build();
        existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
        existingUser.setUsername(request.getUsername());
        existingUser.setProfileImage(request.getProfileImage());
        existingUser.setIsActive(true);

        repository.createUser(existingUser);
        var jwtToken = jwtService.generateToken(existingUser);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    public PreRegisterCode preRegistration(PreRegister request) {
        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .role(request.getRole())
                .build();

        repository.createUser(user);

        String emailContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <style>" +
                "        body { " +
                "            font-family: Arial, sans-serif; " +
                "            background-color: #e7f3ff; /* Light blue background */" +
                "            margin: 0; " +
                "            padding: 0; " +
                "        }" +
                "        .container { " +
                "            max-width: 600px; " +
                "            background: #ffffff; " +
                "            margin: 20px auto; " +
                "            padding: 20px; " +
                "            border-radius: 10px; " +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); " +
                "        }" +
                "        .header { " +
                "            background: #007BFF; /* Blue header */" +
                "            padding: 20px; " +
                "            text-align: center; " +
                "            color: #ffffff; " +
                "            font-size: 24px; " +
                "            border-top-left-radius: 10px; " +
                "            border-top-right-radius: 10px; " +
                "        }" +
                "        .content { " +
                "            padding: 20px; " +
                "            text-align: center; " +
                "            font-size: 16px; " +
                "            color: #333333; " +
                "        }" +
                "        .btn { " +
                "            display: inline-block; " +
                "            background: #0056b3; " +
                "            color: #ffffff; " +
                "            padding: 12px 20px; " +
                "            text-decoration: none; " +
                "            font-size: 18px; " +
                "            border-radius: 5px; " +
                "            margin-top: 20px; " +
                "        }" +
                "        .footer { " +
                "            text-align: center; " +
                "            padding: 15px; " +
                "            font-size: 14px; " +
                "            color: #777777; " +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>Bienvenue chez Initiative</div>" +
                "        <div class='content'>" +
                "            <p>Bonjour <strong>" + user.getFirstName() + "</strong>,</p>" +
                "            <p>Nous sommes ravis de vous accueillir ! Cliquez sur le bouton ci-dessous pour activer votre compte :</p>" +
                "            <a href='https://omar.nocturlab.fr/signup/submitcode?code=" + user.getActivationCode() + "' class='btn'>Activer mon compte</a>" +
                "            <p>Si vous n'avez pas demandé cette inscription, vous pouvez ignorer cet e-mail en toute sécurité.</p>" +
                "        </div>" +
                "        <div class='footer'>©" +  Year.now().getValue() + " Initiative. Tous droits réservés.</div>" +
                "    </div>" +
                "</body>" +
                "</html>";

        mailSending.sendEmail(user.getEmail(), "Activer votre compte", emailContent);

        return PreRegisterCode.builder()
                .activationCode(user.getActivationCode())
                .build();
    }

    @Transactional
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

    public User passwordRecovery(String email) {
        User user = repository.getUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        user.autoGenerateActivationCode();

        String emailContent = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <style>" +
                "        body { " +
                "            font-family: Arial, sans-serif; " +
                "            background-color: #e7f3ff;" +
                "            margin: 0; " +
                "            padding: 0; " +
                "        }" +
                "        .container { " +
                "            max-width: 600px; " +
                "            background: #ffffff; " +
                "            margin: 20px auto; " +
                "            padding: 20px; " +
                "            border-radius: 10px; " +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); " +
                "        }" +
                "        .header { " +
                "            background: #007BFF;" +
                "            padding: 20px; " +
                "            text-align: center; " +
                "            color: #ffffff; " +
                "            font-size: 24px; " +
                "            border-top-left-radius: 10px; " +
                "            border-top-right-radius: 10px; " +
                "        }" +
                "        .content { " +
                "            padding: 20px; " +
                "            text-align: center; " +
                "            font-size: 16px; " +
                "            color: #333333; " +
                "        }" +
                "        .btn { " +
                "            display: inline-block; " +
                "            background: #0056b3; " +
                "            color: #ffffff; " +
                "            padding: 12px 20px; " +
                "            text-decoration: none; " +
                "            font-size: 18px; " +
                "            border-radius: 5px; " +
                "            margin-top: 20px; " +
                "        }" +
                "        .footer { " +
                "            text-align: center; " +
                "            padding: 15px; " +
                "            font-size: 14px; " +
                "            color: #777777; " +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='container'>" +
                "        <div class='header'>Récupération de Mot de Passe</div>" +
                "        <div class='content'>" +
                "            <p>Bonjour <strong>" + user.getFirstName() + "</strong>,</p>" +
                "            <p>Nous avons reçu une demande pour réinitialiser votre mot de passe. Cliquez sur le bouton ci-dessous pour réinitialiser votre mot de passe :</p>" +
                "            <a href='https://omar.nocturlab.fr/passwordreset?code=" + user.getActivationCode() + "' class='btn'>Réinitialiser le Mot de Passe</a>" +
                "            <p>Si vous n'avez pas demandé cette réinitialisation, vous pouvez ignorer cet e-mail en toute sécurité.</p>" +
                "        </div>" +
                "        <div class='footer'>©" + Year.now().getValue() + " Initiative. Tous droits réservés.</div>" +
                "    </div>" +
                "</body>" +
                "</html>";

        mailSending.sendEmail(user.getEmail(), "Récupération de Mot de Passe", emailContent);

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
                .id(user.getId())
                .token(jwtToken)
                .username(user.getUsername())
                .firstname(user.getFirstName())
                .lastname(user.getLastName())
                .role(user.getRole())
                .build();
    }

    public Optional<User> getUserByEmail(PasswordRecoveryInfo email) {
        return repository.getUserByEmail(email.getEmail());
    }
}
