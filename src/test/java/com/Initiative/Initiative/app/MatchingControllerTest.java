package com.Initiative.Initiative.app;

import com.Initiative.Initiative.app.model.Match;
import com.Initiative.Initiative.app.model.MatchRequest;
import com.Initiative.Initiative.app.model.User;
import com.Initiative.Initiative.app.service.MatchingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

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

    @Mock
    private MatchingService matchingService;

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
                .id(matchRequest.getDemanderId())
                .firstName("omer")
                .build();

        User receiver = User.builder()
                .id(matchRequest.getReceiverId())
                .firstName("ali")
                .build();

        Match match = Match.builder()
                .demander(demander)
                .receiver(receiver)
                .build();


        when(matchingService.createMatch(any(Match.class))).thenReturn(match);

        // When
        ResultActions resultActions = mockMvc.perform(post("/api/v1/match")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(matchRequest)));

        // Then
        resultActions.andExpect(status().isOk());
        verify(matchingService).createMatch(any(Match.class));
    }
}