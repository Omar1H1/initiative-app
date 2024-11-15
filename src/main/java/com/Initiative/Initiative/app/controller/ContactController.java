package com.Initiative.Initiative.app.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Initiative.Initiative.app.model.ContactForm;
import com.Initiative.Initiative.app.service.ContactFormService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/contact")
public class ContactController {

    @Autowired
    private ContactFormService contactFormService;


    @GetMapping
    public ResponseEntity<List<ContactForm>> getContacts() {
        return ResponseEntity.ok(contactFormService.getAllContactRequests());
    }

    @PostMapping
    public ResponseEntity<?> createContact(@RequestBody ContactForm contactForm) {
        try {
            ContactForm contactToSave = ContactForm.builder()
                    .firstname(contactForm.getFirstname())
                    .lastname(contactForm.getLastname())
                    .email(contactForm.getEmail())
                    .project(contactForm.getProject())
                    .build();
            return ResponseEntity.ok(contactFormService.createContact(contactToSave));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating contact: " + e.getMessage());
        }
    }
}
