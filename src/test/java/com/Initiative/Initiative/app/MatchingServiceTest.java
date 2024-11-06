package com.Initiative.Initiative.app;

import com.Initiative.Initiative.app.enums.MatchStatus;
import com.Initiative.Initiative.app.enums.RoleEnum;
import com.Initiative.Initiative.app.model.Match;
import com.Initiative.Initiative.app.model.User;
import com.Initiative.Initiative.app.repository.MatchingRepository;
import com.Initiative.Initiative.app.service.MatchingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class MatchingServiceTest {

    /*
      - what do i need ?
        1 - Matching Service
            1.1 - matching service needs to implement a matching service Interface
               1.1.1 - matching service interface should have the following methods
                     1.1.1.1 - createMatch
                     1.1.1.2 - find if user has a match waiting to be accepted !! "hasMatch"
                     1.1.1.3 - accept a match , user can accept the match
                     1.1.1.4 - refuse a match, user can refuse a match

     */


    @Mock
    private MatchingRepository matchingRepository;

    @InjectMocks
    private MatchingServiceImpl matchingService;


    @Test
    void TestCreateMatching () {

        // Given

        User demander = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("test1@test.fr")
                .username("johndoe")
                .role(RoleEnum.PORTEUR)
                .isActive(Boolean.TRUE)
                .build();

        User reciver = User.builder()
                .id(2L)
                .firstName("Alex")
                .lastName("ali")
                .email("test2@test.fr")
                .username("alex11")
                .role(RoleEnum.PORTEUR)
                .isActive(Boolean.TRUE)
                .build();

        Match match = Match.builder()
                .demander(demander)
                        .receiver(reciver)
                                        .build();

        Match expectedMatch = Match.builder()
                .id(1L)
                        .demander(demander)
                                .receiver(reciver)
                                        .build();


        // When

        when(matchingRepository.save(match)).thenReturn(expectedMatch);

        Match foundMatch = matchingService.createMatch(match);

        // Then

        assertEquals(expectedMatch.getId(), foundMatch.getId());
        assertEquals(expectedMatch.getDemander().getId(), foundMatch.getDemander().getId());
        assertEquals(expectedMatch.getReceiver().getId(), foundMatch.getReceiver().getId());
        assertEquals(expectedMatch.getReceiver().getUsername(), foundMatch.getReceiver().getUsername());
    }

    @Test
    void TestCreateMatchDefaultBehavior()  {

        // Given

        User demander = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("test1@test.fr")
                .username("johndoe")
                .role(RoleEnum.PORTEUR)
                .isActive(Boolean.TRUE)
                .build();

        User reciver = User.builder()
                .id(2L)
                .firstName("Alex")
                .lastName("ali")
                .email("test2@test.fr")
                .username("alex11")
                .role(RoleEnum.PORTEUR)
                .isActive(Boolean.TRUE)
                .build();

        Match match = Match.builder()
                .demander(demander)
                .receiver(reciver)
                .build();

        Match expectedMatch = Match.builder()
                .id(1L)
                .demander(demander)
                .receiver(reciver)
                .build();


        // When

        when(matchingRepository.save(match)).thenReturn(expectedMatch);

        Match foundMatch = matchingService.createMatch(match);

        // Then
        assertEquals(foundMatch.getStatus(), MatchStatus.pending);

    }
}
