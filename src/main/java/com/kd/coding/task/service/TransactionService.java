package com.kd.coding.task.service;

import com.kd.coding.task.dto.TransactionRequest;
import com.kd.coding.task.exception.DataNotFoundException;
import com.kd.coding.task.exception.ValidationException;
import com.kd.coding.task.mapper.TransactionMapper;
import com.kd.coding.task.model.Transaction;
import com.kd.coding.task.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final TransactionMapper mapper;

    public Transaction saveTransaction(TransactionRequest request) {
        log.debug("Saving transaction with id " + request.getTransactionId());
        return transactionRepository.save(mapper.toModel(request));
    }

    public Transaction getTransaction(String transactionId) {
        log.debug("Retrieving transaction");
        return transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new DataNotFoundException("No Transaction found for given transactionId - " + transactionId));
    }

    public List<Transaction> queryTransaction(String customerId) {
        log.debug("Retrieving transaction for customer - " + customerId);
        if (customerId == null) {
            throw new ValidationException("transactionId and customerId cannot be empty at the same time");
        }
        return transactionRepository.findAllByCustomerId(customerId);
    }

    public void removeTransaction(String transactionId) {
        log.debug("removing transaction with id - " + transactionId);
        transactionRepository.delete(getTransaction(transactionId));
    }

    public List<Transaction> getLastThreeMonthsOfTransactions() {
        log.debug("retrieving older transactions");
        return transactionRepository.findAllByTransactionDateBetween(
                Timestamp.valueOf(LocalDateTime.now().minusMonths(3)),
                Timestamp.valueOf(LocalDateTime.now()));
    }
}
