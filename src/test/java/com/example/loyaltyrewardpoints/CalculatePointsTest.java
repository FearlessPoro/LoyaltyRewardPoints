package com.example.loyaltyrewardpoints;

import com.example.loyaltyrewardpoints.commons.RewardPointsCalculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CalculatePointsTest {

    @ParameterizedTest
    @CsvSource({
            "12000, 90",
            "6075, 10",
            "3000, 0",
            "15050, 151",
            "10025, 50",
            "1000010, 19850",
            "0, 0"
    })
    public void shouldCalculateMultipleTransactionsValue(BigDecimal amount, BigDecimal expectedPoints) {
        List<BigDecimal> transactionsRecord = new ArrayList<>();
        transactionsRecord.add(amount);

        assertEquals(0, RewardPointsCalculator.calculatePoints(transactionsRecord).compareTo(expectedPoints));
    }

    @Test
    public void shouldCalculateMultipleTransactionsValue() {
        List<BigDecimal> transactionsRecord = new ArrayList<>();
        transactionsRecord.add(new BigDecimal(13000)); //110
        transactionsRecord.add(new BigDecimal(10000)); //50
        transactionsRecord.add(new BigDecimal(50000)); //850

        assertEquals(0, RewardPointsCalculator.calculatePoints(transactionsRecord).compareTo(new BigDecimal(1010
        )));
    }

    @Test
    public void shouldThrowRuntimeErrorWhenAmountIsLessThanZero() {
        List<BigDecimal> transactionsRecord = new ArrayList<>();
        transactionsRecord.add(new BigDecimal(-1));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> RewardPointsCalculator.calculatePoints(transactionsRecord));
        assertEquals("Amount spent must be greater than 0", exception.getMessage());
    }

    @Test
    public void shouldReturnZeroWhenListIsEmpty() {
        List<BigDecimal> transactionsRecord = new ArrayList<>();

        assertEquals(0, RewardPointsCalculator.calculatePoints(transactionsRecord).compareTo(new BigDecimal(0
        )));
    }
}
