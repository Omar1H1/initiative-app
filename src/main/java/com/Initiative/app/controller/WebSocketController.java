package com.Initiative.app.controller;

import com.Initiative.app.model.Message;
import com.Initiative.app.model.User;
import com.Initiative.app.service.MessageService;
import com.Initiative.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
public class WebSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;

    public WebSocketController(MessageService messageService, SimpMessagingTemplate messagingTemplate, UserService userService) {
        this.messageService = messageService;
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
    }

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public void sendMessage(Message message) {




        Long senderId = message.getSenderId();
        message.setSenderId(senderId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User senderUsername = userService.getUserById(message.getSenderId()).orElseThrow(() -> new RuntimeException("User not found"));
        User receiverUsername = userService.getUserById(message.getReceiverId()).orElseThrow(() -> new RuntimeException("User not found"));

        if(!currentUsername.equals(senderUsername.getUsername())) {
            throw new RuntimeException("Unauthorized access");
        }


        messageService.sendMessage(senderId, message.getReceiverId(), message.getContent());

        log.warn("Message sent from " + senderId + " to " + message.getReceiverId() + ": " + message.getContent());

        messagingTemplate.convertAndSend("/topic/messages", message);
    }
}