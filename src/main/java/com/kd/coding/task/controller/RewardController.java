package com.kd.coding.task.controller;

import com.kd.coding.task.dto.RewardResponse;
import com.kd.coding.task.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reward")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    /**
     * Get endpoint which will generate a report with calculations
     * regarding rewards for each customer. If no data then report will be empty
     *
     * @return
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<RewardResponse> getTransactionRewards() {
        return rewardService.getRewards();
    }
}
