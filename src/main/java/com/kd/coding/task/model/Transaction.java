package com.kd.coding.task.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "TRANSACTION")
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Transaction {
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "TRANSACTION_ID")
    @Id
    private String transactionId;

    @Column(name = "CUSTOMER_ID")
    private String customerId;

    @Column(name = "TRANSACTION_DATE")
    private Timestamp transactionDate;

    @Column(name = "AMOUNT")
    private long transactionAmount;
}