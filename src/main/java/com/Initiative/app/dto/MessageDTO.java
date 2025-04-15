package com.Initiative.app.dto;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MessageDTO {


    private ObjectId id;

    private Long sender;

    private Long receiver;

    private String content;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    @Builder.Default
    private boolean read = false;

    private String conversationId;

    public static String generateConversationId(Long userId1, Long userId2) {
        return userId1 < userId2
                ? userId1 + "_" + userId2
                : userId2 + "_" + userId1;
    }
} 
