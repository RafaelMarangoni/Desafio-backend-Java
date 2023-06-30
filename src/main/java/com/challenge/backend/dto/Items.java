package com.challenge.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class Items {

    private int id;
    private BigDecimal price;
    private int amount;
    private BigDecimal partialAmount;
}
