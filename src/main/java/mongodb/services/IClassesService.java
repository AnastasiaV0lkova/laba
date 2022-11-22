package com.alchotest.spring.jwt.mongodb.services;

import com.alchotest.spring.jwt.mongodb.models.Classes;
import com.alchotest.spring.jwt.mongodb.payload.request.ClassesRequest;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IClassesService {

    Classes createClass(ClassesRequest subjRequest);

    Boolean existsByNumberAndName(String number, String name);

    String deleteById(String id);

    Optional<Classes> findById(String id);

    @Query("{ 'number' : ?0 }")
    List<Classes> findByNumber(String number);

    @Query("{ 'name' : ?0 }")
    List<Classes> findByName(String name);
}
