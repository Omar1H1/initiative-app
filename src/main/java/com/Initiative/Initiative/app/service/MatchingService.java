package com.Initiative.Initiative.app.service;

import com.Initiative.Initiative.app.model.Match;
import com.Initiative.Initiative.app.model.User;

import java.util.List;

public interface MatchingService {
    Match createMatch(Match match);
    List<Match> receiverHasMatches(User user);
    List<Match> demanderHasMatches(User user);
    Match rejectMatch(Match match);
}
