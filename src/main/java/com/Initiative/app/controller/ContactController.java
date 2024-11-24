package com.Initiative.app.controller;

import java.util.List;

import com.Initiative.app.service.MailSending;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Initiative.app.model.ContactForm;
import com.Initiative.app.service.ContactFormService;


@RestController
@RequestMapping("/api/v1/contact")
public class ContactController {

    @Autowired
    private ContactFormService contactFormService;
    private MailSending mailSending;


    /**
     * Gets all contact requests. This endpoint is intended to be used by
     * moderators.
     *
     * @return a list of contact requests
     */
    @GetMapping
    public ResponseEntity<List<ContactForm>> getContacts() {
        return ResponseEntity.ok(contactFormService.getAllContactRequests());
    }

    /**
     * Creates a new contact request.
     *
     * @param contactForm the contact form to submit
     * @return the newly created contact form, or an error response if the submission failed
     */
    @PostMapping
    public ResponseEntity<?> createContact(@RequestBody ContactForm contactForm) {
        try {
            ContactForm contactToSave = ContactForm.builder()
                    .firstname(contactForm.getFirstname())
                    .lastname(contactForm.getLastname())
                    .email(contactForm.getEmail())
                    .project(contactForm.getProject())
                    .build();

            ContactForm savedContact = contactFormService.createContact(contactToSave);

            mailSending.sendEmail(
                    savedContact.getEmail(),
                    "Contact Form",
                    "Hello " + savedContact.getFirstname() + ", your contact form has been submitted. We will get back to you as soon as possible."
            );

            // Return the saved contact as a response
            return ResponseEntity.ok(savedContact);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating contact: " + e.getMessage());
        }
    }

}
