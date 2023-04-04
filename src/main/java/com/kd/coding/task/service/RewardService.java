package com.kd.coding.task.service;

import com.kd.coding.task.dto.RewardResponse;
import com.kd.coding.task.model.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class RewardService {
    public static int TIER_1_REWARD = 50;
    public static int TIER_2_REWARD = 100;
    private final TransactionService transactionService;

    public List<RewardResponse> getRewards() {
        log.debug("Generating rewards report");
        Map<String, Map<Month, List<Transaction>>> collect1 = transactionService.getLastThreeMonthsOfTransactions().stream()
                .collect(groupingBy(
                        Transaction::getCustomerId,
                        groupingBy(this::getMonth)));

        log.debug("Transactions retrieved");

        List<RewardResponse> collect2 = collect1.entrySet()
                .stream()
                .map(byCustomerId -> {
                    Map<Month, Long> valuesByMonth = byCustomerId.getValue().entrySet().stream()
                            .collect(toMap(Map.Entry::getKey, value -> getRewardsPerMonth(value.getValue())));
                    long sum = valuesByMonth.values()
                            .stream()
                            .mapToLong(Long::longValue).sum();
                    return new RewardResponse(byCustomerId.getKey(), sum, valuesByMonth);
                })
                .toList();

        log.debug("Report generated for "+ collect2.size() + " customers");
        return collect2;
    }

    private Month getMonth(Transaction transaction) {
        return Month.from(transaction.getTransactionDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate());
    }

    private Long getRewardsPerMonth(List<Transaction> transactions) {
        return transactions.stream().map(this::getReward).mapToLong(Long::longValue).sum();
    }

    private long getReward(Transaction transaction) {
        if (transaction.getTransactionAmount() > TIER_1_REWARD && transaction.getTransactionAmount() <= TIER_2_REWARD) {
            return Math.round(transaction.getTransactionAmount() - TIER_1_REWARD);
        } else if (transaction.getTransactionAmount() > TIER_2_REWARD) {
            return Math.round(transaction.getTransactionAmount() - TIER_2_REWARD) * 2L
                    + (TIER_2_REWARD - TIER_1_REWARD);
        }

        return 0L;
    }
}
