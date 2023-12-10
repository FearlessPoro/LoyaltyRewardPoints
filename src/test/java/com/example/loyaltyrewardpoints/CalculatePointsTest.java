package com.example.loyaltyrewardpoints;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatePointsTest {

    @ParameterizedTest
    @CsvSource({
            "12000, 90",
            "6000, 10",
            "3000, 0",
            "15000, 150",
            "10000, 50",
            "1000000, 19850",
            "0, 0"
    })
    public void shouldCalculateMultipleTransactionsValue(BigDecimal amount, BigDecimal expectedPoints) {
        List<BigDecimal> transactionsRecord = new ArrayList<>();
        transactionsRecord.add(amount);

        PointsCalculator pointsCalculator = new PointsCalculator(transactionsRecord);
        pointsCalculator.calculatePoints();
        assertEquals(0, pointsCalculator.getPointAmount().compareTo(expectedPoints));
    }

    @Test
    public void shouldCalculateMultipleTransactionsValue() {
        List<BigDecimal> transactionsRecord = new ArrayList<>();
        transactionsRecord.add(new BigDecimal(13000)); //110
        transactionsRecord.add(new BigDecimal(10000)); //50
        transactionsRecord.add(new BigDecimal(50000)); //850

        PointsCalculator pointsCalculator = new PointsCalculator(transactionsRecord);
        pointsCalculator.calculatePoints();
        assertEquals(0, pointsCalculator.getPointAmount().compareTo(new BigDecimal(1010
        )));
    }

    @Test
    public void shouldThrowRuntimeErrorWhenAmountIsLessThanZero() {
        List<BigDecimal> transactionsRecord = new ArrayList<>();
        transactionsRecord.add(new BigDecimal(-1));

        PointsCalculator pointsCalculator = new PointsCalculator(transactionsRecord);
        Assertions.assertThrows(RuntimeException.class, pointsCalculator::calculatePoints);
    }
}
