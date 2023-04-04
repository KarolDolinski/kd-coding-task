package com.kd.coding.task.service;

import com.kd.coding.task.dto.RewardResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.kd.coding.task.util.TransactionFixtureFactory.getFixtureTransactions;
import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RewardServiceTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private RewardService rewardService;

    @Test
    void getRewards() {
        when(transactionService.getLastThreeMonthsOfTransactions()).thenReturn(getFixtureTransactions());

        List<RewardResponse> rewards = rewardService.getRewards();

        RewardResponse customerOne = rewards.get(0);
        assertEquals(4190, customerOne.getTotalRewards());
        assertEquals(3248, customerOne.getMonthlyRewards().get(APRIL));
        assertEquals(136, customerOne.getMonthlyRewards().get(FEBRUARY));
        assertEquals(806, customerOne.getMonthlyRewards().get(MARCH));
    }
}