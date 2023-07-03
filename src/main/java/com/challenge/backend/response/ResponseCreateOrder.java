package com.challenge.backend.response;

import com.challenge.backend.dto.Items;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseCreateOrder {

    private UUID id;
    private int userId;
    private String status;
    private BigDecimal totalPrice;
    private List<Items> items;

}
