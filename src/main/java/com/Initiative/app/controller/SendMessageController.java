package com.Initiative.app.controller;

import com.Initiative.app.model.Match;
import com.Initiative.app.model.Message;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/messages")
public class SendMessageController {

    private final Map<Long, Sinks.Many<Message>> userSinks = new ConcurrentHashMap<>();

    public void sendMessage(Message message) {

        Long receiverId = message.getReceiver();

        Sinks.Many<Message> sink = userSinks.get(receiverId);
        if (sink != null) {
            sink.tryEmitNext(message);
        }
    }

    @GetMapping(value = "/send", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Message> streamMatchNotifications(Long userId) {
        Sinks.Many<Message> sink = userSinks.computeIfAbsent(userId, id -> Sinks.many().multicast().directAllOrNothing());
        return sink.asFlux().doOnCancel(() -> userSinks.remove(userId));
    }

}
