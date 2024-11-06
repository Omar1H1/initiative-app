package com.Initiative.Initiative.app.service;


import com.fasterxml.jackson.databind.deser.DataFormatReaders;

public interface MatchingService {
    Match createMatch(Match match);
    List<Match> hasMatches(User user);
    Match rejectMatch(Match match);
}
