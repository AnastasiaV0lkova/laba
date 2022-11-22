package com.alchotest.spring.jwt.mongodb.repository;

import com.alchotest.spring.jwt.mongodb.models.Subject;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SubjRepository extends MongoRepository<Subject, String> {
    Optional<Subject> findById(String id);

    Boolean existsByName(String name);
}
