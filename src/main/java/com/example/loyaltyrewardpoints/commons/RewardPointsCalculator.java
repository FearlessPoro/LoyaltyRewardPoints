package com.example.loyaltyrewardpoints.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class RewardPointsCalculator {

    public static final BigDecimal FIFTY_THRESHOLD = new BigDecimal(5000);
    public static final BigDecimal CENTS_TO_DOLLAR_CONVERTER = BigDecimal.valueOf(100);
    private static final Logger LOGGER = LoggerFactory.getLogger(RewardPointsCalculator.class);
    private static final BigDecimal HUNDRED_THRESHOLD = new BigDecimal(10000);
    private static final BigDecimal HUNDRED_THRESHOLD_MULTIPLIER = new BigDecimal(2);

    public static BigDecimal calculatePoints(final List<BigDecimal> transactionRecord) throws RuntimeException {
        return transactionRecord.stream()
                .map(RewardPointsCalculator::calculateOneTransactionPointsAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal calculateOneTransactionPointsAmount(final BigDecimal amount) throws RuntimeException {
        LOGGER.info("Calculating points for amount: {}", amount);
        BigDecimal points = BigDecimal.ZERO;
        if (isLessThanZero(amount)) {
            LOGGER.error("Amount spent must be greater than 0");
            throw new RuntimeException("Amount spent must be greater than 0");
        } else if (isGreaterThanSecondThreshold(amount)) {
            points = amount.subtract(HUNDRED_THRESHOLD).multiply(HUNDRED_THRESHOLD_MULTIPLIER).add(FIFTY_THRESHOLD);
        } else if (isGreaterThanFirstThreshold(amount)) {
            points = amount.subtract(FIFTY_THRESHOLD);
        }
        LOGGER.info("Calculated points: {}", points);
        return points.divide(CENTS_TO_DOLLAR_CONVERTER, RoundingMode.DOWN);
    }

    private static boolean isGreaterThanSecondThreshold(final BigDecimal amount) {
        return amount.compareTo(HUNDRED_THRESHOLD) > 0;
    }

    private static boolean isGreaterThanFirstThreshold(final BigDecimal amount) {
        return amount.compareTo(FIFTY_THRESHOLD) >= 0;
    }

    private static boolean isLessThanZero(final BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

}
