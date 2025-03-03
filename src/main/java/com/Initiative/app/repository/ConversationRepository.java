package com.Initiative.app.repository;

import com.Initiative.app.model.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, String> {

    @Query("SELECT c FROM Conversation c WHERE c.user1Id = ?1 OR c.user2Id = ?1 ORDER BY c.lastMessageTime DESC")
    List<Conversation> findConversationsByUserId(Long userId);

    Optional<Conversation> findByUser1IdAndUser2Id(Long user1Id, Long user2Id);

    @Query("SELECT c FROM Conversation c WHERE (c.user1Id = ?1 AND c.user2Id = ?2) OR (c.user1Id = ?2 AND c.user2Id = ?1)")
    Optional<Conversation> findConversationBetweenUsers(Long user1Id, Long user2Id);
}