package com.alchotest.spring.jwt.mongodb.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "users")
@NoArgsConstructor
public class ListOfManyStudents {

    private List<ManyStudents> manyStudents = new ArrayList<>();

    public ListOfManyStudents(List<ManyStudents> manyStudents) {
        this.manyStudents = manyStudents;
    }
}
