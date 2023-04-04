package com.kd.coding.task.repository;

import com.kd.coding.task.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
     Transaction save(Transaction transaction);

     Optional<Transaction> findByTransactionId(String transactionId);
     List<Transaction> findAllByCustomerId(String customerId);
     List<Transaction> findAllByTransactionDateBetween(Timestamp date, Timestamp to);


}
