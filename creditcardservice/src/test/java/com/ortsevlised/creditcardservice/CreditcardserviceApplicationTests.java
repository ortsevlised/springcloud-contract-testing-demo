package com.ortsevlised.creditcardservice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc

@AutoConfigureStubRunner(ids = "com.ortsevlised:creditCheckService:+:stubs:8080", stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class CreditcardserviceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldGrantApplicationWhenCreditScoreIsHigh() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.
                post("/credit-card-applications")
                .contentType(APPLICATION_JSON)
                .content("{" +
                        "\"citizenNumber\":1234," +
                        "\"cardType\":\"GOLD\"" +
                        "}"
                )).
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("GRANTED"))
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));

    }

    @Test
    public void shouldDenyApplicationWhenCreditScoreIsLow() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.
                post("/credit-card-applications")
                .contentType(APPLICATION_JSON)
                .content("{" +
                        "\"citizenNumber\":4444," +
                        "\"cardType\":\"GOLD\"" +
                        "}"
                )).
                andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DENIED"))
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));
    }
}
