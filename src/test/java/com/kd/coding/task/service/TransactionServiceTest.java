package com.kd.coding.task.service;

import com.kd.coding.task.dto.TransactionRequest;
import com.kd.coding.task.exception.DataNotFoundException;
import com.kd.coding.task.exception.ValidationException;
import com.kd.coding.task.mapper.TransactionMapper;
import com.kd.coding.task.model.Transaction;
import com.kd.coding.task.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.kd.coding.task.util.TransactionFixtureFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    private TransactionMapper mapper;
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(TransactionMapper.class);
        transactionService = new TransactionService(transactionRepository, mapper);
    }

    @Test
    void saveTransaction_noErrors_callsRepository() {
        TransactionRequest transactionRequest = getTransactionRequest();

        transactionService.saveTransaction(getTransactionRequest());
        ArgumentCaptor<Transaction> acTransaction = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository)
                .save(acTransaction.capture());
        Transaction value = acTransaction.getValue();
        assertEquals(transactionRequest.getTransactionId(), value.getTransactionId());
        assertEquals(transactionRequest.getTransactionAmount(), value.getTransactionAmount());
        assertEquals(transactionRequest.getCustomerId(), value.getCustomerId());
        assertEquals(transactionRequest.getTransactionDate(), value.getTransactionDate());
    }

    @Test
    void getTransaction_findsTransaction_returnsTransaction() {
        when(transactionRepository.findByTransactionId(anyString()))
                .thenReturn(Optional.of(new Transaction(TRANSACTION_ID, CUSTOMER_ID, dayOffset(0), 12L)));
        Transaction transaction = transactionService.getTransaction(TRANSACTION_ID);
        assertEquals(TRANSACTION_ID, transaction.getTransactionId());
        assertEquals(CUSTOMER_ID, transaction.getCustomerId());
    }

    @Test
    void getTransaction_noTransactionFound_throwsException() {
        when(transactionRepository.findByTransactionId(anyString()))
                .thenReturn(Optional.empty());
        assertThrows(DataNotFoundException.class, () -> {
            transactionService.getTransaction(TRANSACTION_ID);
        });
    }


    @Test
    void queryTransaction_validQuery_returnsList() {
        Transaction tr1 = new Transaction(
                TRANSACTION_ID,
                CUSTOMER_ID,
                dayOffset(0),
                12L);
        Transaction tr2 = new Transaction(
                TRANSACTION_ID1,
                CUSTOMER_ID,
                dayOffset(1),
                13L);
        when(transactionRepository.findAllByCustomerId(anyString()))
                .thenReturn(Arrays.asList(tr1, tr2));

        List<Transaction> transactions = transactionService.queryTransaction(CUSTOMER_ID);
        assertEquals(transactions.get(0).getTransactionId(), tr1.getTransactionId());
        assertEquals(transactions.get(0).getCustomerId(), tr1.getCustomerId());
        assertEquals(transactions.get(0).getTransactionAmount(), tr1.getTransactionAmount());

        assertEquals(transactions.get(1).getTransactionId(), tr2.getTransactionId());
        assertEquals(transactions.get(1).getCustomerId(), tr2.getCustomerId());
        assertEquals(transactions.get(1).getTransactionAmount(), tr2.getTransactionAmount());
    }


    @Test
    void queryTransaction_noQueryGiven_returnsList() {
        assertThrows(ValidationException.class, () -> {
            transactionService.queryTransaction(null);
        });
    }

    @Test
    void removeTransaction_callsRepository_success() {
        Transaction expectedTransaction = new Transaction(TRANSACTION_ID, CUSTOMER_ID, dayOffset(0), 12L);
        when(transactionRepository.findByTransactionId(anyString()))
                .thenReturn(Optional.of(expectedTransaction));
        transactionService.removeTransaction(TRANSACTION_ID);
        ArgumentCaptor<Transaction> acTransaction = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).delete(acTransaction.capture());
        Transaction value = acTransaction.getValue();
        assertEquals(expectedTransaction.getTransactionId(), value.getTransactionId());
        assertEquals(expectedTransaction.getTransactionAmount(), value.getTransactionAmount());
        assertEquals(expectedTransaction.getCustomerId(), value.getCustomerId());
        assertEquals(expectedTransaction.getTransactionDate(), value.getTransactionDate());
    }

    @Test
    void removeTransaction_entityNotFound_exception() {
        assertThrows(DataNotFoundException.class, () -> {
            transactionService.removeTransaction(TRANSACTION_ID);
        });
    }
}