package com.example.loyaltyrewardpoints.ApiTest;

import com.example.loyaltyrewardpoints.dto.ApplicationUserDto;
import com.example.loyaltyrewardpoints.dto.TransactionDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

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
                .statusCode(201);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setAmount(BigDecimal.valueOf(10000.0));

        given()
                .pathParam("userId", 1L)
                .contentType(ContentType.JSON)
                .body(transactionDto)
                .when()
                .post("/transaction/{userId}")
                .then()
                .statusCode(201)
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
                .statusCode(400)
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
                .statusCode(200)
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
                .statusCode(400)
                .body("error", equalTo("Bad Request"))
                .body("message", containsString("Transaction with that ID does not exist"));
    }


}
