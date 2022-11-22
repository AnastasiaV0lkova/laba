package com.alchotest.spring.jwt.mongodb.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "status")
@Data
@NoArgsConstructor
public class TestStatus {
    @Id
    private String id;

    private ETestStatus name;
}
