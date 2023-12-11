package com.example.loyaltyrewardpoints.ApiTest;

import com.example.loyaltyrewardpoints.dto.ApplicationUserDto;
import com.example.loyaltyrewardpoints.dto.TransactionDto;
import com.example.loyaltyrewardpoints.model.Transaction;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TransactionControllerApiTests {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080/api";

    }

    @Test
    @Order(1)
    public void testAddTransaction() {
        ApplicationUserDto userDto = new ApplicationUserDto();
        userDto.setEmail("test@example.com");

        given()
                .contentType(ContentType.JSON)
                .body(userDto)
                .post("/user")
                .then()
                .statusCode(HttpStatus.CREATED.value());

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(BigDecimal.valueOf(10000.0));

        given()
                .pathParam("userId", 1L)
                .contentType(ContentType.JSON)
                .body(transactionDto)
                .when()
                .post("/transaction/{userId}")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("amount", equalTo(10000.0F));
    }

    @Test
    @Order(2)
    public void testAddTransactionWithInvalidUserId() {
        given()
                .pathParam("userId", 999)
                .contentType(ContentType.JSON)
                .body("{ \"amount\": 15000.0 }")
                .when()
                .post("/transaction/{userId}")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("error", equalTo("Bad Request"))
                .body("message", containsString("User with that ID does not exist"));
    }

    @Test
    @Order(3)
    public void testUpdateTransaction() {
        given()
                .pathParam("transactionId", 1)
                .contentType(ContentType.JSON)
                .body("{ \"amount\": 20000.0 }")
                .when()
                .put("/transaction/{transactionId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("amount", equalTo(20000.0F));
    }

    @Test
    @Order(4)
    public void testUpdateTransactionWithInvalidId() {
        given()
                .pathParam("transactionId", 999)
                .contentType(ContentType.JSON)
                .body("{ \"amount\": 25000.0 }")
                .when()
                .put("/transaction/{transactionId}")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("error", equalTo("Bad Request"))
                .body("message", containsString("Transaction with that ID does not exist"));
    }

    @Test
    @Order(5)
    public void testGetAllUserTransactionsFromTimePeriod_Success() {
        final Long applicationUserId = 1L;
        final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

        final LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        final LocalDateTime endTime = LocalDateTime.now();

        // Format the date using the specified pattern
        final String formattedStartTime = startTime.format(formatter);
        final String formattedEndTime = endTime.format(formatter);

        final Response response = given()
                .param("startTime", formattedStartTime)
                .param("endTime", formattedEndTime)
                .when()
                .get("/transaction/" + applicationUserId + "/time-period")
                .then()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .extract()
                .response();

        List<Transaction> userTransactions = Arrays.asList(response.getBody().as(Transaction[].class));

        assertEquals(1, userTransactions.size());
        assertEquals(BigDecimal.valueOf(20000.00).setScale(2), userTransactions.get(0).getAmount());
    }

}
