package com.example.loyaltyrewardpoints;

import com.example.loyaltyrewardpoints.dto.TransactionDto;
import com.example.loyaltyrewardpoints.model.ApplicationUser;
import com.example.loyaltyrewardpoints.model.Transaction;
import com.example.loyaltyrewardpoints.repostitory.ApplicationUserRepository;
import com.example.loyaltyrewardpoints.repostitory.TransactionRepository;
import com.example.loyaltyrewardpoints.service.ApplicationUserService;
import com.example.loyaltyrewardpoints.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ApplicationUserRepository applicationUserRepository;

    @Mock
    private ApplicationUserService applicationUserService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddTransaction() throws ChangeSetPersister.NotFoundException {
        final Long userId = 1L;
        int tenDollarsInCents = 1000;
        final ApplicationUser user = new ApplicationUser();
        user.setId(userId);
        when(applicationUserRepository.getReferenceById(userId)).thenReturn(user);

        TransactionDto transactionDto = new TransactionDto();

        transactionDto.setAmount(BigDecimal.valueOf(tenDollarsInCents));

        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction savedTransaction = invocation.getArgument(0);
            savedTransaction.setId(userId);
            return savedTransaction;
        });

        TransactionDto result = transactionService.addTransaction(userId, transactionDto);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(tenDollarsInCents), result.getAmount());
        verify(applicationUserService, times(1)).addUserRewardPoints(userId, BigDecimal.ZERO);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testUpdateTransaction() throws ChangeSetPersister.NotFoundException {
        final Long transactionId = 1L;
        int sixtyDollarsInCents = 6000;
        final Long userId = 1L;
        final ApplicationUser user = new ApplicationUser();
        user.setId(userId);

        final Transaction existingTransaction = new Transaction();
        existingTransaction.setId(transactionId);
        existingTransaction.setUser(user);
        existingTransaction.setAmount(BigDecimal.valueOf(sixtyDollarsInCents));
        existingTransaction.setRewardPoints(BigDecimal.TEN);



        when(transactionRepository.getTransactionById(transactionId)).thenReturn(Optional.of(existingTransaction));
        when(transactionRepository.save(any(Transaction.class)))
                .thenAnswer(invocation -> invocation.<Transaction>getArgument(0));

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(BigDecimal.valueOf(sixtyDollarsInCents));

        Transaction result = transactionService.updateTransaction(transactionId, transactionDto);

        assertNotNull(result);
        assertEquals(transactionId, result.getId());
        assertEquals(BigDecimal.valueOf(sixtyDollarsInCents), result.getAmount());
        verify(applicationUserService, times(1)).removeUserRewardPoints(userId, BigDecimal.TEN);
        verify(applicationUserService, times(1)).addUserRewardPoints(userId, BigDecimal.TEN);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testGetAllUserTransactionsFromTimePeriod() {
        Long userId = 1L;
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();
        List<Transaction> transactions = new ArrayList<>();

        when(transactionRepository.findByUserIdAndTimestampBetween(userId, oneDayAgo, endTime)).thenReturn(transactions);

        List<Transaction> result = transactionService.getAllUserTransactionsFromTimePeriod(userId, oneDayAgo, endTime);

        assertNotNull(result);
        assertEquals(transactions, result);
        verify(transactionRepository, times(1)).findByUserIdAndTimestampBetween(userId, oneDayAgo, endTime);
    }
}
