package com.Initiative.app.controller;

import com.Initiative.app.dto.MessageDTO;
import com.Initiative.app.enums.MatchStatus;
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

  private final Map<Long, Sinks.Many<Match>> matchSinks = new ConcurrentHashMap<>();
  private final Map<Long, Sinks.Many<MessageDTO>> messageSinks = new ConcurrentHashMap<>();

  public void sendMatchNotification(Match match) {
    Long receiverId = match.getReceiver().getId();
    Sinks.Many<Match> sink = matchSinks.get(receiverId);
    if (sink != null && shouldSendNotification(match)) {
      sink.tryEmitNext(match);
    }
  }

  public void sendMessageNotification(MessageDTO message) {
    Long receiverId = message.getReceiver();
    Sinks.Many<MessageDTO> sink = messageSinks.get(receiverId);
    if (sink != null) {
      sink.tryEmitNext(message);
    }
  }

  @GetMapping(value = "/match", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<Match> streamMatchNotifications(Long userId) {
    Sinks.Many<Match> sink = matchSinks.computeIfAbsent(userId, id -> Sinks.many().multicast().directAllOrNothing());
    return sink.asFlux().doOnCancel(() -> matchSinks.remove(userId));
  }

  @GetMapping(value = "/messages", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Flux<MessageDTO> streamMessageNotifications(Long userId) {
    Sinks.Many<MessageDTO> sink = messageSinks.computeIfAbsent(userId, id -> Sinks.many().multicast().directAllOrNothing());
    return sink.asFlux().doOnCancel(() -> messageSinks.remove(userId));
  }

  public void sendMatchHasBeenAccepted(Match match) {
    Long demanderId = match.getDemander().getId();
    Sinks.Many<Match> sink = matchSinks.get(demanderId);
    if (sink != null && match.getStatus() == MatchStatus.accepted) {
      sink.tryEmitNext(match);
    }
  }

  private boolean shouldSendNotification(Match match) {
    return !match.isSeen();
  }
}
