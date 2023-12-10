package com.example.loyaltyrewardpoints.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ApplicationUserDto {

    private String email;

    private BigDecimal rewardPointsForLastThreeMonths;
}
