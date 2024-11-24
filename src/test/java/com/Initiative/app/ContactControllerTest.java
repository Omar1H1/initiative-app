package com.Initiative.app;

import com.Initiative.app.controller.ContactController;
import com.Initiative.app.model.ContactForm;
import com.Initiative.app.service.ContactFormService;
import com.Initiative.app.service.MailSending;


import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerTest {

    @Mock
    private ContactFormService contactFormService;

    @Mock
    private MailSending mailSending;

    @InjectMocks
    private ContactController contactController;

    private ContactForm contactForm;

    @BeforeEach
    public void setup() {
        contactForm = ContactForm.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .project("Test Project")
                .build();
    }

    @Test
    public void testCreateContactSuccess() {
        // Arrange
        ContactForm savedContact = ContactForm.builder()
                .firstname(contactForm.getFirstname())
                .lastname(contactForm.getLastname())
                .email(contactForm.getEmail())
                .project(contactForm.getProject())
                .build();
        when(contactFormService.createContact(any(ContactForm.class))).thenReturn(savedContact);

        // Act
        ResponseEntity<?> response = contactController.createContact(contactForm);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(savedContact, response.getBody());
    }

    @Test
    public void testCreateContactFailureContactFormService() {
        // Arrange
        doThrow(new RuntimeException("Test exception")).when(contactFormService).createContact(any(ContactForm.class));

        // Act
        ResponseEntity<?> response = contactController.createContact(contactForm);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertThrows(RuntimeException.class, () -> contactController.createContact(contactForm));
        assertEquals("Error creating contact: Test exception", response.getBody());
    }

    @Test
    public void testCreateContactFailureMailSending() {
        // Arrange
        ContactForm savedContact = ContactForm.builder()
                .firstname(contactForm.getFirstname())
                .lastname(contactForm.getLastname())
                .email(contactForm.getEmail())
                .project(contactForm.getProject())
                .build();
        when(contactFormService.createContact(any(ContactForm.class))).thenReturn(savedContact);
        doThrow(new RuntimeException("Test exception")).when(mailSending).sendEmail(any(String.class), any(String.class), any(String.class));

        // Act
        ResponseEntity<?> response = contactController.createContact(contactForm);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error creating contact: Test exception", response.getBody());
    }

    @Test
    public void testCreateContactNullContactForm() {
        // Act
        ResponseEntity<?> response = contactController.createContact(null);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error creating contact: Cannot invoke \"com.Initiative.Initiative.app.model.ContactForm.getFirstname()\" because \"contactForm\" is null", response.getBody());
    }
}