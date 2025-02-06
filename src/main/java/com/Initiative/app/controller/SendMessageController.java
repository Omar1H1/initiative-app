package com.Initiative.app.controller;

import com.Initiative.app.model.Message;
import com.Initiative.app.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * REST controller for managing message-related actions such as sending and streaming messages.
 */
@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
@Slf4j
public class SendMessageController {

    private final Map<Long, Sinks.Many<Message>> userSinks = new ConcurrentHashMap<>();
    private MessageService messageService;

    @Autowired
    public void setMessageService(@Lazy MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    @Operation(
            summary = "Send a message",
            description = "Sends a message to a specified user. The message will be streamed to the user if they are connected.",
            parameters = {
                    @Parameter(name = "message", description = "Message object containing sender, receiver, and content.", required = true,
                            schema = @Schema(implementation = Message.class))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Message sent successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input. The message object is missing required fields.",
                            content = @Content(mediaType = "application/json")
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error occurred while processing the request.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public ResponseEntity<?> saveMessage(@RequestBody Message message) {
        try {
            Optional<Message> savedMessage = messageService.sendMessage(message);
            if (savedMessage.isPresent()) {
                sendMessage(savedMessage.get());
                return ResponseEntity.ok(savedMessage.get());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to send message.");
            }
        } catch (Exception e) {
            log.error("Error sending message: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping(value = "/send", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(
            summary = "Stream match notifications",
            description = "Streams messages to the specified user in real-time.",
            parameters = {
                    @Parameter(name = "userId", description = "ID of the user to stream messages to.", required = true,
                            schema = @Schema(type = "integer", format = "int64"))
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Streaming messages successfully.",
                            content = @Content(mediaType = "text/event-stream", schema = @Schema(implementation = Message.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User  not found.",
                            content = @Content(mediaType = "application/json")
                    )
            }
    )
    public Flux<Message> streamMatchNotifications(@RequestParam Long userId) {
        Sinks.Many<Message> sink = userSinks.computeIfAbsent(userId, id -> Sinks.many().multicast().directAllOrNothing());
        return sink.asFlux().doOnCancel(() -> userSinks.remove(userId));
    }

    public void sendMessage(Message message) {
        Long receiverId = message.getReceiver();
        Sinks.Many<Message> sink = userSinks.get(receiverId);
        if (sink != null) {
            sink.tryEmitNext(message);
        }
    }
}