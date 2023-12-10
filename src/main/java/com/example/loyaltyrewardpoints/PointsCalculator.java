package com.example.loyaltyrewardpoints;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class PointsCalculator {

    public static final BigDecimal FIFTY_THRESHOLD = new BigDecimal(5000);
    public static final BigDecimal CENTS_TO_DOLLAR_CONVERTER = BigDecimal.valueOf(100);
    private final BigDecimal HUNDRED_THRESHOLD = new BigDecimal(10000);
    private final BigDecimal HUNDRED_THRESHOLD_MULTIPLIER = new BigDecimal(2);
    private final List<BigDecimal> transactionsRecord;

    @Getter
    private BigDecimal pointAmount;

    public PointsCalculator(final List<BigDecimal> transactionsRecord) {
        this.transactionsRecord = transactionsRecord;
        pointAmount = BigDecimal.ZERO;
    }

    public void calculatePoints() throws RuntimeException {
        this.pointAmount = transactionsRecord.stream()
                .map(this::calculateOneTransactionPointsAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateOneTransactionPointsAmount(final BigDecimal amount) throws RuntimeException {
        BigDecimal points = BigDecimal.ZERO;
        if (isLessThanZero(amount)) {
            throw new RuntimeException("Amount spent must be greater than 0");
        } else if (isGreaterThanSecondThreshold(amount)) {
            points = amount.subtract(HUNDRED_THRESHOLD).multiply(HUNDRED_THRESHOLD_MULTIPLIER).add(FIFTY_THRESHOLD);
        } else if (isGreaterThanFirstThreshold(amount)) {
            points = amount.subtract(FIFTY_THRESHOLD);
        }
        return points.divide(CENTS_TO_DOLLAR_CONVERTER, RoundingMode.DOWN);
    }

    private boolean isGreaterThanSecondThreshold(final BigDecimal amount) {
        return amount.compareTo(HUNDRED_THRESHOLD) > 0;
    }

    private boolean isGreaterThanFirstThreshold(final BigDecimal amount) {
        return amount.compareTo(FIFTY_THRESHOLD) >= 0;
    }

    private boolean isLessThanZero(final BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

}
