package com.Initiative.app.service;


import com.Initiative.app.controller.SendMessageController;
import com.Initiative.app.model.Match;
import com.Initiative.app.model.Message;
import com.Initiative.app.model.User;
import com.Initiative.app.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final SendMessageController sendMessageController;


    public Optional<Message> sendMessage(Message message) {
        Optional<User> senderOpt = userService.getUserById(message.getSender());
        Optional<User> receiverOpt = userService.getUserById(message.getReceiver());

        if (senderOpt.isEmpty() || receiverOpt.isEmpty()) {
            return Optional.empty();
        }

        User sender = senderOpt.get();
        User receiver = receiverOpt.get();
        List<Match> listOfMatch = sender.getMatchList();

        boolean isFriends = listOfMatch.stream().anyMatch(match ->
                (match.getDemander().getId() == sender.getId() && match.getReceiver().getId() == receiver.getId()) ||
                        (match.getDemander().getId() == receiver.getId() && match.getReceiver().getId() == sender.getId())
        );

        if (isFriends) {
            sendMessageController.sendMessage(message);
            return Optional.of(messageRepository.save(message));
        }

        return Optional.empty();
    }
}
