package com.Initiative.Initiative.app.service;

import com.Initiative.Initiative.app.enums.MatchStatus;
import com.Initiative.Initiative.app.model.Match;
import com.Initiative.Initiative.app.model.User;
import com.Initiative.Initiative.app.repository.MatchingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchingServiceImpl implements MatchingService {

    @Autowired
    private MatchingRepository matchingRepository;

    @Override
    public Match createMatch(Match match) {
        return matchingRepository.save(match);
    }

    @Override
    public List<Match> hasMatches(User user) {
        return matchingRepository.findAllByReceiver(user);
    }

    @Override
    public Match rejectMatch(Match match) {
        Match foundMatch = matchingRepository.findById(match.getId()).orElseThrow();

        foundMatch.setStatus(MatchStatus.rejected);

        return matchingRepository.save(foundMatch);
    }
}
