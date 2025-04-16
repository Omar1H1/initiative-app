package com.Initiative.app.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDTO {

    @JsonIgnore
    private org.bson.types.ObjectId id;

    @JsonIgnore
    private Long sender;
    private Long receiver;
    private String content;
    
    @JsonIgnore
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    
    @JsonIgnore
    @Builder.Default
    private boolean read = false;

    @JsonIgnore
    private String conversationId;

    public static String generateConversationId(Long userId1, Long userId2) {
        return userId1 < userId2
                ? userId1 + "_" + userId2
                : userId2 + "_" + userId1;
    }
}

