package com.kd.coding.task.mapper;

import com.kd.coding.task.dto.TransactionRequest;
import com.kd.coding.task.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    Transaction toModel(TransactionRequest request);
    TransactionRequest toDto(Transaction transaction);
}