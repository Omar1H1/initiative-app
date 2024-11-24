package com.Initiative.app.service;

import com.Initiative.app.enums.MatchStatus;
import com.Initiative.app.model.Match;
import com.Initiative.app.model.User;
import com.Initiative.app.repository.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingServiceImpl implements MatchingService {


    private final MatchingRepository matchingRepository;

    @Override
    public Match createMatch(Match match) {
        return matchingRepository.save(match);
    }

    @Override
    public List<Match> receiverHasMatches(User user) {
        return matchingRepository.findAllByReceiver(user);
    }

    @Override
    public List<Match> demanderHasMatches(User user) {
        return matchingRepository.findAllByDemander(user);
    }

    @Override
    public Match rejectMatch(Match match) {
        Match foundMatch = matchingRepository.findById(match.getId()).orElseThrow();

        foundMatch.setStatus(MatchStatus.rejected);

        return matchingRepository.save(foundMatch);
    }
}
