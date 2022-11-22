package com.alchotest.spring.jwt.mongodb.repository;

import com.alchotest.spring.jwt.mongodb.models.EQuestion;
import com.alchotest.spring.jwt.mongodb.models.QuestionType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface QuestionTypeRepository extends MongoRepository<QuestionType, String> {
    Optional<QuestionType> findByName(EQuestion name);
    Optional<QuestionType> findById(String id);
}
