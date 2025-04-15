package com.Initiative.app.controller;

import org.bson.Document;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import org.springframework.security.core.Authentication;


import com.Initiative.app.dto.MessageDTO;
import com.Initiative.app.model.User;
import com.Initiative.app.service.MongoService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MessageController {

  private MongoService mongoService;
  private SimpMessagingTemplate template;
  private final NotificationController notificationController;

  public MessageController(MongoService mongoService, SimpMessagingTemplate template, NotificationController notificationController) {
    this.mongoService = mongoService;
    this.template = template;
    this.notificationController = notificationController;
  }


  @MessageMapping("/send")
  public void sendMessage(MessageDTO message,Authentication authentication) throws Exception {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    User authenticatedUser  = (User ) userDetails;

    message.setSender(authenticatedUser.getId());

    // there's a better way maybe hide it inside the object init !!
    String conversationId = MessageDTO.generateConversationId(message.getSender(), message.getReceiver());
    message.setConversationId(conversationId);

    // let the the runtime figure it out, i'm too lazy smh
    Document insertedMessage = mongoService.insert(message);

    notificationController.sendMessageNotification(message);

    // this will create /user/r_id/queue/messages, i don't know why !!
    template.convertAndSendToUser (String.valueOf(message.getReceiver()), "/queue/messages", insertedMessage);
  }


}

