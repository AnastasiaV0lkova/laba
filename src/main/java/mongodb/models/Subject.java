package com.alchotest.spring.jwt.mongodb.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Size;

@Data
@Document(collection = "subject")
@NoArgsConstructor
public class Subject {
    @Id
    private String id;
    @Size(max = 120)
    private String name;

    public Subject(String name) {
        this.name = name;
    }

}
