package com.kd.coding.task.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kd.coding.task.dto.RewardResponse;
import com.kd.coding.task.dto.TransactionRequest;
import com.kd.coding.task.repository.TransactionRepository;
import com.kd.coding.task.service.RewardService;
import com.kd.coding.task.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Timestamp;
import java.time.Month;
import java.util.List;

import static com.kd.coding.task.util.TransactionFixtureFactory.CUSTOMER_ID;
import static com.kd.coding.task.util.TransactionFixtureFactory.CUSTOMER_ID1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RewardIT {

    public static final String TRANSACTION_PATH = "/transaction";

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RewardService rewardService;

    @Autowired
    private TransactionRepository transactionRepository;

    TransactionService transactionService;

    @BeforeEach
    public void beforeEach() {
        transactionRepository.deleteAll();
    }

    @Test
    public void canGenerateRewardResponse() throws Exception {
        TransactionRequest request = TransactionRequest.builder()
                .transactionAmount(100L)
                .customerId(CUSTOMER_ID)
                .transactionDate(Timestamp.valueOf("2023-04-04 11:35:51.916"))
                .build();

        TransactionRequest request2 = TransactionRequest.builder()
                .transactionAmount(120L)
                .transactionDate(Timestamp.valueOf("2023-02-04 11:35:51.916"))
                .customerId(CUSTOMER_ID1)
                .build();

        mvc.perform(MockMvcRequestBuilders
                        .post(TRANSACTION_PATH)
                        .header("Content-Type", "application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mvc.perform(MockMvcRequestBuilders
                        .post(TRANSACTION_PATH)
                        .header("Content-Type", "application/json")
                        .content(objectMapper.writeValueAsString(request2))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<RewardResponse> rewards = rewardService.getRewards();
        RewardResponse customer1 = rewards.get(0);
        RewardResponse customer2 = rewards.get(1);

        assertEquals(50,customer1.getTotalRewards());
        assertEquals(50, customer1.getMonthlyRewards().get(Month.APRIL));
        assertEquals(90,customer2.getTotalRewards());
        assertEquals(90, customer2.getMonthlyRewards().get(Month.FEBRUARY));
    }

    @Test
    public void generatesRewardResponseAndNotIncludeTransactionsOlderThanThreeMonths() throws Exception {
        TransactionRequest request = TransactionRequest.builder()
                .transactionAmount(100L)
                .customerId(CUSTOMER_ID)
                .transactionDate(Timestamp.valueOf("2021-04-04 11:35:51.916"))
                .build();

        TransactionRequest request2 = TransactionRequest.builder()
                .transactionAmount(120L)
                .transactionDate(Timestamp.valueOf("2023-02-04 11:35:51.916"))
                .customerId(CUSTOMER_ID1)
                .build();

        mvc.perform(MockMvcRequestBuilders
                        .post(TRANSACTION_PATH)
                        .header("Content-Type", "application/json")
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mvc.perform(MockMvcRequestBuilders
                        .post(TRANSACTION_PATH)
                        .header("Content-Type", "application/json")
                        .content(objectMapper.writeValueAsString(request2))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        List<RewardResponse> rewards = rewardService.getRewards();
        RewardResponse customer1 = rewards.get(0);

        assertEquals(1,rewards.size());
        assertEquals(90,customer1.getTotalRewards());
        assertEquals(90, customer1.getMonthlyRewards().get(Month.FEBRUARY));
    }

    @Test
    public void generatesEmptyRewardResponseIfNoData() {
        List<RewardResponse> rewards = rewardService.getRewards();
        assertEquals(0,rewards.size());
    }
}


