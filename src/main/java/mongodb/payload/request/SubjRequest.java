package com.alchotest.spring.jwt.mongodb.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class SubjRequest {
    @NotBlank
    @Size(min = 3, max = 60)
    private String name;
}
