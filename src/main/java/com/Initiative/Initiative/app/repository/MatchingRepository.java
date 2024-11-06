package com.Initiative.Initiative.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Initiative.Initiative.app.model.Match;
import org.springframework.stereotype.Repository;
import com.Initiative.Initiative.app.model.User;


import java.util.List;

@Repository
public interface MatchingRepository extends JpaRepository<Match, Long> {

    List<Match> findAllByReceiver(User receiver);
}
