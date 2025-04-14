package com.Initiative.app.controller;

import org.bson.Document;
import com.Initiative.app.service.MongoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageRestController {

    private final MongoService mongoService;

    @GetMapping("/conversation/{conversationId}")
    @Operation(
            summary = "Get conversation messages by conversation ID",
            description = "Retrieves the messages exchanged in a conversation identified by its ID.",
            parameters = {
                    @Parameter(name = "conversationId", description = "ID of the conversation.", required = true,
                            schema = @Schema(type = "string"))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Messages retrieved successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Document.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No conversation found with the specified ID.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error occurred while processing the request.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<List<Document>> getConversation(
            @PathVariable String conversationId) {
        
        List<Document> messages = mongoService.getMessagesByConversationId(conversationId);
        
        return ResponseEntity.ok(messages);
    }
}
