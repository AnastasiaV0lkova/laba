package com.alchotest.spring.jwt.mongodb.payload.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
public class SignUpRequest {

    @NotBlank
    @Size(min = 3, max = 60)
    private String username;

    @NotBlank
    @Size(max = 60)
    private String firstName, lastName, patronymic;

    @NotBlank
    @Size(max = 60)
    @Email
    private String email;

    private Set<String> roles;
    private Set<String> subjects;
    private Set<String> classes;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
}
