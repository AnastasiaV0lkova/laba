package com.alchotest.spring.jwt.mongodb.repository;

import com.alchotest.spring.jwt.mongodb.models.ChosenAnswers;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChosenAnswersRepository extends MongoRepository<ChosenAnswers, String> {

    @Query("{ 'testId' : ?0 }")
    List<ChosenAnswers> findByTestId(String id);

    List<ChosenAnswers> findByTestIdAndStudentId(String testId, String studentId);

}
