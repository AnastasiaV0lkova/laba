package com.alchotest.spring.jwt.mongodb.repository;

import com.alchotest.spring.jwt.mongodb.models.Question;
import com.alchotest.spring.jwt.mongodb.models.Test;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.Set;

public interface QuestionRepository extends MongoRepository<Question, String> {
    Optional<Question> findById(String id);

    Set<Question> findByTest(Test test);

//    Optional<Question> deleteQuestions(Question question);

}
