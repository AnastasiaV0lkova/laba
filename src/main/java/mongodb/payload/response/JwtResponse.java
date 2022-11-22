package com.alchotest.spring.jwt.mongodb.payload.response;

import lombok.Data;

import java.util.List;


@Data
public class JwtResponse {
    private String accessToken;
    private String type = "Bearer";
    private List<String> roles;

    public JwtResponse(String accessToken, List<String> roles) {
        this.accessToken = accessToken;
        this.roles = roles;
    }
}
