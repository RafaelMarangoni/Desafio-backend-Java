package com.challenge.backend.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class RequestOrder {

    @NotNull(message ="userId é obrigatório")
    private Integer userID;

    @NotNull(message ="products é obritório")
    private List<Id> products;
}
