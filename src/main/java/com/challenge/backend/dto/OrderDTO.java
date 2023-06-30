package com.challenge.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class OrderDTO {

    private UUID id;
    private int userId;
    private String status;
    private BigDecimal totalPrice;
}
