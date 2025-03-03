package com.Initiative.app.service;

import com.Initiative.app.enums.MatchStatus;
import com.Initiative.app.model.Match;
import com.Initiative.app.model.User;
import com.Initiative.app.repository.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

@RequiredArgsConstructor
public class MatchingServiceImpl {


    private final MatchingRepository matchingRepository;

    public Match createMatch(Match match) {
        return matchingRepository.save(match);
    }

    public List<Match> receiverHasMatches(User user) {
        return matchingRepository.findAllByReceiver(user);
    }

    public List<Match> demanderHasMatches(User user) {
        return matchingRepository.findAllByDemander(user);
    }

    public Match rejectMatch(Match match) {
        Match foundMatch = matchingRepository.findById(match.getId()).orElseThrow();

        foundMatch.setStatus(MatchStatus.rejected);

        return matchingRepository.save(foundMatch);
    }

    public Match acceptMatch(Match match) {
        Match foundMatch = matchingRepository.findById(match.getId()).orElseThrow();

        foundMatch.setStatus(MatchStatus.accepted);

        return matchingRepository.save(foundMatch);
    }

    public Optional<Match> findMatchById(Long id) {
        return matchingRepository.findById(id);
    }

    public void hasBeenSeen(Long id) {
        Optional<Match> match = findMatchById(id);

        match.get().setSeen(true);
    }
}
