package com.challenge.backend.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Getter
@Setter
public class RequestProcessorOrder {

    private UUID id;
    private Integer userId;
    private String status;

}
