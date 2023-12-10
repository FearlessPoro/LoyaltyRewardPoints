package com.example.loyaltyrewardpoints.controller;

import com.example.loyaltyrewardpoints.dto.TransactionDto;
import com.example.loyaltyrewardpoints.model.Transaction;
import com.example.loyaltyrewardpoints.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> addTransaction(
            @PathVariable Long userId,
            @RequestBody TransactionDto transactionDto) {
        try {
            return ResponseEntity.ok(transactionService.addTransaction(userId, transactionDto));
        } catch (ChangeSetPersister.NotFoundException e) {
            String errorMessage = "{\"error\": \"Bad Request\", \"message\":" +
                    " \"User with that ID does not exist. Please provide a valid userID.\"}";
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<?> updateTransaction(
            @PathVariable Long transactionId,
            @RequestBody TransactionDto updatedTransactionDto) {
        try {
            return ResponseEntity.ok(transactionService.updateTransaction(transactionId, updatedTransactionDto));
        } catch (ChangeSetPersister.NotFoundException e) {
            String errorMessage = "{\"error\": \"Bad Request\", \"message\":" +
                    " \"Transaction with that ID does not exist. Please provide a valid transactionID.\"}";
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    @GetMapping("/user/{applicationUserId}/time-period")
    public ResponseEntity<List<Transaction>> getAllUserTransactionsFromTimePeriod(
            @PathVariable Long applicationUserId,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<Transaction> userTransactions = transactionService.getAllUserTransactionsFromTimePeriod(applicationUserId, startTime, endTime);
        return ResponseEntity.ok(userTransactions);
    }
}
