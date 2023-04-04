package com.kd.coding.task.controller;

import com.kd.coding.task.dto.TransactionRequest;
import com.kd.coding.task.model.Transaction;
import com.kd.coding.task.service.TransactionService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
@Validated
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Saves Transaction - creates new one if not exist
     * overrides when data present.
     *
     * @param request
     * @return
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction saveTransaction(@RequestBody @Valid TransactionRequest request) {
        return transactionService.saveTransaction(request);
    }

    /**
     *  Retrieves transaction for given Id
     * @exception com.kd.coding.task.exception.DataNotFoundException
     * @param transactionId
     * @return
     */
    @GetMapping("/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    public Transaction getTransaction(@PathVariable String  transactionId) {
        return transactionService.getTransaction(transactionId);
    }

    /**
     * Removes transaction for given Id
     *
     * @exception com.kd.coding.task.exception.DataNotFoundException
     *
     * @param transactionId
     */
    @DeleteMapping("/{transactionId}")
    @ResponseStatus(HttpStatus.OK)
    public void removeTransaction(@PathVariable String transactionId) {
        transactionService.removeTransaction(transactionId);
    }

    /**
     * Gets all transactions for given customers
     * it is not calculating reward
     *
     * @param customerId Id of the customer in param - cannot be null
     *
     * @return
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Transaction> getTransactions(@RequestParam@ NotBlank String customerId) {
        return transactionService.queryTransaction(customerId);
    }
}