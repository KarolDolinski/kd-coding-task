package com.kd.coding.task.controller;

import com.kd.coding.task.service.RewardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RewardController.class)
public class RewardControllerTest {

    public static final String REWARD_PATH = "/reward";

    @MockBean
    RewardService rewardService;

    @Autowired
    private MockMvc mvc;

    @Test
    void getRewards_validRequest_returnsOk() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get(REWARD_PATH)
                        .header("Content-Type", "application/json")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(rewardService).getRewards();
    }
}