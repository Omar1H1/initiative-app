package com.Initiative.app.repository;

import com.Initiative.app.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationIdOrderByTimestampAsc(String conversationId);
    List<Message> findBySenderAndReceiverOrderByTimestampDesc(Long sender, Long receiver);
    List<Message> findByReceiverAndReadFalse(Long receiver);
}