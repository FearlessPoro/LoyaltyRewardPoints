package com.example.loyaltyrewardpoints.controller;

import com.example.loyaltyrewardpoints.commons.RewardPointsCalculator;
import com.example.loyaltyrewardpoints.dto.TransactionDto;
import com.example.loyaltyrewardpoints.model.Transaction;
import com.example.loyaltyrewardpoints.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(RewardPointsCalculator.class);

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> addTransaction(
            @PathVariable final Long userId,
            @RequestBody final TransactionDto transactionDto) {
        LOGGER.info("Saving transaction with amount: {} for user with id: {}", transactionDto.getAmount(), userId);
        try {
            return ResponseEntity.ok(transactionService.addTransaction(userId, transactionDto));
        } catch (ChangeSetPersister.NotFoundException e) {
            String errorMessage = "{\"error\": \"Bad Request\", \"message\":" +
                    " \"User with that ID does not exist. Please provide a valid userID.\"}";
            LOGGER.error("User with id: {} not found", userId);
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    @PutMapping("/{transactionId}")
    public ResponseEntity<?> updateTransaction(
            @PathVariable final Long transactionId,
            @RequestBody final TransactionDto updatedTransactionDto) {
        LOGGER.info("Updating transaction with id: {} with amount: {}", transactionId,
                updatedTransactionDto.getAmount());
        try {
            return ResponseEntity.ok(transactionService.updateTransaction(transactionId, updatedTransactionDto));
        } catch (ChangeSetPersister.NotFoundException e) {
            String errorMessage = "{\"error\": \"Bad Request\", \"message\":" +
                    " \"Transaction with that ID does not exist. Please provide a valid transactionID.\"}";
            LOGGER.error("Transaction with id: {} not found", transactionId);
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }

    @GetMapping("/user/{applicationUserId}/time-period")
    public ResponseEntity<List<Transaction>> getAllUserTransactionsFromTimePeriod(
            @PathVariable final Long applicationUserId,
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) final LocalDateTime endTime) {
        LOGGER.info("Getting all transactions for user with id: {} from {} to {}", applicationUserId,
                startTime,
                endTime);
        List<Transaction> userTransactions = transactionService.getAllUserTransactionsFromTimePeriod(applicationUserId,
                startTime,
                endTime);
        return ResponseEntity.ok(userTransactions);
    }
}
