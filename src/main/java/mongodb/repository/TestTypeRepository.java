package com.alchotest.spring.jwt.mongodb.repository;

import com.alchotest.spring.jwt.mongodb.models.ETestStatus;
import com.alchotest.spring.jwt.mongodb.models.TestStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface TestTypeRepository extends MongoRepository<TestStatus, String> {
    Optional<TestStatus> findByName(ETestStatus name);

    Optional<TestStatus> findById(String id);

    Boolean existsByName(String name);

}
