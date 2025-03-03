package com.Initiative.app.service;

import com.Initiative.app.dto.ChatMessageDTO;
import com.Initiative.app.dto.ChatNotificationDTO;
import com.Initiative.app.enums.MatchStatus;
import com.Initiative.app.model.Conversation;
import com.Initiative.app.model.Match;
import com.Initiative.app.model.Message;
import com.Initiative.app.model.User;
import com.Initiative.app.repository.ConversationRepository;
import com.Initiative.app.repository.MatchingRepository;
import com.Initiative.app.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final MatchingRepository matchingRepository;
    private final ConversationRepository conversationRepository;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public Optional<ChatMessageDTO> sendMessage(ChatMessageDTO messageDto) {
        Optional<User> senderOpt = userService.getUserById(messageDto.getSender());
        Optional<User> receiverOpt = userService.getUserById(messageDto.getReceiver());

        if (senderOpt.isEmpty()) {
            log.warn("Sender does not exist: senderId={}", messageDto.getSender());
            return Optional.empty();
        }

        if (receiverOpt.isEmpty()) {
            log.warn("Receiver does not exist: receiverId={}", messageDto.getReceiver());
            return Optional.empty();
        }

        User sender = senderOpt.get();
        User receiver = receiverOpt.get();
        List<Match> listOfMatch = sender.getMatchList();

        boolean isFriends = matchingRepository.findAllByDemander(sender).stream()
                .anyMatch(match -> match.getReceiverId().equals(receiver.getId()) && match.getStatus() == MatchStatus.accepted) ||
                matchingRepository.findAllByReceiver(receiver).stream()
                        .anyMatch(match -> match.getDemanderId().equals(sender.getId()) && match.getStatus() == MatchStatus.accepted);

        if (!isFriends) {
            log.warn("Users are not friends: senderId={}, receiverId={}", sender.getId(), receiver.getId());
            return Optional.empty();
        }

        String conversationId = Message.generateConversationId(sender.getId(), receiver.getId());
        messageDto.setConversationId(conversationId);
        log.info("Generated conversationId: {}", conversationId);

        Conversation conversation = conversationRepository
                .findConversationBetweenUsers(sender.getId(), receiver.getId())
                .orElse(Conversation.builder()
                        .id(conversationId)
                        .user1Id(sender.getId())
                        .user2Id(receiver.getId())
                        .build());

        conversation.setLastMessageTime(LocalDateTime.now());
        conversationRepository.save(conversation);

        Message message = Message.builder()
                .sender(messageDto.getSender())
                .receiver(messageDto.getReceiver())
                .content(messageDto.getContent())
                .timestamp(LocalDateTime.now())
                .read(false)
                .conversationId(conversationId)
                .build();

        Message savedMessage = messageRepository.save(message);
        log.info("Message saved with ID: {}", savedMessage.getId());

        ChatMessageDTO responseDto = ChatMessageDTO.builder()
                .id(savedMessage.getId())
                .sender(savedMessage.getSender())
                .receiver(savedMessage.getReceiver())
                .content(savedMessage.getContent())
                .timestamp(savedMessage.getTimestamp())
                .read(savedMessage.isRead())
                .conversationId(savedMessage.getConversationId())
                .senderUsername(sender.getUsername())
                .build();

        messagingTemplate.convertAndSendToUser (
                String.valueOf(receiver.getId()),
                "/queue/messages",
                responseDto
        );

        ChatNotificationDTO notification = ChatNotificationDTO.builder()
                .id(savedMessage.getId())
                .senderId(sender.getId())
                .senderName(sender.getUsername())
                .message(savedMessage.getContent())
                .build();

        messagingTemplate.convertAndSendToUser (
                String.valueOf(receiver.getId()),
                "/queue/notifications",
                notification
        );

        return Optional.of(responseDto);
    }

    public List<ChatMessageDTO> getConversationMessages(Long user1Id, Long user2Id) {
        String conversationId = Message.generateConversationId(user1Id, user2Id);
        return messageRepository.findByConversationIdOrderByTimestampAsc(conversationId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<Conversation> getUserConversations(Long userId) {
        return conversationRepository.findConversationsByUserId(userId);
    }

    @Transactional
    public void markMessagesAsRead(Long senderId, Long receiverId) {
        List<Message> unreadMessages = messageRepository
                .findBySenderAndReceiverOrderByTimestampDesc(senderId, receiverId)
                .stream()
                .filter(message -> !message.isRead())
                .collect(Collectors.toList());

        unreadMessages.forEach(message -> message.setRead(true));
        messageRepository.saveAll(unreadMessages);
    }

    private ChatMessageDTO mapToDTO(Message message) {
        Optional<User> senderOpt = userService.getUserById(message.getSender());
        String username = senderOpt.map(User::getUsername).orElse("Unknown");

        return ChatMessageDTO.builder()
                .id(message.getId())
                .sender(message.getSender())
                .receiver(message.getReceiver())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .read(message.isRead())
                .conversationId(message.getConversationId())
                .senderUsername(username)
                .build();
    }
}