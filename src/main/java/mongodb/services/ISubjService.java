package com.alchotest.spring.jwt.mongodb.services;

import com.alchotest.spring.jwt.mongodb.models.Subject;
import com.alchotest.spring.jwt.mongodb.payload.request.SubjRequest;

import java.util.Optional;

public interface ISubjService {

    Subject createSubj(SubjRequest subjRequest);

    Boolean existsByName(String username);

    String deleteById(String id);

    Optional<Subject> findById(String id);
}
