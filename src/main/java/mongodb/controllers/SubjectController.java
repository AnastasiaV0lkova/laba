package com.alchotest.spring.jwt.mongodb.controllers;

import com.alchotest.spring.jwt.mongodb.models.Subject;
import com.alchotest.spring.jwt.mongodb.payload.request.SubjRequest;
import com.alchotest.spring.jwt.mongodb.repository.SubjRepository;
import com.alchotest.spring.jwt.mongodb.services.ISubjService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/subj")
public class SubjectController {

    private final ISubjService subjService;
    private final SubjRepository subjRepository;

    @Autowired
    public SubjectController(ISubjService subjService, SubjRepository subjRepository) {
        this.subjService = subjService;
        this.subjRepository = subjRepository;
    }

    @PostMapping("/createSubject")
    public ResponseEntity<?> createSubj(@Valid @RequestBody SubjRequest subjRequest) {
        if (subjRepository.existsByName(subjRequest.getName())) {
            throw new RuntimeException("exist");
        } else {
            Subject subj = subjService.createSubj(subjRequest);
            return ResponseEntity.ok().body(subj);
        }
    }

    @GetMapping("/subjects")
    public List<Subject> allSubj() {
        return subjRepository.findAll();
    }

    @DeleteMapping("deleteSubjects/{id}")
    public String deleteSubj(@PathVariable String id) {
        subjService.deleteById(id);
        return id;
    }
}
