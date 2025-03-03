package com.Initiative.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatNotificationDTO {
    private Long id;
    private Long senderId;
    private String senderName;
    private String message;
}