package com.Initiative.Initiative.app;



import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithAnonymousUser()
    public void testNonSecuredEndPoint() throws Exception {
        mockMvc.perform(get("/api/v1/demo/public"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser()
    public void testSecuredEndPoint() throws Exception {
        mockMvc.perform(get("/api/v1/demo/secure"))
                .andExpect(status().isForbidden());
    }


}
