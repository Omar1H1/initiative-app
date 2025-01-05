package com.Initiative.app.controller;

import com.Initiative.app.model.Match;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final Map<Long, Sinks.Many<Match>> userSinks = new ConcurrentHashMap<>();

    public void sendMatchNotification(Match match) {
        Long receiverId = match.getReceiver().getId();
        Sinks.Many<Match> sink = userSinks.get(receiverId);
        if (sink != null) {
            sink.tryEmitNext(match);
        }
    }

    @GetMapping(value = "/match", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Match> streamMatchNotifications(Long userId) {
        Sinks.Many<Match> sink = userSinks.computeIfAbsent(userId, id -> Sinks.many().multicast().directAllOrNothing());
        return sink.asFlux().doOnCancel(() -> userSinks.remove(userId));
    }
}