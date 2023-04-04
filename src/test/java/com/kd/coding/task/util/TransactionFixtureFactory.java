package com.kd.coding.task.util;

import com.kd.coding.task.dto.TransactionRequest;
import com.kd.coding.task.model.Transaction;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
public class TransactionFixtureFactory {
    public static final String TRANSACTION_ID = "transactionId";

    public static final String TRANSACTION_ID1 = "transactionId1";

    public static final String CUSTOMER_ID = "123";

    public static final String CUSTOMER_ID1 = "1234";

    public static List<Transaction> getFixtureTransactions() {
        return List.of(new Transaction("tr1", "ct1", dayOffset(1), 100L),
                new Transaction("tr1", "ct1", dayOffset(1), 100L),
                new Transaction("tr2", "ct1", dayOffset(30), 120L),
                new Transaction("tr3", "ct1", dayOffset(60), 140L),
                new Transaction("tr4", "ct1", dayOffset(12), 13L),
                new Transaction("tr5", "ct2", dayOffset(30), 430L),
                new Transaction("tr6", "ct2", dayOffset(60), 143L),
                new Transaction("tr7", "ct2", dayOffset(2), 432L),
                new Transaction("tr8", "ct2", dayOffset(3), 1342L),
                new Transaction("tr9", "ct2", dayOffset(4), 123L),
                new Transaction("tr10", "ct3", dayOffset(1), 120L)
        );
    }

    public static Timestamp dayOffset(int days) {
        return Timestamp.valueOf(LocalDateTime.now().minusDays(days));
    }
    public static TransactionRequest getTransactionRequest() {
        return TransactionRequest.builder()
                .transactionAmount(100L)
                .transactionDate(new Timestamp(System.currentTimeMillis()))
                .build();

    }
}
