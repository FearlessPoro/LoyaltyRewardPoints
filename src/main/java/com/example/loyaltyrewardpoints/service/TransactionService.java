package com.example.loyaltyrewardpoints.service;

import com.example.loyaltyrewardpoints.commons.RewardPointsCalculator;
import com.example.loyaltyrewardpoints.dto.TransactionDto;
import com.example.loyaltyrewardpoints.mapper.TransactionMapper;
import com.example.loyaltyrewardpoints.model.Transaction;
import com.example.loyaltyrewardpoints.model.ApplicationUser;
import com.example.loyaltyrewardpoints.repostitory.TransactionRepository;
import com.example.loyaltyrewardpoints.repostitory.ApplicationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ApplicationUserRepository userRepository;
    private final ApplicationUserService userService;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository, ApplicationUserRepository userRepository, ApplicationUserService userService) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public TransactionDto addTransaction(Long userId, TransactionDto transactionDto) throws ChangeSetPersister.NotFoundException {
        ApplicationUser user = userRepository.getReferenceById(userId);

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTimestamp(LocalDateTime.now());
        List<BigDecimal> amountList = new ArrayList<>();
        amountList.add(transactionDto.getAmount());
        transaction.setRewardPoints(RewardPointsCalculator.calculatePoints(amountList));
        transaction.setUser(user);
        userService.addUserRewardPoints(userId, transaction.getRewardPoints());

        Transaction savedTransaction = transactionRepository.save(transaction);

        return TransactionMapper.mapToDto(savedTransaction);
    }

    public Transaction updateTransaction(Long transactionId, TransactionDto transactionDto) throws ChangeSetPersister.NotFoundException {
        Transaction transaction = transactionRepository.getTransactionById(transactionId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);
        transaction.setAmount(transactionDto.getAmount());
        userService.removeUserRewardPoints(transaction.getUser().getId(), transaction.getRewardPoints());
        List<BigDecimal> amounts = new ArrayList<>();
        amounts.add(transactionDto.getAmount());
        transaction.setAmount(RewardPointsCalculator.calculatePoints(amounts));
        userService.addUserRewardPoints(transaction.getUser().getId(), transaction.getRewardPoints());

        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllUserTransactionsFromTimePeriod(final Long userId, final LocalDateTime startTime, final LocalDateTime endTime) {
        return transactionRepository.findByUserIdAndTimestampBetween(userId, startTime, endTime);
    }
}
