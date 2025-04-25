package com.Initiative.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Initiative.app.model.Match;
import org.springframework.stereotype.Repository;
import com.Initiative.app.model.User;


import java.util.List;

@Repository
public interface MatchingRepository extends JpaRepository<Match, Long> {

  List<Match> findAllByReceiver(User receiver);
  List<Match> findAllByDemander(User demander);
  boolean existsByDemanderAndReceiver(User demander, User receiver);
}
