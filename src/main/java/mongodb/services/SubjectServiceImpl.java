package com.alchotest.spring.jwt.mongodb.services;

import com.alchotest.spring.jwt.mongodb.models.Subject;
import com.alchotest.spring.jwt.mongodb.payload.request.SubjRequest;
import com.alchotest.spring.jwt.mongodb.repository.SubjRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubjectServiceImpl implements ISubjService {

    private final SubjRepository subjRepository;

    @Autowired
    public SubjectServiceImpl(SubjRepository subjRepository) {
        this.subjRepository = subjRepository;
    }

    @Override
    public Subject createSubj(SubjRequest subjRequest) {
        Subject subject = new Subject(subjRequest.getName());
        return subjRepository.save(subject);
    }

    @Override
    public Boolean existsByName(String name) {
        return subjRepository.existsByName(name.toLowerCase());
    }

    @Override
    public String deleteById(String id) {
        subjRepository.deleteById(id);
        return id;
    }

    @Override
    public Optional<Subject> findById(String id) {
        return subjRepository.findById(id);
    }
}
