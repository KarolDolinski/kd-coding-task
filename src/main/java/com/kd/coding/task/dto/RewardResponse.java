package com.kd.coding.task.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Month;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
public class RewardResponse {
    private String customerId;
    private long totalRewards;
    private Map<Month, Long> monthlyRewards;
}
