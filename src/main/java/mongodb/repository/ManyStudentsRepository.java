package com.alchotest.spring.jwt.mongodb.repository;

import com.alchotest.spring.jwt.mongodb.models.ListOfManyStudents;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ManyStudentsRepository extends MongoRepository<ListOfManyStudents, String> {
}
