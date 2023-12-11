package com.example.loyaltyrewardpoints.service;

import com.example.loyaltyrewardpoints.commons.RewardPointsCalculator;
import com.example.loyaltyrewardpoints.dto.TransactionDto;
import com.example.loyaltyrewardpoints.mapper.TransactionMapper;
import com.example.loyaltyrewardpoints.model.Transaction;
import com.example.loyaltyrewardpoints.model.ApplicationUser;
import com.example.loyaltyrewardpoints.repostitory.TransactionRepository;
import com.example.loyaltyrewardpoints.repostitory.ApplicationUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RewardPointsCalculator.class);

    private final TransactionRepository transactionRepository;
    private final ApplicationUserRepository userRepository;
    private final ApplicationUserService userService;

    @Autowired
    public TransactionService(final TransactionRepository transactionRepository,
                              final ApplicationUserRepository userRepository,
                              final ApplicationUserService userService) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public TransactionDto addTransaction(final Long userId, final TransactionDto transactionDto)
            throws ChangeSetPersister.NotFoundException {
        final ApplicationUser user = userRepository.getReferenceById(userId);
        LOGGER.info("Saving transaction with amount: {} for user with id: {}", transactionDto.getAmount(), userId);
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTimestamp(LocalDateTime.now());
        final List<BigDecimal> amountList = new ArrayList<>();
        amountList.add(transactionDto.getAmount());
        transaction.setRewardPoints(RewardPointsCalculator.calculatePoints(amountList));
        transaction.setUser(user);
        userService.addUserRewardPoints(userId, transaction.getRewardPoints());
        LOGGER.info("Adding {} reward points to user with id: {}", transaction.getRewardPoints(), userId);
        final Transaction savedTransaction = transactionRepository.save(transaction);

        return TransactionMapper.mapToDto(savedTransaction);
    }

    public Transaction updateTransaction(final Long transactionId, final TransactionDto transactionDto)
            throws ChangeSetPersister.NotFoundException {
        final Transaction transaction = transactionRepository.getTransactionById(transactionId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        LOGGER.info("Updating transaction with amount: {} with id: {}", transactionDto.getAmount(), transactionId);
        transaction.setAmount(transactionDto.getAmount());
        userService.removeUserRewardPoints(transaction.getUser().getId(), transaction.getRewardPoints());
        LOGGER.info("Removing {} reward points from user with id: {}", transaction.getRewardPoints(),
                transaction.getUser().getId());
        final List<BigDecimal> amounts = new ArrayList<>();
        amounts.add(transactionDto.getAmount());
        transaction.setRewardPoints(RewardPointsCalculator.calculatePoints(amounts));
        userService.addUserRewardPoints(transaction.getUser().getId(), transaction.getRewardPoints());
        LOGGER.info("Adding {} reward points to user with id: {}", transaction.getRewardPoints(),
                transaction.getUser().getId());
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllUserTransactionsFromTimePeriod(final Long userId,
                                                                  final LocalDateTime startTime,
                                                                  final LocalDateTime endTime) {
        return transactionRepository.findByUserIdAndTimestampBetween(userId, startTime, endTime);
    }
}
