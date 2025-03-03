package com.Initiative.app.controller;

import com.Initiative.app.dto.ChatMessageDTO;
import com.Initiative.app.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageDTO chatMessage) {
        messageService.sendMessage(chatMessage)
                .ifPresent(message ->
                        messagingTemplate.convertAndSendToUser(
                                message.getReceiver().toString(),
                                "/queue/messages",
                                message
                        )
                );
    }
}