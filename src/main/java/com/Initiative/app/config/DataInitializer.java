package com.Initiative.app.config;

import com.Initiative.app.enums.RoleEnum;
import com.Initiative.app.model.User;
import com.Initiative.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Check if users already exist
        if (userRepository.count() > 0) {
            System.out.println("Users already initialized.");
            return;
        }

        List<User> users = new ArrayList<>();
        String defaultPassword = passwordEncoder.encode("password123");

        // Generate 20 users dynamically
        for (int i = 1; i <= 20; i++) {
            RoleEnum role = switch (i % 4) {
                case 0 -> RoleEnum.ADMIN;
                case 1 -> RoleEnum.SUPERVISOR;
                case 2 -> RoleEnum.PARRAIN;
                default -> RoleEnum.PORTEUR;
            };

            users.add(User.builder()
                    .username("user" + i)
                    .firstName("FirstName" + i)
                    .lastName("LastName" + i)
                    .email("user" + i + "@example.com")
                    .password(defaultPassword)
                    .role(role)
                    .isActive(true)
                    .build());
        }

        userRepository.saveAll(users);
        System.out.println("20 dummy users initialized.");
    }
}
