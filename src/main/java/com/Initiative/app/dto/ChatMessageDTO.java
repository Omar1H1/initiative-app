package com.Initiative.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    private Long id;
    private Long sender;
    private Long receiver;
    private String content;
    private LocalDateTime timestamp;
    private boolean read;
    private String conversationId;
    private String senderUsername;
}