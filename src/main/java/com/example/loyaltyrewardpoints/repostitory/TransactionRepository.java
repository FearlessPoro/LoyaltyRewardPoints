package com.example.loyaltyrewardpoints.repostitory;

import com.example.loyaltyrewardpoints.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> getTransactionById(Long id);

    List<Transaction> findByUserIdAndTimestampBetween(Long userId, LocalDateTime startTime, LocalDateTime endTime);
}
