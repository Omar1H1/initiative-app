package com.Initiative.app.config.core;

import com.Initiative.app.enums.RoleEnum;
import com.Initiative.app.enums.SectorsOfActivity;
import com.Initiative.app.model.User;
import com.Initiative.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String[] firstNames = {"John", "Jane", "Michael", "Sarah", "David", "Emily", "Daniel", "Laura", "James", "Jessica"};
    private final String[] lastNames = {"Smith", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor"};

    private final String[] profilePictures = {
            "images/admin.png",
            "images/supervisor.png",
            "images/parrain.png",
            "images/porteur.png"
    };

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            System.out.println("Users already initialized.");
            return;
        }

        List<User> users = new ArrayList<>();
        String defaultPassword = passwordEncoder.encode("password123");
        Random random = new Random();

        for (int i = 1; i <= 20; i++) {
            RoleEnum role = switch (i % 4) {
                case 0 -> RoleEnum.ADMIN;
                case 1 -> RoleEnum.SUPERVISOR;
                case 2 -> RoleEnum.PARRAIN;
                default -> RoleEnum.PORTEUR;
            };

            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            String project = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate.";

            byte[] profileImage = loadProfileImage(role);

            users.add(User.builder()
                    .username("user" + i)
                    .firstName(firstName)
                    .lastName(lastName)
                    .email("user" + i + "@example.com")
                    .password(defaultPassword)
                    .role(role)
                    .sectorOfActivity(SectorsOfActivity.EDUCATION)
                    .projectDescription(project)
                    .isActive(true)
                    .profileImage(profileImage)
                    .build());
        }

        userRepository.saveAll(users);
        System.out.println("20 dummy users initialized.");
    }

    private byte[] loadProfileImage(RoleEnum role) {
        String imagePath;
        switch (role) {
            case ADMIN:
                imagePath = profilePictures[0];
                break;
            case SUPERVISOR:
                imagePath = profilePictures[1];
                break;
            case PARRAIN:
                imagePath = profilePictures[2];
                break;
            case PORTEUR:
                imagePath = profilePictures[3];
                break;
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }

        try (InputStream inputStream = Files.newInputStream(Paths.get("src/main/resources/" + imagePath))) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load profile image for role: " + role, e);
        }
    }
}