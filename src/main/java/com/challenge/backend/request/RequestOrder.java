package com.challenge.backend.request;

import com.challenge.backend.dto.Items;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class RequestOrder {

    @NotNull(message ="userId é obrigatório")
    private Integer userId;
    private List<Id> products;
    private UUID id;
    private List<Items> items;
}
