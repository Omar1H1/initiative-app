package com.Initiative.app.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import com.Initiative.app.model.Message;
import com.Initiative.app.model.User;
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

    @MessageMapping("/requestMessages/{idConversation}")
    public void openMessagePage(String idConversation, Authentication userAct) {

        // get the current user id from spring secuirty
        // can i say listen to my logged user id and anyone else !!

        User user = (User) userAct.getPrincipal();

        user.getId();

        var messages = mongoService.getMessagesForConversation("1", "2");
        template.convertAndSend("/getMessages", messages);
        
        mongoService.listenForNewMessages("1", "2")
            .forEach(doc -> {
                switch (doc.getOperationType()) {
                    case INSERT:
                        template.convertAndSend("/newMessage", doc.getFullDocument().toJson());
                        break;
                    case DELETE:
                        template.convertAndSend("/deleteMessage", doc.getDocumentKey().get("_id").asObjectId().getValue());
                        break;
                    case UPDATE:
                        template.convertAndSend("/updateMessage", doc.getUpdateDescription().getUpdatedFields().toJson());
                        break;
                    case REPLACE:
                        template.convertAndSend("/updateMessage", doc.getFullDocument().toJson());
                        break;
                    default:
                        log.warn("Not yet implemented OperationType: {}", doc.getOperationType());
                }
            });
    }

    @MessageMapping("/send")
    public void sendMessage(Message message) throws Exception {
        mongoService.insert(message.getSender(), message.getReceiver(), message.getContent());
    }
}
