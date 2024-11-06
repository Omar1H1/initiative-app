package com.Initiative.Initiative.app;

import com.Initiative.Initiative.app.enums.MatchStatus;
import com.Initiative.Initiative.app.model.Match;
import com.Initiative.Initiative.app.model.MatchRequest;
import com.Initiative.Initiative.app.model.User;
import com.Initiative.Initiative.app.repository.MatchingRepository;
import com.Initiative.Initiative.app.repository.UserRepository;
import com.Initiative.Initiative.app.service.MatchingService;
import com.Initiative.Initiative.app.service.MatchingServiceImpl;
import com.Initiative.Initiative.app.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MatchingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchingRepository matchingRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MatchingServiceImpl matchingService;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSendingMatchRequest() throws Exception {
        // Given
        MatchRequest matchRequest = MatchRequest.builder()
                .demanderId(1L)
                .receiverId(2L)
                .build();

        User demander = User.builder()
                .firstName("omer")
                .build();

        User receiver = User.builder()
                .firstName("ali")
                .build();

        Match match = Match.builder()
                .demander(demander)
                .receiver(receiver)
                .build();

        Match expectedMatch = Match.builder()
                .id(1L)
                .demander(match.getDemander())
                .receiver(match.getReceiver())
                .build();

        User expectedDemander = User.builder()
                        .id(matchRequest.getDemanderId())
                                .firstName(demander.getFirstName())
                                        .build();

        User expectedReceiver = User.builder()
                        .firstName(String.valueOf(matchRequest.getDemanderId()))
                                .firstName(String.valueOf(matchRequest.getReceiverId()))
                                        .build();




        when(userRepository.save(demander)).thenReturn(expectedDemander);
        when(userRepository.save(receiver)).thenReturn(expectedReceiver);

        when(userService.getUserById(matchRequest.getDemanderId())).thenReturn(Optional.of(demander));

        when(userService.getUserById(matchRequest.getReceiverId())).thenReturn(Optional.of(receiver));

        when(matchingRepository.save(match)).thenReturn(expectedMatch);


        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/match")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(matchRequest)));

        // Then
        resultActions.andExpect(status().isOk());
        verify(matchingService).createMatch(any(Match.class));
    }
}