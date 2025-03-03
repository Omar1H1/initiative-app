package com.Initiative.app.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long sender;

    @Column(nullable = false)
    private Long receiver;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    @Builder.Default
    private boolean read = false;

    @Column(nullable = false)
    private String conversationId;

    public static String generateConversationId(Long userId1, Long userId2) {
        return userId1 < userId2
                ? userId1 + "_" + userId2
                : userId2 + "_" + userId1;
    }
}