package com.Initiative.app.controller;

import com.Initiative.app.dto.ChatMessageDTO;
import com.Initiative.app.model.Conversation;
import com.Initiative.app.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing messaging functionalities including retrieving conversations,
 * sending messages, and marking messages as read.
 */
@RestController
@RequestMapping("/api/v1/messages")
public class MessageRestController {

    private final MessageService messageService;

  public MessageRestController (MessageService messageService) {
    this.messageService = messageService;
  }

    @GetMapping("/conversation/{user1Id}/{user2Id}")
    @Operation(
            summary = "Get conversation between two users",
            description = "Retrieves the messages exchanged between two users identified by their IDs.",
            parameters = {
                    @Parameter(name = "user1Id", description = "ID of the first user.", required = true,
                            schema = @Schema(type = "integer", format = "int64")),
                    @Parameter(name = "user2Id", description = "ID of the second user.", required = true,
                            schema = @Schema(type = "integer", format = "int64"))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Messages retrieved successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatMessageDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No conversation found between the specified users.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error occurred while processing the request.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<List<ChatMessageDTO>> getConversation(
            @PathVariable Long user1Id,
            @PathVariable Long user2Id) {
        return ResponseEntity.ok(messageService.getConversationMessages(user1Id, user2Id));
    }

    @GetMapping("/conversations/{userId}")
    @Operation(
            summary = "Get all conversations for a user",
            description = "Retrieves a list of conversations for a specified user identified by their ID.",
            parameters = {
                    @Parameter(name = "userId", description = "ID of the user whose conversations are to be retrieved.", required = true,
                            schema = @Schema(type = "integer", format = "int64"))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Conversations retrieved successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Conversation.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User  not found or no conversations available.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error occurred while processing the request.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<List<Conversation>> getUserConversations(@PathVariable Long userId) {
        return ResponseEntity.ok(messageService.getUserConversations(userId));
    }

    @PostMapping("/read/{senderId}/{receiverId}")
    @Operation(
            summary = "Mark messages as read",
            description = "Marks all messages sent from a sender to a receiver as read.",
            parameters = {
                    @Parameter(name = "senderId", description = "ID of the sender.", required = true,
                            schema = @Schema(type = "integer", format = "int64")),
                    @Parameter(name = "receiverId", description = "ID of the receiver.", required = true,
                            schema = @Schema(type = "integer", format = "int64"))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Messages marked as read successfully."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Sender or receiver not found.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error occurred while processing the request.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<?> markMessagesAsRead(
            @PathVariable Long senderId,
            @PathVariable Long receiverId) {
        messageService.markMessagesAsRead(senderId, receiverId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send")
    @Operation(
            summary = "Send a message",
            description = "Sends a message from one user to another.",
            parameters = {
                    @Parameter(name = "message", description = "Message details including sender, receiver, and content.", required = true,
                            schema = @Schema(implementation = ChatMessageDTO.class))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Message sent successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChatMessageDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad Request. The request body is missing required fields or contains invalid data.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error occurred while processing the request.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<ChatMessageDTO> sendMessage(@RequestBody ChatMessageDTO message) {
        return messageService.sendMessage(message)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.badRequest().build());
    }
}
