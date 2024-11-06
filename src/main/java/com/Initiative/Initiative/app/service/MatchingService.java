package com.Initiative.Initiative.app.service;

import com.Initiative.Initiative.app.model.Match;
import com.Initiative.Initiative.app.model.User;

import java.util.List;

public interface MatchingService {
    Match createMatch(Match match);
    List<Match> hasMatches(User user);
    Match rejectMatch(Match match);
}
