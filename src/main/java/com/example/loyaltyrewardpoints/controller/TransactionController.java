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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transaction")
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
            TransactionDto createdTransaction = transactionService.addTransaction(userId, transactionDto);
            final String uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/api/transaction/{id}")
                    .buildAndExpand(createdTransaction.getId())
                    .toUriString();

            return ResponseEntity.created(URI.create(uri)).body(createdTransaction);
        } catch (ChangeSetPersister.NotFoundException e) {
            String errorMessage = "{\"error\": \"Bad Request\", \"message\":" +
                    " \"User with that ID does not exist. Please provide a valid userID.\"}";
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Bad Request");
            errorResponse.put("message", errorMessage);
            LOGGER.error("User with id: {} not found", userId);
            return ResponseEntity.badRequest().body(errorResponse);
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
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Bad Request");
            errorResponse.put("message", errorMessage);
            LOGGER.error("Transaction with id: {} not found", transactionId);
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/{applicationUserId}/time-period")
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
