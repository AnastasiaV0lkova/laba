package com.alchotest.spring.jwt.mongodb.repository;

import com.alchotest.spring.jwt.mongodb.models.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface StudentRepository extends MongoRepository<Student, String> {

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    @Query("{ 'classes' : ?0 }")
    List<Student> findByClasses(String id);

}
