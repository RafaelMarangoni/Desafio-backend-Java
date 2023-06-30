package com.challenge.backend.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ResponseOrderProcessed {

    private String message;
}
