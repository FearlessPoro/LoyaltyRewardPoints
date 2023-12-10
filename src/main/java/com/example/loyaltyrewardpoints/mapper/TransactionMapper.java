package com.example.loyaltyrewardpoints.mapper;

import com.example.loyaltyrewardpoints.dto.TransactionDto;
import com.example.loyaltyrewardpoints.model.Transaction;

public class TransactionMapper {

    public static Transaction mapToEntity(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDto.getAmount());
        transaction.setId(transactionDto.getId());
        return transaction;
    }

    public static TransactionDto mapToDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setId(transaction.getId());
        return transactionDto;
    }
}
