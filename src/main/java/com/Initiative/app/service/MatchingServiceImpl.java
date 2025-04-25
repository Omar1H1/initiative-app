package com.Initiative.app.service;

import com.Initiative.app.enums.MatchStatus;
import com.Initiative.app.model.Match;
import com.Initiative.app.model.User;
import com.Initiative.app.repository.MatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MatchingServiceImpl {

    private final MatchingRepository matchingRepository;

    public Match createMatch(Match match) {
        List<Match> demanderHasMatches = matchingRepository.findAllByDemander(match.getReceiver());
        List<Match> receiverHasMatches = matchingRepository.findAllByReceiver(match.getDemander());

        Set<Long> receiverMatchIds = new HashSet<>();
        receiverHasMatches.forEach(receiverMatch -> receiverMatchIds.add(receiverMatch.getId()));

        boolean hasCommonMatch = demanderHasMatches.stream()
                .anyMatch(demanderMatch -> receiverMatchIds.contains(demanderMatch.getId()));

        if (hasCommonMatch) {
            acceptMatch(match);
        }
        return matchingRepository.save(match);
    }

    public List<Match> receiverHasMatches(User user) {
        return matchingRepository.findAllByReceiver(user);
    }

    public List<Match> demanderHasMatches(User user) {
        return matchingRepository.findAllByDemander(user);
    }

    public Match rejectMatch(Match match) {
        Match foundMatch = matchingRepository.findById(match.getId())
                .orElseThrow(() -> new IllegalArgumentException("Match not found with id: " + match.getId()));

        foundMatch.setStatus(MatchStatus.rejected);
        return matchingRepository.save(foundMatch);
    }

    public Match acceptMatch(Match match) {
        Match foundMatch = matchingRepository.findById(match.getId())
                .orElseThrow(() -> new IllegalArgumentException("Match not found with id: " + match.getId()));

        foundMatch.setStatus(MatchStatus.accepted);
        return matchingRepository.save(foundMatch);
    }

    public Optional<Match> findMatchById(Long id) {
        return matchingRepository.findById(id);
    }

    public void markAsSeen(Long id) {
        Optional<Match> match = findMatchById(id);
        match.ifPresentOrElse(m -> m.setSeen(true), 
            () -> { throw new IllegalArgumentException("Match not found with id: " + id); });
    }

    public boolean areUsersMatched(User userA, User userB) {
        return matchingRepository.existsByDemanderAndReceiver(userA, userB) ||
               matchingRepository.existsByDemanderAndReceiver(userB, userA);
    }
}
