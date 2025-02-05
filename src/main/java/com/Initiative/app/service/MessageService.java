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

        Optional<User> send = userService.getUserById(message.getSender());
        Optional<User> receiver = userService.getUserById(message.getSender());
        List<Match> listOfMatch = send.get().getMatchList();
        boolean isFriends = false;

        if (receiver.isEmpty()) {
            return Optional.empty();
        }

        for(Match match : listOfMatch) {

            if(match.getDemander().getId() == send.get().getId() && match.getReceiver().getId() == receiver.get().getId() || match.getDemander().getId() == receiver.get().getId() && match.getReceiver().getId() == send.get().getId()) {
                isFriends = true;
            }

        }

        if(isFriends) {
            sendMessageController.sendMessage(message);
            return Optional.of(messageRepository.save(message));
        }

        return Optional.empty();
    }
}
