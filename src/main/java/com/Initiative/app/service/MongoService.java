package com.Initiative.app.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.bson.BsonValue;
import org.bson.Document;
import org.springframework.stereotype.Service;

import com.Initiative.app.dto.MessageDTO;
import com.Initiative.app.model.User;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.changestream.FullDocument;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;

@Service
@Slf4j
public class MongoService {


    @Value("${mongo.url}")
    private String mongoUrl;
    @Value("${mongo.db}")
    private String mongoDb;
    private MongoClient client;
    private MongoDatabase db;
    private MongoCollection<Document> messageCollection;

    @PostConstruct
    void init() throws IOException {
        log.info("start");
        client = MongoClients.create(mongoUrl);
        db = client.getDatabase(mongoDb);
        messageCollection = db.getCollection("messages");

        log.warn("Connection to MongoDB is established !!");
    }

    public Document insert(MessageDTO messageTosend) {

        Document document = new Document("sender", messageTosend.getSender())
                .append("receiver", messageTosend.getReceiver())
                .append("content", messageTosend.getContent())
                .append("date", LocalDateTime.now())
                .append("conversationId", messageTosend.getConversationId());

        var id = messageCollection.insertOne(document).getInsertedId();

        log.info("New message inserted: " + messageTosend.getContent());

        Document insertedDocument = messageCollection.find(Filters.eq("_id", id)).first();

        return insertedDocument;
    }

    public List<Document> getMessagesByConversationId(String conversationId) {
        MongoIterable<Document> messages = messageCollection.find(Filters.eq("conversationId", conversationId))
                .sort(Sorts.descending("date"));

        return StreamSupport.stream(messages.spliterator(), false)
          .collect(Collectors.toList());
    }

    public List<String> getUserConversations(String userId) {
      Integer numericUserId = null;
      try {
        numericUserId = Integer.parseInt(userId);
      } catch (NumberFormatException e) {
        return List.of();
      }

      MongoIterable<Document> conversations = messageCollection.find(
          Filters.or(
            Filters.eq("sender", numericUserId),
            Filters.eq("receiver", numericUserId)
            )
          ).projection(
            new Document("conversationId", 1)
            ).sort(
              Sorts.descending("date")
              );

      return StreamSupport.stream(conversations.spliterator(), false)
        .map(doc -> doc.getString("conversationId"))
        .distinct()
        .collect(Collectors.toList());
    }
}
