package com.Initiative.app.controller;

import org.bson.Document;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.Initiative.app.dto.MessageDTO;
import com.Initiative.app.service.MongoService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MessageController {

  private MongoService mongoService;
  private SimpMessagingTemplate template;

  public MessageController(MongoService mongoService, SimpMessagingTemplate template) {
    this.mongoService = mongoService;
    this.template = template;
  }


  @MessageMapping("/send")
  public void sendMessage(MessageDTO message) throws Exception {

    // there's a better way maybe hide it inside the object init !!
    String conversationId = MessageDTO.generateConversationId(message.getSender(), message.getReceiver());
    message.setConversationId(conversationId);

    // let the the runtime figure it out, i'm too lazy smh
    Document insertedMessage = mongoService.insert(message);

    // this will create /user/r_id/queue/messages, i don't know why !!
    template.convertAndSendToUser (String.valueOf(message.getReceiver()), "/queue/messages", insertedMessage);
  }


}

