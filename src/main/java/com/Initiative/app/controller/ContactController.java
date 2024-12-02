package com.Initiative.app.controller;

import java.util.List;

import com.Initiative.app.service.MailSending;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @Operation(summary = "Retrieve all Contact requests",
            description = "This endpoint retrieves a list of all contact requests submitted by users.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully retrieved contact requests",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = List.class))),
                    @ApiResponse(responseCode = "500", description = "Internal server error occurred while processing the request",
                            content = @Content(mediaType = "application/json"))
            }
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERVISOR')")
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
    @Operation(summary = "Create a Contact request",
            description = "A user sends a contact request. " +
                    "On successful creation, the contact request will be processed and an email will be sent to the user.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Contact request created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ContactForm.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input, contact form is missing or invalid",
                            content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "500", description = "Internal server error occurred while processing the request",
                            content = @Content(mediaType = "application/json"))
            }
    )
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
