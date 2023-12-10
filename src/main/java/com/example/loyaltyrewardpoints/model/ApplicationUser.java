package com.example.loyaltyrewardpoints.model;

import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.math.BigDecimal;

@Entity
@Data
@Getter
@Setter
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal rewardPointsForLastThreeMonths;

    private String email;
}
