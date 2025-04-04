package com.Initiative.app.service;

import java.io.IOException;
import java.util.Arrays;

import org.bson.BsonValue;
import org.bson.Document;
import org.springframework.stereotype.Service;

import com.Initiative.app.model.User;
import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
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

    public BsonValue insert(Long sender, Long dest, String content) {

        
        var id = messageCollection.insertOne(
                new Document("sender", sender)
                        .append("dest", dest)
                        .append("content", content))
                .getInsertedId();

        log.info("new message: " + content);

        return id;
    }

    public MongoIterable<Document> getMessagesForConversation(String user1, String user2) {
        return messageCollection.aggregate(Arrays.asList(
            Aggregates.match(
                Filters.and(
                        Filters.in("sender", user1, user2),
                        Filters.in("dest", user1, user2)))))
                .map(v -> v);
    }

    public ChangeStreamIterable<Document> listenForNewMessages(String user1, String user2) {
         return messageCollection
                .watch(Arrays.asList(
                        Aggregates.match(
                            Filters.and(
                                Filters.in("fullDocument.sender", user1, user2),
                                Filters.in("fullDocument.dest", user1, user2)))))
                .fullDocument(FullDocument.UPDATE_LOOKUP);
    }
}
