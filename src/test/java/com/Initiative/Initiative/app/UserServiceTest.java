package com.Initiative.Initiative.app;

import com.Initiative.Initiative.app.model.User;
import com.Initiative.Initiative.app.repository.UserRepository;
import com.Initiative.Initiative.app.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;


    @Test
    public void testCreateUser() {
        // Mock data

        User userToSave = User.builder()
                .username("testusername")
                .firstName("Omer")
                .lastName("HAMAD")
                .email("test@fr.fr")
                .password("password")
                .build();

        // Define behavior of mock

        when(userRepository.save(userToSave)).thenReturn(userToSave);

        // Test the service method

        User createdUser = userService.createUser(userToSave);

        // Assertions
        assertEquals(userToSave, createdUser);
    }

    @Test
    public void testGetUserById() {
        // Mock data

        Long id = 1L;

        User userToSave = User.builder()
                .id(id)
                .username("testusername")
                .firstName("Omer")
                .lastName("HAMAD")
                .email("test@fr.fr")
                .password("password")
                .build();

        // Define behavior of mock

        when(userRepository.findById(id)).thenReturn(Optional.of(userToSave));

        // Test the service method

        Optional<User> createdUser = userService.getUserById(id);

        // Assertions
        assertEquals(userToSave, createdUser.orElseThrow());
        assertNotNull(createdUser);
        assertEquals(createdUser.get().getId(), id);
    }

    @Test
    public void testDeleteUser() {

        // Given

        long id = 1L;

        User userToDelete = User.builder()
                .id(id)
                .username("testusername")
                .firstName("Omer")
                .lastName("HAMAD")
                .email("test@fr.fr")
                .password("password")
                .build();

        // WHEN

        userService.deleteUser(userToDelete);

        // Then
        // Verify that the delete method was called once
        verify(userRepository, times(1)).delete(userToDelete);

    }
}
