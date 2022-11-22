package com.alchotest.spring.jwt.mongodb.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ClassesRequest {
    @NotBlank
    @Size(min = 1, max = 4)
    private String name;
    @NotBlank
    @Size(min = 1, max = 3)
    private String number;

}

