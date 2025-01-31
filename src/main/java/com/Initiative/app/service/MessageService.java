package com.Initiative.app.service;

import com.Initiative.app.model.Message;
import com.Initiative.app.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public Message sendMessage(Long senderId, Long receiverId, String content) {
        Message message = new Message();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public List<Message> getMessages(Long userId1, Long userId2) {
        List<Message> messages = messageRepository.findBySenderIdAndReceiverId(userId1, userId2);
        messages.addAll(messageRepository.findByReceiverIdAndSenderId(userId1, userId2));
        return messages;
    }
}