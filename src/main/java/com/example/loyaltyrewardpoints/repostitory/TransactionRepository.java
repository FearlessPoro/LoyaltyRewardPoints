package com.example.loyaltyrewardpoints.repostitory;

import com.example.loyaltyrewardpoints.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> getTransactionById(final Long id);

    List<Transaction> findByUserIdAndTimestampBetween(final Long userId, final LocalDateTime startTime, LocalDateTime endTime);
}
