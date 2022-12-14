package mongodb.services;

import mongodb.models.Test;

import java.util.List;
import java.util.Optional;

public interface ITestService {

    Test save(Test test);

    void deleteById(String id);

    List<Test> findAll();

    Optional<Test> findById(String id);

    List<Test> findBySubjectAndClasses(String subject, String classes);

}
